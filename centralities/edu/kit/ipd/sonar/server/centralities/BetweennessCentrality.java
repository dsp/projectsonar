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
 * Calculates the betweenness-centrality on all nodes in a graph.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class BetweennessCentrality extends CentralityImpl {
    /**
     * Return the Type of this centrality.
     *
     * @return The type.
     */
    public Type getType() {
        return Type.NodeCentrality;
    }

    /**
     * @see CentralityImpl#getWeight(Graph g)
     * @param g The graph
     * @return The mapping
     */
    public HashMap<Node, Double> getWeight(final Graph g) {

        if (g == null) {
            throw new IllegalArgumentException("Graph passed to Betweenness"
                        + " Centrality is null.");
        }

        /* Calculating the unweighted betweenness centrality
         * using Brandes' Algorithm, for further information,
         * description and a formal proof please see:
         * Brandes, Ulrik: "A Faster Algorithm for Betweenes Centrality",
         * Published in Journal of Mathematical Sociology 25(2):163-177, 2001
         */

        // init the betweenness-values for each node with 0.0:
        HashMap<Node, Double> betweenness = new HashMap<Node, Double>();
        for (Node n : g.getNodeList().values()) {
            betweenness.put(n, 0.0);
        }

        for (Node startNode : g.getNodeList().values()) {

            /* Use dijkstra's algorithm to calculate all shortest paths
             * from the startode: */

            //A Stack used to fetch the reached nodes in reversed direction:
            Stack<Node> destinationStack = new Stack<Node>();

            /* For each node, store a list of adjacent nodes over which
             * shortest paths to that node lead: */
            HashMap<Node, LinkedList<Node>> shortestPathPredecessors =
                        new HashMap<Node, LinkedList<Node>>();

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
                shortestPathPredecessors.put(n, new LinkedList<Node>());
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
                            shortestPathPredecessors.get(neighbor)
                                                    .add(predecessor);
                        }
                    }
                }
            }

            /* For each node, we calculate the dependency of the startnode
             * on it. For an explanation what it means,
             * please see Brandes' paper referenced above. */

            // init the dependencys:
            HashMap<Node, Double> dependency =
                        new HashMap<Node, Double>();
            for (Node n : g.getNodeList().values()) {
                dependency.put(n, 0.0);
            }

            /* calculate the dependencys between the startnode and the other
             * nodes in reverse order in which they were reached: */

            while (!destinationStack.isEmpty()) {
                Node w = destinationStack.pop();
                for (Node v : shortestPathPredecessors.get(w)) {
                    Double value = dependency.get(v);
                    value += ((double) numberOfShortPaths.get(v)
                            / (double) numberOfShortPaths.get(w))
                            * (1.0 + dependency.get(w));

                    dependency.put(v, value);
                }
                // sum up the dependencys to the overall betweeenness:
                if (w != startNode) {
                    betweenness.put(w,
                                betweenness.get(w) + dependency.get(w));
                }
            }
        }
        return betweenness;

    }

    /**
     * @see NodeCentrality#getRequiredAPIVersion
     * @return The required api version
     */
    public int getRequiredAPIVersion() {
        return 0;
    }

    /**
     * @see NodeCentrality#getVersion
     * @return the version
     */
    public int getVersion() {
        return 1;
    }

    /**
     * @see NodeCentrality#getName
     * @return The name
     */
    public String getName() {
        return "Node Betweenness";
    }
}
