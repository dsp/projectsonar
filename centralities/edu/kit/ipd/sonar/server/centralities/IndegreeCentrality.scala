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

package edu.kit.ipd.sonar.server.centralities

import edu.kit.ipd.sonar.server._

import scala.collection.JavaConversions._
import scala.collection.mutable.Map
import java.util.{HashMap => JavaHashMap}

/**
 * Indegree calculations done in Scala.
 *
 * @author David Soria Parra <david.parra@student.kit..edu>
 */
class IndegreeCentrality extends CentralityImpl {
    def getWeight(g : edu.kit.ipd.sonar.server.Graph) : JavaHashMap[Annotable, java.lang.Double]= {
        if (g == null)
            throw new IllegalArgumentException("Graph is null")
        val res = new JavaHashMap[Annotable, java.lang.Double]()
        g.getNodeList.values.foreach(
            (n : Node) => res.put(n, g.getEdgeList().filter(
                (e : Edge) => e.isIncomingEdge(n)).size))
        res 
    }

    override def getType = Centrality.Type.NodeCentrality
    override def getRequiredAPIVersion = 0
    override def getVersion = 0
    override def getName = "Indegree Scala"
}

// vim: set ts=4 sw=4 et:
