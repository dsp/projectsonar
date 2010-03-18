package edu.kit.ipd.sonar.server

import edu.kit.ipd.sonar.server.centralities.CentralityImpl
import org.scalatest.junit.JUnitSuite
import org.junit.Assert._
import org.junit.Test

class CalculatorTest extends JUnitSuite {
    @Test def testGraph() {
        val g = TestUtil.getGraphMock
        val t = TestUtil.getCalculator
        val b = new TimeBoundary(0, 3)
        val ng = t.bounded(g, b)

        assert(ng.getNodeList.containsKey(5) === false)
        assert(ng.getEdgeList.size === 3)
    }

    @Test def testAddCentralities() {
        val t = TestUtil.getCalculator
        val g = TestUtil.getGraphMock
        val c = TestUtil.getNodeCentrality

        /* the node centrality from the test util calculates the value
           of the node with 1/nodeid. */
        t.addCentralities(g, g, c)
        assert(g.getNodeById(1).getWeightForCentrality(c) === 1.0)
        assert(g.getNodeById(2).getWeightForCentrality(c) === 1.0/2.0)
        assert(g.getNodeById(3).getWeightForCentrality(c) === 1.0/3.0)
    }
}

// vim: set ts=4 sw=4 et:
