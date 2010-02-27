/*
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.client;

import java.util.ArrayList;

/**
 * Represents a graph with nodes and edges that can be drawn to the screen by
 * a GraphDrawer.
 */
public class DrawableGraph {

    /** The nodes in this graph. */
    private final ArrayList<DrawableNode> nodes;

    /** The edges in this graph. */
    private final ArrayList<DrawableEdge> edges;

    /**
     * Gets the nodes in this graph.
     *
     * @return An ArrayList containing the DrawableNodes in this Graph.
     */
    public ArrayList<DrawableNode> getNodeList() {
        return this.nodes;
    }

    /**
     * Gets the edges in this graph.
     *
     * @return An ArrayList containing the DrawableEdges in this Graph.
     */
    public ArrayList<DrawableEdge> getEdgeList() {
        return this.edges;
    }

    /**
     * Creates a new DrawableEdge with the given nodes and edges.
     *
     * @param nodes An ArrayList containing the DrawableNodes.
     * @param edges An ArrayList containing the DrawableEdges.
     */
    public DrawableGraph(final ArrayList<DrawableNode> nodes,
                         final ArrayList<DrawableEdge> edges) {
        for (DrawableEdge edge : edges) {
            if (!nodes.contains(edge.getSource())
                    || !nodes.contains(edge.getDestination())) {
                throw new IllegalArgumentException("Got edge pointing to nodes"
                                                   + "not in the graph!");
            }
        }
        this.nodes = nodes;
        this.edges = edges;
    }
}
