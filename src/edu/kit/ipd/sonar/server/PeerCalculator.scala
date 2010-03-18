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
package edu.kit.ipd.sonar.server

import edu.kit.ipd.sonar.server.centralities.{Centrality, CentralityImpl}
import edu.kit.ipd.sonar.server.centralities.Centrality.Type

import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer
import java.util.ArrayList

/*
 * Calculates a graph with a central node and optional limitations.
 *
 * @author David Soria Parra
 */
class PeerCalculator extends Calculator {
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
    @throws(classOf[CalculationFailedException])
    def calc (g: Graph, centralities: ArrayList[CentralityImpl],
        bound: TimeBoundary, limit: java.lang.Integer): Graph = {

        /* needed by scala as limit doesn't have functions */
        val lim = limit.intValue

        if (null == g) {
            throw new IllegalArgumentException("Passed graph is null");
        }

        if (null == centralities) {
            throw new IllegalArgumentException("Passed centralities are null");
        }

        if (0 == centralities.size) {
            return g
        }

        /* bound the graph if necessary */
        val cur = if (null != bound && !bound.contains(g.getMaxTimeBoundary)) {
            bounded(g, bound)
        } else {
            g
        }

        if (0 == cur.getNodeList.size) { 
            throw new CalculationFailedException("Graph is empty");
        }

        if (null == cur.getCentralNode) {
            throw new CalculationFailedException("Graph has no central node");
        }

        val next = if (lim > 0) {
            val limitedList = limited(cur, lim)
            graph(cur) {
                (a:Annotable) => a match {
                    case n: Node => limitedList contains n
                    case _ => true
                }
            }
        } else if (cur == g){
            graph(cur)
        } else {
            cur
        }

        centralities.foreach(addCentralities(next, cur, _))
        next
    }

    private def limited(graph: Graph, limit: Int) = {
        var list: List[Node] = List()
        def recur(cur: Node, limit: Int) {
            cur.getEdges
               .withFilter(_.isOutgoingEdge(cur))
               .foreach((e: Edge) => {
                   list = e.getDestinationNode :: list
                   if (limit > 0)
                       recur(e.getDestinationNode, limit - 1)
               })
        }

        val node = (n: Node) => graph.getNodeById(n.getId)

        recur(graph.getCentralNode, limit - 1)
        list
    }
}

// vim: set ts=4 sw=4 et:
