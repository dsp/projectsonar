/*
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate Database Connection Class. Provides the high level methods for
 * dealing with a hibernate based database connection.
 * @author Martin Reiche <martin.reiche@student.kit.edu>
 */
class HibernateDatabase implements Database {
    /**
     * The Hibernate session. This will be used throughout the class to
     * characterize the currently active hibernate session and its connection to
     * the data base.
     */
    private SessionFactory _session = null;

    /**
     * Holds the cached Graph. Caching has been implemented for preformance
     * reasons.
     */
    private volatile Graph cachedGraph = null;

    /**
     * Holds an identifier to identify the cache as valid or invalid.
     */
    private volatile Long cacheId = null;

    /**
     * Logger for error logging.
     */
    private static Logger logger = LoggerFactory
            .getLogger(HibernateDatabase.class);

    /**
     * Default constructor. Creates the Hibernate Database connection by using
     * the SessionFactory provided by the HibernateUtil class.
     */
    HibernateDatabase() {
        // HibernateUtil.startup();
        try {
            _session = HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            logger.error("Hibernate initialization failed: " + e.getMessage());
        }

    }

    /**
     * Returns the graph from the underlying data base which is connected via
     * Hibernate.
     * @see Database#getGraph()
     * @throws DataException if no graph could be loaded.
     * @return A new graph from the database.
     */
    public Graph getGraph() throws DataException {
        Graph g = null;

        /* Fundamental caching routine. */
        if (!this.dbChanged() && this.cachedGraph != null) {
            g = this.cachedGraph;
            logger.debug("Graph does not seem to have changed. "
                    + "Taking cached one.");
        } else {
            g = startGraph();
            this.cachedGraph = g;
        }

        if (g == null) {
            logger.error("HibernateDatabase: No graph loaded. Aborted.");
            throw new DataException("Hibernate could not load a graph from the"
                    + "Database");
        }
        logger.debug("Graph created: " + g.getStateHash());
        return g;
    }

    /**
     * Authenticates a user at the Database. Therefore it first searches the
     * data base for the specific user, then checks if the specified password is
     * correct. If both criteria match, the user is logged on and a User object
     * for the new user is created and returned.
     * @see Database#authenticate(String username, String password)
     * @param username The username to authenticate.
     * @param password The password to authenticate.
     * @return A user object of the authenticated user or null.
     */
    public User authenticate(final String username, final String password) {
        Transaction tx = null;
        logger.debug("Login attempt for " + username);

        /* acquire Hibernate session */
        Session session = _session.openSession();

        String hashAlgo = Configuration.getInstance()
                .getUserPasswordHashAlgorithm();
        MessageDigest md = null;

        if (hashAlgo.equals("SHA-1") || hashAlgo.equals("SHA-256")
                || hashAlgo.equals("SHA-512") || hashAlgo.equals("MD5")) {
            try {
                md = MessageDigest.getInstance(hashAlgo);
            } catch (NoSuchAlgorithmException e) {
                logger.error("X: Login attempt could not be processed."
                        + " Specified password hashing method not known."
                        + " Possible hash functions: MD5, SHA-1, SHA-256,"
                        + " SHA-512");
            }
        } else {
            logger.error(":" + hashAlgo + ": Login attempt could not be"
                    + "processed. Specified"
                    + "password hashing method not known. Possible hash"
                    + "functions: MD5, SHA-1, SHA-256, SHA-512");
        }

        try {
            tx = session.beginTransaction();

            /*
             * create SQL query and therefore compute the Hash of the user
             * password firs
             */
            List lst = session.createQuery(
                    "SELECT u FROM User as u WHERE username='"
                            + username
                            + "' AND password='"
                            + new BigInteger(1, md.digest(password.getBytes()))
                                    .toString(16) + "'").list();
            if (!lst.isEmpty()) {
                /*
                 * a user with the given credentials has been found, so login
                 * attempt is successful
                 */
                logger.info("User logged in: " + ((User) lst.get(0)).getName());
                return (User) lst.get(0);
            }

            tx.commit();
            

        } catch (Throwable ex) {
            logger.info("Login attempt for " + username + " failed due to an"
                    + "internal database error.: " + ex.getMessage());
        }

        return null;

    }

    /**
     * Starts to fetch the graph structure from the data source. Also does
     * generic Hibernate Database Error Management.
     * @return the fetched Graph
     */
    private Graph startGraph() {
        try {
            Graph g = new Graph();
            return loadGraph(g);
        } catch (Exception e) {
            if (e instanceof HibernateException) {
                logger.error(e.getMessage());
            } else {
                logger.error("Unknown Error occurred while fetching"
                        + " Graph from data source: " + e.getMessage());
            }
        }

        return null;
    }

