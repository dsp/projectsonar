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
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that contains shared methods among the calculator
 * implementations.
 *
 * The methods provided in the helper class can be used to add centralities to a
 * new graph or generate limited graphs. The methods are used by concrete
 * implementations of the Calculator interface.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
final class CalculatorUtil {
    /**
     * Defines a callback to filter annotable during the create graph methods.
     */
    interface AnnotableFilter {
        /**
         * Checks if the given annotable should be part of the new graph.
         *
         * The method checks if the given annotable should be included in
         * the new graph or not.
         *
         * @param a The annotable to check.
         *
         * @return True if the annotable should be included otherwise false.
         */
        boolean accept(Annotable a);
    }

    /**
     * The logger.
     */
    private static Logger log = LoggerFactory.getLogger(CalculatorUtil.class);

    /**
     * Private constructor as this is a utility class.
     */
    private CalculatorUtil() {
    }

    /**
     * Creates a new graph out of nodes from the given graph that lay within
     * the given time boundary.
     *
     * The method takes a old graph and time boundary and creates a new graph
     * out of it. The new graph only contains edges and nodes that are created
     * within the given time boundary.
     *
     * @param oldGraph The original graph
     * @param bound The time boundary
     *
     * @return A tuple of the mapping and the new graph
     */
    static Graph getTimeBoundedGraph(final Graph oldGraph,
            final TimeBoundary bound) {
        return getNewGraph(oldGraph, oldGraph.getNodeList().values(),
                new AnnotableFilter() {
                    public boolean accept(final Annotable a) {
                        return bound.inBoundary(a.getTime());
                    }
                });
    }

    /**
     * Returns a new graph from given set of nodes with all edges from the old
     * graph being copied to the new one.
     *
     * @param oldGraph The original graph
     * @param workingSet The working set
     *
     * @return The new graph.
     */
    static Graph getNewGraph(final Graph oldGraph,
            final Collection<Node> workingSet) {
        return getNewGraph(oldGraph, workingSet, null);
    }

    /**
     * Returns a new graph from given set of nodes with all edges from the old
     * graph being copied to the new one.
     *
     * @param oldGraph The original graph
     * @param workingSet The working set
     * @param af An optional filter, null if unused.
     *
     * @return The new graph.
     */
    static Graph getNewGraph(final Graph oldGraph,
            final Collection<Node> workingSet,
            final AnnotableFilter af) {
        Graph newGraph = getNewGraphOfNodes(oldGraph, workingSet, af);

        /* .. before we can continue with adding the edges */
        HashMap<Integer, Node> lookup = newGraph.getNodeList();
        for (Node oldNode : workingSet) {
            for (Edge oldEdge : oldNode.getEdges()) {
                /* use id's as we new nodes and old nodes have
                   different references, so equals won't work */
                int outgoingNodeIdx = oldEdge.getDestinationNode().getId();
                int incomingNodeIdx = oldEdge.getSourceNode().getId();
                if (lookup.containsKey(outgoingNodeIdx)
                        && lookup.containsKey(incomingNodeIdx)
                        && (af == null || af.accept(oldEdge))) {
                    Edge newEdge = new Edge(lookup.get(incomingNodeIdx),
                            lookup.get(outgoingNodeIdx),
                            oldEdge.getTime());
                    newGraph.addEdge(newEdge);
                 }
            }
        }

        return newGraph;
    }

    /**
     * Creates a new graph from the given working set without any edges.
     *
     * This method is used to generate a new graph that just contains copies
     * of the nodes provided in the working set. The method is useful to provide
     * a particular implementation how to add edges.
     *
     * @see PeerCalculator#getNewGraph(Graph oldGraph,
     * Collection workingSet, int hops)
     *
     * @param oldGraph The original graph
     * @param workingSet A set of nodes which should be added to the new graph
     *
     * @return A new graph.
     */
    static Graph getNewGraphOfNodes(final Graph oldGraph,
        final Collection<Node> workingSet) {
        return getNewGraphOfNodes(oldGraph, workingSet, null);
    }

    /**
     * Creates a new graph from the given working set without any edges.
     *
     * This method is used to generate a new graph that just contains copies
     * of the nodes provided in the working set. The method is useful to provide
     * a particular implementation how to add edges.
     *
     * @see PeerCalculator#getNewGraph(Graph oldGraph,
     * Collection workingSet, int hops)
     *
     * @param oldGraph The original graph
     * @param workingSet A set of nodes which should be added to the new graph
     * @param af An optional filter, null if unused.
     *
     * @return A new graph.
     */
    static Graph getNewGraphOfNodes(final Graph oldGraph,
        final Collection<Node> workingSet, final AnnotableFilter af) {

        /* regenerate graph aka the tricky stuff */
        Graph newGraph = new Graph();

        /* we have to make sure that all nodes are added .. */
        for (Node oldNode : workingSet) {
            if (af == null || af.accept(oldNode)) {
                Node newNode = oldNode.getCleanCopy();
                newGraph.addNode(newNode);
            }
        }

        try {
            /* make sure we use the new graph otherwise we'll copy
             * old edges */
            if (null != oldGraph.getCentralNode()) {
                newGraph.setCentralNode(
                        newGraph.getNodeById(
                            oldGraph.getCentralNode().getId()));
            }
        } catch (NodeDoesNotExistException nde) {
            log.error("requested node does not exists " + nde.toString());
        }

        return newGraph;
    }

    /**
     * Calculates the given centrality value for the given centrality
     * and add them to the graph.
     *
     * The old graph refers to the old graph that was used to create
     * the new from and needs to be a superset of the new graph.
     *
     * @param newGraph The graph to add the values too
     * @param oldGraph The original graph.
     * @param cr The centrality
     * @throws CalculationFailedException if the calculation failed
     */
    static void addCentralities(final Graph newGraph, final Graph oldGraph,
            final CentralityImpl cr) throws CalculationFailedException {
        Collection<? extends Annotable> annotables = null;
        HashMap<? extends Annotable, Double> centralityValues = null;
        switch (cr.getType()) {
            case NodeCentrality:
                annotables = newGraph.getNodeList().values();
                break;
            case EdgeCentrality:
                annotables = newGraph.getEdgeList();
                break;
            default:
                throw new CalculationFailedException(
                    "Unexepected centrality type");
        }

        centralityValues = cr.getWeight(oldGraph);

        if (null == centralityValues) {
            throw new CalculationFailedException(
                "centrality returned nothing");
        }
        for (Annotable a : annotables) {
            if (!centralityValues.containsKey(a)) {
                throw new CalculationFailedException("Returned key, values "
                        + "from the " + cr.getName() + " centrality plugins do "
                        + "not contain the requested annotable object");
            }

            a.addWeight(cr.getCentrality(), centralityValues.get(a));
        }
    }
}
