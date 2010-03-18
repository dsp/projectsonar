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
 * Calculates a graph without a central node and optional limitations.
 *
 * @author David Soria Parra
 */
class GlobalCalculator extends Calculator {
    /**
     * Calculates a new graph with centralities from the given parameters.
     *
     * The method has to create and return a new graph.
     *
     * @param g The graph to use for calculation.
     * @param c A list centralities to use.
     * @param boundary An optional time boundary. Null if unused
     * @param limit An optional limit. Negative or zero if unused.
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

        errorif ("Graph is empty") {
            cur.getNodeList.size == 0
        }

        val next = if (lim > 0) {
            val limitedList = limited(cur, centralities, lim)
            graph(cur) {
                (a:Annotable) => a match {
                    case n: Node => limitedList contains n
                    case _ => true
                }
            }
        } else if (g == cur) {
            graph(cur)
        } else {
            cur
        }

        centralities.foreach(addCentralities(next, cur, _))
        next
    }

    private def errorif (msg: String)(pred: => Boolean) {
        if (pred) {
            throw new CalculationFailedException(msg)
        }
    }

    /**
     * Limit the graph with with the given centrality.
     *
     * @param graph The graph to be limited
     * @param c A list of centralities used to limit
     * @param limit The maximum amount of nodes
     *
     * @return Returns a new graph
     */
    private def limited (graph: Graph, c: Buffer[CentralityImpl], limit: Int) = {
        val vals = c.find(_.getType == Type.NodeCentrality) match {
            case Some(calc) => calc.getWeight(graph)
            case _ => throw new IllegalArgumentException("Need at least one"
                + "Node Centrality")
        }

        if (vals == null) {
            throw new CalculationFailedException("Centraltiy returned null")
        }

        /* we need to convert to doubleValue to get a scala double that we need
        for comparision as >= is not defined on java.lang.Double */
        vals.toList
            .sortWith((x, y) => x._2.doubleValue >= y._2.doubleValue)
            .slice(0, limit) /* limit the list */
            .map(_._1) /* map the actual node */
    }
}

// vim: set ts=4 sw=4 et:
