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
import java.util.ArrayList
import java.util.HashMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Interface for the calculation of centrality values of a graph.
 *
 * <p>
 * A calculator is used to create new limited graphs from existing graphs and
 * calculate the values for a given set of centralities on the graph.
 * </p><p>
 * To generate new graphs a calculator has to copy the original nodes. Passing
 * by reference is not valid. A calculator implementation has to make sure that
 * the original graph is not changed so that it can be reused by a new
 * calculator.
 * </p><p>
 * In the current implementation of Sonar all calculators use the
 * same instance of a graph to generate new graphs from it. This approach is
 * used to minimize memory footage of the application.
 * </p><p>
 * The class offers methods to generate graphs. These methods should be used
 * to play well with the Caching trait that overwrites these methods.
 * </p>
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
abstract class Calculator {

    protected val log = LoggerFactory.getLogger(getClass)

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
    def calc(graph: Graph, centralities: ArrayList[CentralityImpl],
        bound: TimeBoundary, limit: java.lang.Integer): Graph

    /**
     * Generate a time bounded graph.
     *
     * @param oldGraph The graph to bound
     * @param bound    The boundary
     * @return The bounded graph
     */
    def bounded(oldGraph: Graph, bound: TimeBoundary) = {
        graph (oldGraph) {
            (a:Annotable) => bound.inBoundary(a.getTime)
        }
    }

    /* default closure for graph(g) */
    implicit val accept = (a: Annotable) => true

    /**
     * Generate a new graph from an old graph with additional filtering.
     *
     * @param graph The graph to duplicate
     * @param accept The filter method
     * @return The duplicated graph
     */
    def graph(graph: Graph)(implicit accept: Annotable => Boolean) = {
        log debug "generate new graph"

        val newgraph = new Graph()
        val map = graph.getNodeList
        map.values.withFilter(accept).foreach(
            (n:Node) => newgraph.addNode(n.getCleanCopy))

        log debug newgraph.getNodeList.toString

        val lookup = (n:Node) => newgraph.getNodeList.containsKey(n.getId)
        val get    = (n:Node) => newgraph.getNodeById(n.getId)

        graph.getEdgeList.withFilter(accept).foreach((e:Edge) => {
            val src  = e.getSourceNode
            val dest = e.getDestinationNode
            if (lookup(dest) && lookup(src)) {
                newgraph.addEdge(new Edge(get(src), get(dest), e.getTime))
            }
        })

        log debug newgraph.getEdgeList.toString

        if (graph.getCentralNode != null && lookup(graph.getCentralNode)) {
            newgraph.setCentralNode(get(graph.getCentralNode))
        }
        newgraph
    }

    private def annotables(graph: Graph, c: Centrality) = c.getType match {
        case Type.NodeCentrality => graph.getNodeList.values
        case Type.EdgeCentrality => graph.getEdgeList
    }

    /**
     * Add centralities to a graph.
     *
     * Calculates the centrality values based on the old graph and adds the
     * items to the new graph.
     *
     * @param graph    The graph to add the centrality values to
     * @param oldGraph The graph to be used to calculates values on it
     * @param c        The centrality impl to be used to calc the values.
     * @return The graph with the centralities added
     */
    def addCentralities(graph: Graph, oldGraph: Graph, c: CentralityImpl): Unit = {
        val vals = c.getWeight(oldGraph)
        annotables(graph, c).foreach((a: Annotable) => {
            if (!vals.containsKey(a))
                throw new CalculationFailedException("Returned " + a + ", values "
                    + "from the centrality plugins do "
                    + "not contain the requested annotable object")
            a.addWeight(c.getCentrality, vals.get(a))
        })
    }
}

// vim: set ts=4 sw=4 et:
