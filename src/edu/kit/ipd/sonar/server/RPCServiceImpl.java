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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.kit.ipd.sonar.client.rpc.RPCService;
import edu.kit.ipd.sonar.server.centralities.Centrality;
import edu.kit.ipd.sonar.server.centralities.CentralityImpl;
import edu.kit.ipd.sonar.server.centralities.CentralityLoader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation class of the main RPC service.
 *
 * This service handles all requests from the client.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class RPCServiceImpl extends RemoteServiceServlet implements RPCService {
    /**
     * The Database connection.
     */
    private Database database;

    /**
     * A global calculator.
     */
    private Calculator globalCalculator;

    /**
     * A peer calculator.
     */
    private Calculator peerCalculator;

    /**
     * Centrality loader.
     */
    private CentralityLoader loader;

    /**
     * Logging instance.
     */
    private Logger log = LoggerFactory.getLogger(RPCServiceImpl.class);

    /**
     * A mapping between Centrality and CentralityImpl.
     */
    private HashMap<Integer, CentralityImpl> mapping
        = new HashMap<Integer, CentralityImpl>();
    /**
     * Enumerates possible access values.
     *
     * @see hasAccessRight
     */
    private enum Access {
        /**
         * Marks an user with admin access rights.
         */
        ADMIN,

        /**
         * Marks an user with just user access rights.
         */
        USER
    }

    /**
     * Returns the current user that is logged in.
     *
     * @return The current user object or null.
     */
    private User getCurrentUser() {
        HttpServletRequest request = getThreadLocalRequest();
        HttpSession session = request.getSession();
        AuthenticationResult obj =
            (AuthenticationResult) session.getAttribute("userAuth");
        if (null == obj) {
            return null;
        }

        return obj.getUser();
    }

    /**
     * Checks if the current user has the requested access right.
     *
     * @param a The requested access right.
     * @return True if user is allowed, otherwise false.
     */
    private boolean hasAccessRight(final Access a) {
        HttpServletRequest request = getThreadLocalRequest();
        HttpSession session = request.getSession();
        switch(a) {
            case ADMIN:
                Boolean obj = (Boolean) session.getAttribute("isAdmin");
                return (null != obj && obj);
            case USER:
                AuthenticationResult usr =
                    (AuthenticationResult) session.getAttribute("userAuth");
                return (null != usr && usr.isSuccessful());
            default:
                return false;
        }
    }

    /**
     * Main initialization method.
     *
     * @param config The configuration as provided by the servlet container.
     * @throws ServletException If the service cannot be started for some
     * reasion
     */
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        log.info("server started");
        try {
            database = DatabaseFactory.createInstance(
                    Configuration.getInstance());
        }
        catch (DataException de) {
            throw new ServletException(de);
        }
        
        globalCalculator = CalculatorFactory
            .createCalculatorForGlobalGraphs();
        peerCalculator = CalculatorFactory
            .createCalculatorForPeerGraphs();

        loader = CentralityLoader.createInstance();
        loader.reload();
        for (CentralityImpl c : loader.getAvailableCentralities()) {
            mapping.put(c.hashCode(), c);
        }
    }

    /**
     * Tries to log in as a user.
     *
     * @param username  The username.
     * @param password  The password.
     *
     * @throws DataException If an error during data processing occurs.
     * @return An AuthenticationResult object representing the outcome of
     *         this call.
     */
    public AuthenticationResult authenticateUser(final String username,
            final String password) throws DataException {
        User u = database.authenticate(username, password);
        AuthenticationResult result = new AuthenticationResult(false, null);
        if (u != null) {
            HttpServletRequest request = getThreadLocalRequest();
            HttpSession session = request.getSession();

            result = new AuthenticationResult(true, u);

            session.setAttribute("isAdmin", false);
            session.setAttribute("userAuth", result);
        }
        return result;
    }

    /**
     * Tries to log in as admin.
     *
     * @param password The password.
     *
     * @return True if the authentication was successful, false otherwise.
     */
    public boolean authenticateAdmin(final String password) {
        boolean success = password.equals(
                Configuration.getInstance().getAdminPassword());

        log.debug("authenticateAdmin called");
        if (success) {
            HttpServletRequest request = getThreadLocalRequest();
            HttpSession session = request.getSession();

            session.setAttribute("isAdmin", true);
            session.setAttribute("userAuth", null);
        } else {
            log.warn("Wrong administration authentication attempt");
        }

        return success;
    }

    /**
     * Get the available centralities from the server.
     *
     * The method does not reload the list. This has to be done by the sever
     * administartor.
     *
     * @throws NotAuthorizedException if the user is not authorized.
     *
     * @return An ArrayList containing the available centralities.
     */
    public ArrayList<Centrality> getAvailableCentralities() throws
        NotAuthorizedException {
        log.debug("getAvailableCentralities called");
        if (!hasAccessRight(Access.ADMIN) && !hasAccessRight(Access.USER)) {
            throw new NotAuthorizedException();
        }

        ArrayList<Centrality> al = new ArrayList<Centrality>();
        for (CentralityImpl ci : loader.getAvailableCentralities()) {
            al.add(ci.getCentrality());
        }

        return al;
    }

    /**
     * Get the time boundary over which the available data spans.
     *
     * The method returns the interval between the first created node and
     * the last created node or edge.
     *
     * @throws NotAuthorizedException if the user is not authorized.
     * @throws DataException If an error during data processing occurs.
     *
     * @return The maximal time boundary.
     */
    public TimeBoundary getTimeBoundary() throws DataException,
        NotAuthorizedException {
        log.debug("getTimeBoundary called");
        if (!hasAccessRight(Access.ADMIN) && !hasAccessRight(Access.USER)) {
            throw new NotAuthorizedException();
        }

        return database.getGraph().getMaxTimeBoundary();
    }

    /**
     * Get the available users.
     *
     * @throws NotAuthorizedException if the user is not authorized.
     * @throws DataException If an error during data processing occurs.
     *
     * @return An ArrayList containing the users in the game.
     */
    public ArrayList<User> getUserList() throws DataException,
        NotAuthorizedException {
        log.debug("getUserList called");

        if (hasAccessRight(Access.ADMIN)) {
            log.info(database.getUserList().toString());
            return database.getUserList();
        }

        if (hasAccessRight(Access.USER)) {
            ArrayList<User> result = new ArrayList<User>();
            result.add(getCurrentUser());
            log.info(getCurrentUser().toString());
            return result;
        }

        throw new NotAuthorizedException();
    }

    /**
     * Get the graph specified by the given values.
     *
     * The method calculates a graph without a central node.
     * This is called a global graph. If maxNodes is given only the given amount
     * of nodes are respected.
     *
     * At least one centrality needs to be a node centraltiy if a maxNodes value
     * is given, otherwise a CalculationFailedException will be thrown.
     *
     * Note that the user needs to be logged in and
     * needs admin rights to be able to use this method. Otherwise a
     * NotAuthorizedException will be thrown.
     *
     * @param timeBound     The time boundary for the graph.
     * @param centralities  The centralities to calculate.
     * @param maxNodes      The number of nodes in the graph.
     *
     * @throws CalculationFailedException If the calculation fails.
     * @throws NotAuthorizedException If the user is not authorized
     * @throws DataException If the database fails to retreive the data.
     *
     * @return  A graph as defined by the parameters.
     */
    public Graph getGlobalGraph(final TimeBoundary timeBound,
            final ArrayList<Centrality> centralities, final int maxNodes)
        throws CalculationFailedException, NotAuthorizedException,
        DataException {
        log.debug("getGlobalGraph called");

        if (!hasAccessRight(Access.ADMIN)) {
            throw new NotAuthorizedException();
        }

        ArrayList<CentralityImpl> impl = new ArrayList<CentralityImpl>();
        for (Centrality c : centralities) {
            impl.add(mapping.get(c.hashCode()));
        }
        log.debug(impl.toString());

        return globalCalculator.calc(database.getGraph(), impl,
            timeBound, maxNodes, null);
    }

    /**
     * Get the peer graph specified by the given values.
     *
     * The method calculates a graph with a central node. The central node is
     * the given user within the social network. If hops is provided only the
     * given amount of hops from the central node are respected during the
     * calculation and the resulting graph will not contain the other nodes.
     *
     * If the user is not an administrator he can only request a peer graph for
     * his own user. If the user tries to obtain a peer graph for another user a
     * NotAuthorizedException is thrown. The user has to be logged in.
     *
     * @param user          The user whose node will be the center node.
     * @param timeBound     The time boundary for the graph.
     * @param centralities  The centralities to calculate.
     * @param hops          The number of hops. Nodes that are more than
     *                          'hops' hops away from the central node
     *                          won't be included.
     *
     * @throws CalculationFailedException If the calculation fails.
     * @throws NodeDoesNotExistException If the given user doesn't exists.
     * @throws NotAuthorizedException If the user is not authorized
     * @throws DataException If the database fails to retreive the data.
     *
     * @return  A graph as defined by the parameters.
     */
    public Graph getPeerGraph(final User user, final TimeBoundary timeBound,
            final ArrayList<Centrality> centralities, final int hops)
        throws CalculationFailedException, NodeDoesNotExistException,
        NotAuthorizedException, DataException {
        log.debug("getPeerGraph called");

        boolean auth = false;
        if (hasAccessRight(Access.USER)) {
            User obj = getCurrentUser();
            if (obj != null && obj.getId() - user.getId() <= 0) {
                auth = true;
            }
        }

        if (hasAccessRight(Access.ADMIN)) {
            auth = true;
        }

        if (!auth) {
            throw new NotAuthorizedException();
        }

        Graph g = database.getGraph();
        ArrayList<CentralityImpl> impl = new ArrayList<CentralityImpl>();
        for (Centrality c : centralities) {
            impl.add(mapping.get(c.hashCode()));
        }
        return peerCalculator.calc(g, impl, timeBound, hops, g.getNodeById(user.getId()));
    }

    /**
     * Returns a hash of the database.
     *
     * This means that this function returns the same value exactly
     * until the database has changed.
     *
     * @throws DataException If an error during data processing occurs.
     * @return The hash int.
     */
    public int getStateHash() throws DataException {
        log.debug("getStateHash called");
        int state = database.getGraph().getStateHash();
        log.info(Integer.toString(state));

        return state;
    }

    /**
     * Logout user and invalidate the current session.
     */
    public void logout() {
        log.debug("logout called");
        HttpServletRequest request = getThreadLocalRequest();
        HttpSession session = request.getSession();

        session.invalidate();
    }
}
