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

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Simple testing suite for the Hiberante Database Connection. Extensible Tests
 * to follow.
 * 
 * @author Martin Reiche <martin.reiche@student.kit.edu>
 * 
 */
public class HibernateDatabaseTest {

    /**
     * The Datasource.
     */
    Database db = null;

    /**
     * Constructs the test case. Therefore the physical database connection will
     * be overridden by a mocked connection.
     */
    public HibernateDatabaseTest() {

        Configuration config = TestUtil.getConfiguration(
                "edu/kit/ipd/sonar/server/sampleconfig.xml");


        try {
            /* we have to use reflection as this is a private method.
               Without startup we cannot test this method */
            Method m = HibernateUtil.class.getDeclaredMethod(
                    "startup", new Class[] {Configuration.class});

            /* make sure we can access the method */
            m.setAccessible(true);

            /* we call from static context, so obj (first argument) is null */
            m.invoke(null, config);
        } catch (InvocationTargetException ite) {
            fail(ite.toString());
        } catch(Exception e) {
            fail("cannot use reflection to instantiate HibernateUtil.startup " + e.toString());
        }
        try {
            db = DatabaseFactory.createInstance(config);
        } catch (DataException e) {
            fail();
            e.printStackTrace();
        }
    }

    /**
     * Starts the sessions. Will be executed before testing.
     */
    @Before
    public void startSession() {
        // No longer used.
        // db = DatabaseFactory.createInstance(Configuration.getInstance());
    }

    /**
     * Simple authentication test at the data source.
     */
    @Test
    public void testAuthenticate() {
        try {
            assertNotNull(db.authenticate("martin", "martin"));
            assertNotNull(db.authenticate("admin", "admin"));
        } catch (DataException e) {
            fail("User auth failed.");
            e.printStackTrace();
        }

    }

    /**
     * Simple test if Graph can be created from the data source.
     */
    @Test
    public void testGraphCreation() {
        try {
            Graph g = db.getGraph();
            Graph f = db.getGraph();
            assertNotNull(g);
        } catch (DataException e) {
            fail();
            e.printStackTrace();
        }
    }

    /**
     * Simple test if Graph is created in the correct way and if Graph.equals
     * works the way it should.
     */
    @Test
    public void testGraph() {
        Graph g = new Graph();
        Node n0 = new Node(0, "n0", 55);
        n0.setOriginalWeight(0.23);
        Node n1 = new Node(1, "n1", 50);
        Node n2 = new Node(2, "n2", 51);
        Node n3 = new Node(3, "n3", 52);
        Node n4 = new Node(4, "n4", 53);
        Node n5 = new Node(5, "n5", 54);
        Node n6 = new Node(6, "n6", 58);
        Node n7 = new Node(7, "n7", 57);
        Node n8 = new Node(8, "n8", 58);
        Node n15 = new Node(15, "n15!!awesome", 56);
        n1.setOriginalWeight(0.5);
        n2.setOriginalWeight(0.2);
        n3.setOriginalWeight(0.93);
        n4.setOriginalWeight(0.33);
        n5.setOriginalWeight(0.34);
        n6.setOriginalWeight(0.4);
        n7.setOriginalWeight(0.2);
        n8.setOriginalWeight(0.33);
        n15.setOriginalWeight(3.444);

        Edge e0 = new Edge(n1, n0, 53);
        Edge e1 = new Edge(n3, n0, 54);
        Edge e2 = new Edge(n0, n1, 52);
        Edge e3 = new Edge(n2, n1, 58);
        Edge e4 = new Edge(n3, n1, 53);
        Edge e5 = new Edge(n1, n2, 56);
        Edge e6 = new Edge(n3, n2, 55);
        Edge e7 = new Edge(n4, n2, 53);
        Edge e8 = new Edge(n0, n3, 55);
        Edge e9 = new Edge(n2, n3, 54);
        Edge e10 = new Edge(n4, n3, 56);
        Edge e11 = new Edge(n3, n5, 60);
        Edge e12 = new Edge(n8, n5, 61);
        Edge e13 = new Edge(n5, n6, 58);
        Edge e14 = new Edge(n3, n7, 58);
        Edge e15 = new Edge(n5, n7, 59);
        Edge e16 = new Edge(n6, n8, 60);
        e0.setOriginalWeight(0.323);
        e1.setOriginalWeight(0.34);
        e2.setOriginalWeight(0.445);
        e3.setOriginalWeight(0.2745);
        e4.setOriginalWeight(0.87);
        e5.setOriginalWeight(0.34);
        e6.setOriginalWeight(0.666);
        e7.setOriginalWeight(0.263);
        e8.setOriginalWeight(0.403);
        e9.setOriginalWeight(0.55554);
        e10.setOriginalWeight(0.232);
        e11.setOriginalWeight(0.224);
        e12.setOriginalWeight(0.34);
        e13.setOriginalWeight(0.564);
        e14.setOriginalWeight(0.234);
        e15.setOriginalWeight(0.835);
        e16.setOriginalWeight(1.0e-4);

        g.addEdge(e0);
        g.addEdge(e1);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);
        g.addEdge(e7);
        g.addEdge(e8);
        g.addEdge(e9);
        g.addEdge(e10);
        g.addEdge(e11);
        g.addEdge(e12);
        g.addEdge(e13);
        g.addEdge(e14);
        g.addEdge(e15);
        g.addEdge(e16);

        g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.addNode(n6);
        g.addNode(n7);
        g.addNode(n8);
        g.addNode(n15);

        try {
            Graph f = db.getGraph();
            assertNotNull(f);
            fail(String.valueOf(g.hashCode()));
            assertTrue(g.equals(f));
            
        } catch (DataException e) {
            fail("DataException occured while trying to test for Graph eq.");
            e.printStackTrace();
        }

    }

    /**
     * Tests the Userlist acquiring function getUserList.
     */
    @Test
    public void testGetUserList() {
        try {
            ArrayList<User> usrs = new ArrayList<User>();
            ArrayList<User> usrlst;
            usrlst = db.getUserList();

            usrs.add(new User(1, "admin"));
            usrs.add(new User(2, "martin"));
            usrs.add(new User(3, "foobar"));
            assertTrue(usrlst.size() == 3);
            assertTrue(usrs.get(0).equals(usrlst.get(0))
                    || usrs.get(1).equals(usrlst.get(0))
                    || usrs.get(2).equals(usrlst.get(0)));
            assertTrue(usrs.get(1).equals(usrlst.get(1))
                    || usrs.get(0).equals(usrlst.get(1))
                    || usrs.get(2).equals(usrlst.get(1)));
            assertTrue(usrs.get(0).equals(usrlst.get(2))
                    || usrs.get(1).equals(usrlst.get(2))
                    || usrs.get(2).equals(usrlst.get(2)));
        } catch (DataException e) {
            fail("DataException while unit testing getUserList()");
            e.printStackTrace();
        }
    }

    /**
     * Closes the session after all work has been done.
     */
    @After
    public void closeSession() {
        db = null;
        HibernateUtil.shutdown();
    }

}