    /**
     * Tries to fetch the nodes and edges for the Graph from the data source.
     * This is an internal function! It traverses through all nodes and tries to
     * fetch their connected edges by calling the private loadEdgesForNode
     * function.
     * @param g the graph
     * @return the graph added by the nodes and edges
     * @throws HibernateException
     */
    private Graph loadGraph(Graph g) {
        Transaction tx = null;
        logger.debug("Node Loader started.");

        /* Acquire Hibernate Session */
        Session session = _session.openSession();

        try {
            tx = session.beginTransaction(); /* start DB transaction */
            List nodes = session.createQuery("select n from Node as n").list();

            logger.debug("Starting Node iteration for " + nodes.size());

            /* traversing through all nodes */
            for (Iterator iter = nodes.iterator(); iter.hasNext();) {
                Node node = (Node) iter.next();
                g.addNode(node);
                logger.trace(node.toString());
                g = loadEdgesForNode(g, node, session); /* load the edges */
            }
            logger.info("Graph build complete. Nodes: "
                    + g.getNodeList().size() + ", Edges: "
                    + g.getEdgeList().size());
            tx.commit(); /* transaction completed. */

        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try { /* rollback could possibly fail */
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.debug("Error rolling back transaction");
                }
                // throw again the first exception
                throw e;
            }
            throw e;
        } finally {
            session.close();
        }

        return g;
    }

    /**
     * Loads the Edges for the specified Node. This is an internal function! I
     * is used to load all edges that are connected to the specified node in
     * order to load the nodes from the data base and to interconnect nodes and
     * edges and add them to the graph data structure. This method also detects
     * inconsistencies in the data base in form of Edges that do not or no
     * longer belong to any nodes but have not been deleted from the data base.
     * @param g the Graph object to load the edges for
     * @param node the node object to load the edges for
     * @param s the active Hibernate session
     * @return the Graph with the edges added
     */
    private Graph loadEdgesForNode(final Graph g, final Node node,
            final Session s) {
        /* fetch current session */
        Session session = s;

        try {
            List edges = session.createQuery(
                    "select e from Edge as e where e.sourceNode="
                            + node.getId() + " OR e.destinationNode="
                            + node.getId()).list();

            logger.debug("Looking up edges for " + node.toString());

            /*
             * iterate over all edges that are associated with the specified
             * node
             */
            for (Iterator edgeIter = edges.iterator(); edgeIter.hasNext();) {

                Edge edge = (Edge) edgeIter.next();

                try {
                    if (edge.getDestinationNode().getName() == null
                            || edge.getSourceNode().getName() == null) {
                        logger.info("Database inconsistency detected.");
                    }

                    node.addEdge(edge);
                    g.addEdge(edge);
                    logger.debug(edge.toString()
                            + " found in database and added to parent "
                            + node.toString());

                } catch (Exception e) {
                    if (e instanceof ObjectNotFoundException) {
                        logger.info("Database inconsistency detected"
                                + "for an edge of Node " + node.toString());
                    } else {
                        throw new HibernateException("Database is"
                                + "inconsistent. This inconsistencs"
                                + "could not be handled by Sonar: "
                                + e.getMessage());
                    }
                } // try-catch

            } // for

        } catch (Exception e) {
            if (e instanceof ObjectNotFoundException) {
                logger.info("Database inconsistency detected for Node "
                        + node.getName());
            } else {
                throw new HibernateException("Database is inconsistent."
                        + "This inconsistency could not be handled by Sonar: "
                        + e.getMessage());
            }
        } // try-catch

        return g;

    }

    /**
     * Retrieves the list of available users from the DB. These are all users
     * that can be accessed by Hibernate. Note that the number of users can
     * differ from the number of edges in the graph. Keep this in mind if you
     * are planning to do a Hibernate mapping with a 1-to-n relation between
     * users and nodes!
     * @return array list of available users in the DB
     */
    public ArrayList<User> getUserList() {
        Transaction tx = null;
        ArrayList<User> userlist = new ArrayList<User>();
        logger.debug("Starting userlist query.");

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();
            List users = session.createQuery("select u from User as u").list();
            logger.trace("Userlist query sent to DB.");

            for (Iterator u = users.iterator(); u.hasNext();) {
                User usr = (User) u.next();
                logger.trace("User " + usr.toString() + " found in DB.");
                userlist.add(usr);
            }

        } catch (Exception e) {
            /* Acquire more info about the exception */
            if (e instanceof ObjectNotFoundException) {
                logger.error("Userlist could not be retrieved.: "
                        + e.getMessage());
            } else {
                throw new HibernateException("Userlist query ended up with an"
                        + "error that could not be handled: " + e.getMessage());
            }

        } finally {
            session.close();
        }
        return userlist;
    }

    /**
     * Returns the data base connection on which the Hibernate subsystem builds
     * its queries. This function currently makes use of Hibernate's
     * Session.connection() method, which is deprecated. I should investigate
     * for a better solution in time.
     * @throws DataException if an error ocurred while trying to fetch the
     *             underlying data base connection
     * @return the underlying data base connection
     */
    @SuppressWarnings("deprecation")
    public Connection getUnderLyingConnection() throws DataException {
        return _session.getCurrentSession().connection();
    }

    /**
     * This method returns if the database has changed. NOTE: This
     * implementation consideres the DB to be append-only! This function is
     * crucial for the database caching to work.
     * @return true if the database has been appended, false else
     */
    private boolean dbChanged() {
        Session s = _session.openSession();

        Transaction tx = s.beginTransaction(); /* start DB transaction */
        Long cnt = (Long) s.createQuery("select count(*) from Edge as e")
                .uniqueResult();
        logger.debug("dbChanged? -> new graph identifier: " + cnt.longValue());
        if (this.cacheId == null || this.cacheId.longValue() != cnt.longValue()) {
            this.cacheId = cnt;
            return true;
        }
        tx.commit();
        s.close();

        return false;
    }

}