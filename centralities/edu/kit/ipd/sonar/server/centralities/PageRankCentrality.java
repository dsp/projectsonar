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

import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.Node;

/**
 * Calculates the PageRankCentrality on a given Graph.
 * Calculates the PageRank (PR) iteratively with a damp factor d = 0.85.
 * It Uses the formula
 * PR(node n) = (1 - d) + d * SUM(i={nodes linking to n}, (PR(i)/Outdegree(i))
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class PageRankCentrality extends CentralityImpl {

    /** The dampening factor. Usually set to 0.85. */
    private double dampFactor = 0.85;

    /** The number of iterations that are calculated. */
    private int maxIterations = 20;

    /**
     * Return the Type of centrality.
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
            throw new IllegalArgumentException(
                    "Graph passed to PageRank is null.");
        }

        HashMap<Node, Double> pageRank = new HashMap<Node, Double>();

        //initialize all node's page rank with 1.0:
        for (Node node : g.getNodeList().values()) {
            pageRank.put(node, 1.0);
        }

        HashMap<Node, Double> results = new HashMap<Node, Double>();
        HashMap<Node, Double> temp;

        HashMap<Node, Integer> outdegree = calcOutdegrees(g);
        // iterate a number of times, so the PageRank-values can converge:
        for (int i = 1; i <= maxIterations; i++) {
            //calculate the new PageRank for all Nodes:
            for (Node node : g.getNodeList().values()) {
                double prSum = 0.0;
                for (Edge edge : node.getEdges()) {
                    if (edge.isIncomingEdge(node)) {
                        Node other = edge.getSourceNode();
                        prSum += pageRank.get(other) / outdegree.get(other);
                        // as "other" is always the sourceNode of "edge", the
                        // outdegree will be > 0 and we avoid DIV0 errors.
                    }
                }
                results.put(node,
                            (1 - dampFactor) + dampFactor * prSum);

            }

            //switch the pointers to the result- and pagerank-maps.
            temp = pageRank;
            pageRank = results;
            results = temp;
        }

        return pageRank;
    }

    /**
     * Subroutine to Calc the Outdegrees of all Nodes within a graph.
     * @param g The graph for which the outdegrees shall be calculated.
     * @return a hashmap containing the outdegree of each node.
     */
    private HashMap<Node, Integer> calcOutdegrees(Graph g) {

        HashMap<Node, Integer> values = new HashMap<Node, Integer>();
        //add all nodes to the list and set their degree to 0.0:
        for (Node node : g.getNodeList().values()) {
            values.put(node, 0);
        }
        //increase the degree of each node by one for each outgoing edge:
        for (Edge edge : g.getEdgeList()) {
            int degree = values.get(edge.getSourceNode());
            values.put(edge.getSourceNode(), (degree + 1));
        }
        return values;
    }

    /**
     * @see Centrality#getRequiredAPIVersion()
     * @return The required api version
     */
    public int getRequiredAPIVersion() {
        return 0;
    }

    /**
     * @see Centrality#getVersion()
     * @return The version
     */
    public int getVersion() {
        return 1;
    }

    /**
     * @see Centrality#getName()
     * @return The name
     */
    public String getName() {
        return "PageRank";
    }

}
