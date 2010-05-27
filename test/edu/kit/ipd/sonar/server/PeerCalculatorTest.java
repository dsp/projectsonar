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

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests for the GlobalCalculator implementation
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class PeerCalculatorTest {
    @Test
    public void testCalc() throws CalculationFailedException {
        Graph oldGraph;
        Calculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(TestUtil.getNodeCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new PeerCalculator();

        Graph newGraph = calculator.calc(oldGraph, centralities, null, 1, oldGraph.getCentralNode());
        assertNotNull(newGraph);
        assertNotNull(newGraph.getCentralities());
        assertEquals(3, newGraph.getNodeList().values().size());
        assertEquals(1, newGraph.getCentralities().size());
        assertFalse(oldGraph == newGraph);
    }

    @Test
    public void testCalcBounded() throws CalculationFailedException {
        Graph oldGraph;
        Calculator calculator;
        ArrayList<CentralityImpl> centralities = new ArrayList<CentralityImpl>();

        centralities.add(TestUtil.getNodeCentrality());

        oldGraph = TestUtil.getGraphMock();
        calculator = new PeerCalculator();

        Graph newGraph = calculator.calc(oldGraph, centralities, new TimeBoundary(2, 3), 1, oldGraph.getCentralNode());
        assertNotNull(newGraph);
        assertNotNull(newGraph.getCentralities());

        assertFalse(newGraph.getNodeList().containsKey(4));
        assertEquals(2, newGraph.getNodeList().values().size());
        assertEquals(1, newGraph.getCentralities().size());
        assertFalse(oldGraph == newGraph);

    }
}
