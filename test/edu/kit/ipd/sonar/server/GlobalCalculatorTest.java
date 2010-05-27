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
import edu.kit.ipd.sonar.server.centralities.EdgeBetweennessCentrality;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests for the GlobalCalculator implementation
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class GlobalCalculatorTest {
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyGraphCalc() throws CalculationFailedException {
        GlobalCalculator calculator = new GlobalCalculator();

        calculator.calc(null, null, null, 0, null);
    }


    @Test
    public void testEmptyCentralities() throws CalculationFailedException {
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();
        Graph oldGraph = TestUtil.getGraphMock();
        GlobalCalculator calculator = new GlobalCalculator();

        Graph newGraph = calculator.calc(oldGraph, centralities, null, 0, null);

        assertEquals(oldGraph, newGraph);
    }

    @Test
    public void testCalc() throws CalculationFailedException {
        Graph oldGraph;
        GlobalCalculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(TestUtil.getNodeCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new GlobalCalculator();

        Graph newGraph = calculator.calc(oldGraph, centralities, null, -1, null);
        assertNotNull(newGraph);
        assertNotNull(newGraph.getCentralities());
        assertEquals(1, newGraph.getCentralities().size());
        assertFalse(oldGraph == newGraph);
    }

    @Test
    public void testCalcLimited() throws CalculationFailedException {
        Graph oldGraph;
        GlobalCalculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(TestUtil.getNodeCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new GlobalCalculator();

        Graph newGraph = calculator.calc(oldGraph, centralities, null, 3, null);
        assertNotNull(newGraph);
        assertFalse(oldGraph == newGraph);
        assertEquals(3, newGraph.getNodeList().values().size());

        TimeBoundary oldGraphBound = oldGraph.getMaxTimeBoundary();
        TimeBoundary newGraphBound = newGraph.getMaxTimeBoundary();
        assertEquals(oldGraphBound.getStart(), newGraphBound.getStart());
        assertEquals(3, newGraphBound.getEnd());
    }

    @Test
    public void testCalcBounded() throws CalculationFailedException {
        Graph oldGraph;
        GlobalCalculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(TestUtil.getNodeCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new GlobalCalculator();

        Graph newGraph = calculator.calc(oldGraph, centralities, new TimeBoundary(2, 4), -1, null);
        assertNotNull(newGraph);
        assertFalse(oldGraph == newGraph);
        assertEquals(3, newGraph.getNodeList().values().size());
        
        TimeBoundary oldGraphBound = oldGraph.getMaxTimeBoundary();
        TimeBoundary newGraphBound = newGraph.getMaxTimeBoundary();
        assertEquals(1, oldGraphBound.getStart());
        assertEquals(5, oldGraphBound.getEnd());
        assertEquals(2, newGraphBound.getStart());
        assertEquals(4, newGraphBound.getEnd());
        assertEquals(3, newGraph.getNodeList().size());
        assertEquals(2, newGraph.getEdgeList().size());
    }

    @Test
    public void testEqualCentralityValues() throws CalculationFailedException {
        Graph oldGraph;
        GlobalCalculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(TestUtil.getEqualCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new GlobalCalculator();

        Graph newGraph = calculator.calc(oldGraph, centralities, null, -1, null);
        assertEquals(oldGraph.getNodeList().size(), newGraph.getNodeList().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPassNoNodeCentrality() throws CalculationFailedException {
        Graph oldGraph;
        GlobalCalculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(new EdgeBetweennessCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new GlobalCalculator();

        /* should fail we need to have at least one node centrality for
           limited graphs */
        calculator.calc(oldGraph, centralities, null, 1, null);
    }

    @Test(expected=CalculationFailedException.class)
    public void testCentralityReturnEmptyMap() throws CalculationFailedException {
        Graph oldGraph;
        GlobalCalculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(TestUtil.getEmptyCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new GlobalCalculator();

        /* should fail we need to have at least one node centrality for
           limited graphs */
        calculator.calc(oldGraph, centralities, null, 1, null);

        centralities = new ArrayList<CentralityImpl>();
        centralities.add(TestUtil.getNullCentrality());
        calculator.calc(oldGraph, centralities, null, 1, null);
    }

    @Test
    public void testGetMoreNodesThanPossible() throws CalculationFailedException {
        Graph oldGraph;
        GlobalCalculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(TestUtil.getNodeCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new GlobalCalculator();

        /* should fail we need to have at least one node centrality for
           limited graphs */
        Graph newGraph = calculator.calc(oldGraph, centralities, null, 100, null);

        assertEquals(newGraph.getNodeList().size(),
                oldGraph.getNodeList().size());
        for (Node n : newGraph.getNodeList().values()) {
            assertTrue(oldGraph.getNodeList().containsValue(n));
        }
        /* assertEquals(newGraph.hashCode(), oldGraph.hashCode()); */
    }
}
