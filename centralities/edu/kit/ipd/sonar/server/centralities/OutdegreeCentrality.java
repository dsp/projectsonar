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

import edu.kit.ipd.sonar.server.Annotable;
import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.Node;

/**
 * Calculates the outdegree of all nodes of a graph.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class OutdegreeCentrality extends CentralityImpl {
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
                    "Graph passed to Outdegree is null.");
        }

        HashMap<Node, Double> values = new HashMap<Node, Double>();

        //add all nodes to the list and set their degree to 0.0:
        for (Node node : g.getNodeList().values()) {
            values.put(node, 0.0);
        }

        //increase the degree of each node by one for each outgoing edge:
        for (Edge edge : g.getEdgeList()) {
            double degree = values.get(edge.getSourceNode());
            values.put(edge.getSourceNode(),
                       (degree + 1));
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
        return "Outdegree";
    }
}
