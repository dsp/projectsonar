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
import edu.kit.ipd.sonar.server.centralities.*;
import static org.junit.Assert.*;
import java.util.*;
import java.lang.reflect.*;

final public class TestUtil {
    /**
     * Return a new configuration by init the object the private constructor via
     * reflect.
     *
     * We must not use a security manager, only chuck norris would be able to
     * instantiate a class _with_ a security manager in place. (maybe mc guyver
     * would use a needle to fix it).
     */
    public static Configuration getConfiguration(String configPath) {
        try {
            /* get the constructor as a method */
            Constructor<Configuration> cstr
                = Configuration.class.getDeclaredConstructor(
                        new Class<?>[]{String.class});
            /* make sure we can access it */
            cstr.setAccessible(true);
            /* go for it */
            return cstr.newInstance(configPath);
        } catch (Exception ie) {
            fail("Cannot instantiate private Configuration"
                    + " constructor with parameter ");
        }
        return null;
    }

    public static CentralityImpl getEqualCentrality() {
        return new CentralityImpl() {

            public Type getType() {
                return Type.NodeCentrality;
            }
            public int getRequiredAPIVersion() {
                return 0;
            }

            public int getVersion() {
                return 1;
            }

            public String getName() {
                return "Equal";
            }
            public HashMap<Node, Double> getWeight(Graph g) {
                HashMap<Node, Double> map = new HashMap<Node, Double>();
                Collection<Node> nl = g.getNodeList().values();
                for(Node n : nl) {
                    map.put(n, 1.0);
                }
                return map;
            }
        };
    }

    public static CentralityImpl getNullCentrality() {
        return new CentralityImpl() {

            public Type getType() {
                return Type.NodeCentrality;
            }
            public int getRequiredAPIVersion() {
                return 0;
            }

            public int getVersion() {
                return 1;
            }

            public String getName() {
                return "Blub";
            }

            public HashMap<Node, Double> getWeight(Graph g) {
                return null;
            }
        };
    }

    public static CentralityImpl getEmptyCentrality() {
        return new CentralityImpl() {

            public Type getType() {
                return Type.NodeCentrality;
            }
            public int getRequiredAPIVersion() {
                return 0;
            }

            public int getVersion() {
                return 1;
            }

            public String getName() {
                return "Blub";
            }

            public HashMap<Node, Double> getWeight(Graph g) {
                return new HashMap<Node, Double>();
            }
        };
    }

    public static CentralityImpl getNodeCentrality() {
        return new CentralityImpl() {

            public Type getType() {
                return Type.NodeCentrality;
            }
            public int getRequiredAPIVersion() {
                return 0;
            }

            public int getVersion() {
                return 1;
            }

            public String getName() {
                return "Blub";
            }
            public HashMap<Node, Double> getWeight(Graph g) {
                HashMap<Node, Double> map = new HashMap<Node, Double>();
                Collection<Node> nl = g.getNodeList().values();
                for(Node n : nl) {
                    map.put(n, 1.0/n.getId());
                }
                return map;
            }
        };
    }
    /**
     * Creates a graph needed for testing.
     *
     * @return the graph.
     */
    public static Graph getGraphMock() {
        Graph g;
        Node n1, n2, n3, n4, n5, n6;
        Edge e12, e13, e14, e24, e23, e35, e45, e52;

        g = new Graph();
        n1 = new Node(1, "Node 1", 1);
        n2 = new Node(2, "Node 2", 2);
        n3 = new Node(3, "Node 3", 3);
        n4 = new Node(4, "Node 4", 4);
        n5 = new Node(5, "Node 5", 5);

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n4);
        g.addNode(n5);

        try {
            g.setCentralNode(n2);
        } catch (Exception e) {}

        e12 = new Edge(n1, n2, 3);
        e13 = new Edge(n1, n3, 3);
        e14 = new Edge(n1, n4, 3);
        e24 = new Edge(n2, n4, 3);
        e23 = new Edge(n2, n3, 3);
        e35 = new Edge(n3, n5, 3);
        e45 = new Edge(n4, n5, 3);
        e52 = new Edge(n5, n2, 3);

        g.addEdge(e12);
        g.addEdge(e13);
        g.addEdge(e14);
        g.addEdge(e24);
        g.addEdge(e23);
        g.addEdge(e35);
        g.addEdge(e45);
        g.addEdge(e52);

        return g;
    }

    /**
     * Returns an empty Graph. needed for testing purposes,
     * as the constructor of Graph is only package-visible.
     *
     * @return a new empty Graph-object.
     */
    public static Graph getEmtpyGraph() {
        return new Graph();
    }

    /**
     * Returns a Simple Graph, with Nodes and Edges having
     * their original weights set.
     * @return the graph
     */
    public static Graph getOriginalWeightedGraph() {

        Graph g = new Graph();

        Node n1 = new Node(1, "", 0);
        n1.setOriginalWeight(4.0);
        Node n2 = new Node(2, "", 0);
        n2.setOriginalWeight(5.0);
        Node n3 = new Node(3, "", 0);
        n3.setOriginalWeight(1.0);

        Edge e1 = new Edge(n1, n2);
        e1.setOriginalWeight(2.0);
        Edge e2 = new Edge(n2, n3);
        e2.setOriginalWeight(2.0);

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addEdge(e1);
        g.addEdge(e2);

        return g;

    }

    public static Calculator getCalculator() {
        return new Calculator() {
            public Graph calc(Graph g, ArrayList<CentralityImpl> c,
                    TimeBoundary b, Integer l) throws CalculationFailedException {
                return null;
            }
        };
    }
}
