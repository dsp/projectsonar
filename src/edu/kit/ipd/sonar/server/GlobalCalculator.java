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

import edu.kit.ipd.sonar.server.centralities.Centrality;
import edu.kit.ipd.sonar.server.centralities.CentralityImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Calculates the centralities for the a global graph.
 *
 * The term Global Graph in Sonar refers to a standard graph with multiple edges
 * between nodes. A global graph has calculated weights for a given set of
 * centraltiies for nodes or edges.
 *
 * The global calculator creates a copy of the given original graph. The copy
 * contains nodes and edges which creation time lays in the given boundary.
 * It is possible to only create just a subgraph of the original graph that
 * contains the highest valued 'maxNodes' amount of nodes.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
class GlobalCalculator implements Calculator {
    /**
     * Logger for error logging.
     */
    private static Logger log = LoggerFactory.getLogger(GlobalCalculator.class);

    /**
     * Inner class to sort annotable and their weights.
     *
     * We cannot use the usual hashset to sort the values,
     * as they can contain equal values.
     */
    class CentralityPair {
        /**
         * The annotable entry.
         */
        private Annotable entry;

        /**
         * The weight for the annotable entry.
         */
        private Double weight;

        /**
         * Initialize a new centrality pair with the given entry and weight.
         *
         * @param entry The annotable entry.
         * @param weight The weight for the annotable entry.
         */
        public CentralityPair(final Annotable entry, final Double weight) {
            this.weight = weight;
            this.entry = entry;
        }

        /**
         * Returns the weight for the annotable entry.
         *
         * @return The weight.
         */
        public Double getWeight() {
            return weight;
        }

        /**
         * Returns the annotable instance.
         *
         * @return The annotable.
         */
        public Annotable getEntry() {
            return entry;
        }
    }

    /**
     * Calculates the values for the requested centralities on the given graph
     * and returns a new graph.
     *
     * @see Calculator#calc(Graph g, ArrayList c,
     *          TimeBoundary boundary, int limit)
     * @param g The graph to use for calculation.
     * @param c A list centralities to use.
     * @param boundary An optional time boundary. Null if unused
     * @param limit An optional limit. Negative or zero if unused.
     * @throws CalculationFailedException if the calculation fails somehow
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

        try {
            /* reset the central node, we do not have a central node.
             * this operation is safe to do on the original graph */
            g.setCentralNode(null);
        } catch (NodeDoesNotExistException nde) {
            log.warn("failed to reset central node");
        }

        Graph ng = g;
        if (boundary != null && !boundary.contains(g.getMaxTimeBoundary())) {
            log.debug("use time boundary");
            ng = CalculatorUtil.getTimeBoundedGraph(g, boundary);
        }

        if (ng.getNodeList().size() == 0) {
            throw new CalculationFailedException("Graph is empty");
        }

        Graph newGraph;
        if (limit > 0) {
            CentralityImpl calculationCentrality = null;

            /* require at least one node centrality if limitation is needed */
            for (CentralityImpl centrality  : c) {
                if (calculationCentrality == null
                    && centrality.getType() == Centrality.Type.NodeCentrality) {
                    calculationCentrality = centrality;
                }
            }

            if (null == calculationCentrality) {
                throw new IllegalArgumentException("Need at least one "
                    + "NodeCentrality");
            }

            HashMap<? extends Annotable, Double> map
                = calculationCentrality.getWeight(ng);

            if (null == map || map.isEmpty()) {
                /* aehm.... */
                throw new CalculationFailedException("Centrality plugin "
                        + "returned an empty map");
            }

            newGraph = getLimitedTree(ng, map, limit);
        } else {
            newGraph = CalculatorUtil.getNewGraph(ng,
                    ng.getNodeList().values());
        }

        /* TODO: don't trigger a new calculation in centralities
         * if we can avoid it */
        for (CentralityImpl centrality : c) {
            CalculatorUtil.addCentralities(newGraph, ng, centrality);
        }

        return newGraph;
    }

    /**
     * Returns a comparator suitable for the CentralityPair comparisions.
     *
     * We need our own comparator that knows the semantic of the CentralityPair
     * class.
     *
     * @return The comparator.
     */
    private static Comparator<CentralityPair> getComparator() {
        return new Comparator<CentralityPair>() {
            /**
             * Compare centrality paris by weight.
             *
             * @param o1 The first pair
             * @param o2 The second pair
             *
             * @return 0 if equals, -1 if o1 is smaller otherwise 1.
             */
            public int compare(final CentralityPair o1,
                    final CentralityPair o2) {
                /* we intentionally sort in reverse order */
                return o2.getWeight().compareTo(o1.getWeight());
            }

            /**
             * Check if the objects are equals.
             *
             * This is never true for our comparision as we
             * do not to check for equality.
             */
            public boolean equals(final Object obj) {
                return false;
            }

            /**
             * Hashcode implementation.
             *
             * Checkstyle needs this.
             */
            public int hashCode() {
                return super.hashCode();
            }
        };
    }

    /**
     * Creates a duplicated graph based on the given centrality to value mapping
     * and the given limit of nodes.
     *
     * The method returns a tuple of the new graph and a map Annotable,Annotable
     * that maps the new nodes to the old ones for further calculation.
     *
     * @param g The Graph to duplicated.
     * @param map The node->centralityValue store.
     * @param maxNodes the maximum count of nodes that should be used.
     *
     * @return A tuple of the mapping and the new graph.
     */
    private Graph getLimitedTree(final Graph g,
            final HashMap<? extends Annotable, Double> map,
            final int maxNodes) {
        LinkedList<CentralityPair> sorting;

        sorting = new LinkedList<CentralityPair>();
        for (Map.Entry<? extends Annotable, Double> e : map.entrySet()) {
            sorting.add(new CentralityPair(e.getKey(), e.getValue()));
        }

        if (sorting.size() > maxNodes && maxNodes > 0) {
            Collections.sort(sorting, getComparator());
            return CalculatorUtil.getNewGraph(g,
                    getNodesFromCentralityPairs(sorting.subList(0, maxNodes)));
        } else {
            return CalculatorUtil.getNewGraph(g,
                    getNodesFromCentralityPairs(sorting));
        }
    }

    /**
     * Returns a collection of nodes from a collection of CentralityPairs.
     *
     * We need to get the nodes out of the CentralityPairs.
     *
     * @param list Collection of centrality pairs.
     *
     * @return A new list of nodes.
     */
    private Collection<Node> getNodesFromCentralityPairs(
        final Collection<CentralityPair> list) {
        LinkedList<Node> result = new LinkedList<Node>();
        for (CentralityPair p : list) {
            if (p.getEntry() instanceof Node) {
                result.add((Node) p.getEntry());
            }
        }
        return result;
    }
}
