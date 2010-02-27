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
package edu.kit.ipd.sonar.server.centralities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.Node;

/**
 * Calculate the betweenness-centrality on all edges of a graph.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class EdgeBetweennessCentrality extends CentralityImpl {
    /**
     * Return the Type of this centrality.
     *
     * @return The type
     */
    public Type getType() {
        return Type.EdgeCentrality;
    }

    /**
     * @see CentraltiyImpl#getWeight(Graph g)
     * @param g The graph
     * @return The mapping
     */
    public HashMap<Edge, Double> getWeight(final Graph g) {

        if (g == null) {
            throw new IllegalArgumentException("Graph passed to EdgeBetweenness"
                        + " Centrality is null.");
        }

        /* Calculating the unweighted Edge-Betweenness centrality.
         * I modified Brandes algorithm, which i used to calculate the
         * Node Betweenness, so that it Calcultes the Edge-Betweenness
         * For further information, please see the NodeBetweenness-
         * Implementation within SONAR or read
         * Brandes, Ulrik: "A Faster Algorithm for Betweenes Centrality",
         * Published in Journal of Mathematical Sociology 25(2):163-177, 2001.
         */

        // init the betweenness-values for each node with 0.0:
        HashMap<Edge, Double> betweenness = new HashMap<Edge, Double>();
        for (Edge e : g.getEdgeList()) {
            betweenness.put(e, 0.0);
        }

        for (Node startNode : g.getNodeList().values()) {

            /* Use dijkstra's algorithm to calculate all shortest paths
             * from the startode: */

            //A Stack used to fetch the used nodes in reversed direction:
            Stack<Node> destinationStack = new Stack<Node>();

            /* For each node, store a list of incident edges over which
             * shortest paths to that node lead: */
            HashMap<Node, LinkedList<Edge>> shortestPathIncEdges =
                        new HashMap<Node, LinkedList<Edge>>();

            /* For each node, store how many shortest paths exist between it
             * and the Startnode */
            HashMap<Node, Integer> numberOfShortPaths =
                        new HashMap<Node, Integer>();

            /* For each node, store the length of the shortest path to it
             * from the startnode */
            HashMap<Node, Integer> shortestPathLength =
                        new HashMap<Node, Integer>();

            // init the HashMaps
            for (Node n : g.getNodeList().values()) {
                shortestPathIncEdges.put(n, new LinkedList<Edge>());
                numberOfShortPaths.put(n, 0);
                shortestPathLength.put(n, -1);
            }
            numberOfShortPaths.put(startNode, 1);
            shortestPathLength.put(startNode, 0);

            Queue<Node> bfsQueue = new LinkedList<Node>();
            bfsQueue.add(startNode);

            //the Dijkstra-main-loop:
            while (!bfsQueue.isEmpty()) {
                Node predecessor = bfsQueue.remove();
                destinationStack.push(predecessor);


                //repeat for all outgoing edges:
                for (Edge edge : predecessor.getEdges()) {
                    if (edge.isOutgoingEdge(predecessor)) {

                        // get the node the edge is pointing to:
                        Node neighbor = edge.getDestinationNode();

                        //is the neighbor found for the first time?
                        if (shortestPathLength.get(neighbor) < 0) {
                            bfsQueue.add(neighbor);
                            shortestPathLength.put(neighbor,
                                    shortestPathLength.get(predecessor) + 1);
                        }
                        // Is the path found to the neighbor a shortest path?
                        if (shortestPathLength.get(neighbor)
                                == shortestPathLength.get(predecessor) + 1) {
                            /* Add together the numbers of shortest paths and
                             * append the predecessor to the list: */
                            numberOfShortPaths.put(neighbor,
                                      numberOfShortPaths.get(neighbor)
                                    + numberOfShortPaths.get(predecessor));
                            shortestPathIncEdges.get(neighbor)
                                                    .add(edge);
                        }
                    }
                }
            }

            /* For each edge, we calculate the dependency of the startnode
             * on it. For an explanation what it means,
             * please see Brandes' paper referenced above. */

            // init the dependencys:
            HashMap<Edge, Double> dependency =
                        new HashMap<Edge, Double>();
            for (Edge e : g.getEdgeList()) {
                dependency.put(e, 0.0);
            }

            /* calculate the number of shortest paths through the edges
             * from the startnode.
             * therefore sum up the number of paths beginning with the
             * node reached at last during dijkstra-bfs-search.
             */

            while (!destinationStack.isEmpty()) {
                Node v = destinationStack.pop();
                double depSum = 0.0;
                for (Edge e : v.getEdges()) {
                    if (e.isOutgoingEdge(v)) {
                        depSum += dependency.get(e);
                    }
                }
                for (Edge e : shortestPathIncEdges.get(v)) {

                    dependency.put(e,
                            ((double) numberOfShortPaths.get(e.getSourceNode())
                            / (double) numberOfShortPaths.get(v))
                            * (1.0 + depSum));

                    // add the dependency to the overall betweeenness:
                    betweenness.put(e,
                            betweenness.get(e) + dependency.get(e));

                }
            }
        }
        return betweenness;

    }

    /**
     * @see EdgeCentrality#getRequiredAPIVersion
     * @return The required api version
     */
    public int getRequiredAPIVersion() {
        return 0;
    }

    /**
     * @see EdgeCentrality#getVersion
     * @return the version
     */
    public int getVersion() {
        return 1;
    }

    /**
     * @see EdgeeCentrality#getName
     * @return The name
     */
    public String getName() {
        return "Edge Betweenness";
    }
}
