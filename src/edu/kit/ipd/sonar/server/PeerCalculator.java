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

import edu.kit.ipd.sonar.server.centralities.CentralityImpl;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calculates the centralities for the a peer graph.
 *
 * The term Peer Graph in Sonar refers to a graph with multiple edges and an
 * information about a central node. The central node represents the peer and
 * the graph will contain a view of the peer to his friends. The view of the
 * peer is limited to a maximum of hops away from himself. A hop limit of 2
 * means that his second grade friends will be the last visible nodes in the
 * graph.
 *
 * The peer graph has to create a copy of the graph before it manipulates the
 * graph.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
class PeerCalculator implements Calculator {
    /**
     * Logger for error logging.
     */
    private static Logger log = LoggerFactory.getLogger(PeerCalculator.class);

    /**
     * Calculates the values for the requested centralities on the given graph
     * and returns a new graph.
     *
     * The limit here means that the limit of hops from the center.
     *
     * @see Calculator#calc(Graph g, ArrayList c,
     *          TimeBoundary boundary, int limit)
     * @param g The graph to use for calculation.
     * @param c A list centralities to use.
     * @param boundary An optional time boundary. Null if unused
     * @param limit An optional limit. Negative or zero if unused.
     *
     * @throws CalculationFailedException if the calculation fails somehow
     *
     * @return A new graph
     */
    public Graph calc(final Graph g, final ArrayList<CentralityImpl> c,
        final TimeBoundary boundary, final int limit)
        throws CalculationFailedException {
        if (null == g) {
            throw new IllegalArgumentException("Passed graph is null");
        }

        if (0 == c.size()) {
            return g;
        }

        Graph ng = g;
        if (boundary != null && !boundary.contains(g.getMaxTimeBoundary())) {
            log.debug("use time boundary");
            ng = CalculatorUtil.getTimeBoundedGraph(g, boundary);
        }

        if (ng.getNodeList().size() == 0) {
            throw new CalculationFailedException("Graph is empty");
        }

        if (null == ng.getCentralNode()) {
            throw new CalculationFailedException("Graph has no central node");
        }

        Graph newGraph;
        if (limit > 0 && limit < ng.getNodeList().size()) {
            newGraph = getHopLimitedTree(ng, limit);
        } else {
            newGraph = getNewGraph(ng,
                    ng.getNodeList().values(), -1);
        }

        for (CentralityImpl cr : c) {
            CalculatorUtil.addCentralities(newGraph, ng, cr);
        }
        return newGraph;
    }

    /**
     * Generates a graph with all nodes that are maxHops away from the central
     * node of the graph.
     *
     * @param graph The graph to use
     * @param maxHops The maximum amount of hops
     *
     * @return The new graph.
     */
    private Graph getHopLimitedTree(final Graph graph, final int maxHops) {
        Set<Node> workingSet = new HashSet<Node>();
        Set<Edge> already = new HashSet<Edge>();
        getHopLimitedTreeRec(graph.getCentralNode(), workingSet, maxHops, already);
        return getNewGraph(graph, workingSet, maxHops);
    }

    /**
     * Recursive function to generate a add nodes to a working set for every
     * hop.
     *
     * The method adds the current node to the working set and then recursive
     * iterates over all edges of the node until the hop limit is reached.
     *
     * @param node The node
     * @param workingSet The working set to add the node to
     * @param maxHops The maximum amount of hops.
     */
    private void getHopLimitedTreeRec(final Node node,
            final Set<Node> workingSet, final int maxHops,
            final Set<Edge> already) {

        if (maxHops < 0 || workingSet.contains(node)) {
            return;
        }

        workingSet.add(node);
        for (Edge edge : node.getEdges()) {
            if (!already.contains(edge)) {
                getHopLimitedTreeRec(
                        edge.getDestinationNode(), workingSet,
                        maxHops - 1, already);
                already.add(edge);
            }
        }
    }

    /**
     * Hop from node to node and rewrite nodes from the central node
     * to the nodes within the hoprange.
     *
     * @param cur The current node
     * @param newGraph The new graph to add the edges to
     * @param oldGraph The old graph, needed as a lookup.
     * @param already A list of already visited nodes.
     * @param hops Maximum hops. Negative values means infinity.
     */
    private void peerGraphEdges(final Node cur, final Graph newGraph,
            final Graph oldGraph, final HashSet<Node> already, final int hops) {
        /* TODO: optimize */
        try {
            already.add(cur);

            Node oldNode = oldGraph.getNodeById(cur.getId());
            for (Edge oldEdge : oldNode.getEdges()) {
                Node otherOld;
                if (oldEdge.isOutgoingEdge(oldNode)) {
                    otherOld = oldEdge.getDestinationNode();
                } else {
                    otherOld = oldEdge.getSourceNode();
                }

                if (!newGraph.getNodeList().containsKey(otherOld.getId())) {
                    continue;
                }

                Node other = newGraph.getNodeById(otherOld.getId());
                Edge newEdge;
                if (oldEdge.isOutgoingEdge(oldNode)) {
                    newEdge = new Edge(cur, other);
                } else {
                    newEdge = new Edge(other, cur);
                }

                newGraph.addEdge(newEdge);
                if (!already.contains(other) && hops != 0) {
                    peerGraphEdges(other, newGraph, oldGraph,
                            already, hops - 1);
                }
            }
        } catch (Exception e) {
            log.info(e.toString());
        }
    }

    /**
     * Generat a new Graph.
     *
     * @param oldGraph The old Graph
     * @param workingSet Set of nodes which should be part of the new graph.
     * @param hops The amount of hops from the central nodes.
     *
     * @return The new Graph.
     */
    private Graph getNewGraph(final Graph oldGraph,
            final Collection<Node> workingSet, final int hops) {

        /* regenerate graph aka the tricky stuff */
        Graph newGraph = CalculatorUtil
            .getNewGraphOfNodes(oldGraph, workingSet);
        peerGraphEdges(newGraph.getCentralNode(), newGraph, oldGraph,
                new HashSet<Node>(), hops - 1);
        return newGraph;
    }
}
