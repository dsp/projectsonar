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
package edu.kit.ipd.sonar.server.centralities;

import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;


import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.TestUtil;

/**
 * Tests the EdgeWeightCentrality - class.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class EdgeWeightCentralityTest {

    /** The delta-value from which double-values are considered equal.*/
    private static final double DOUBLE_DELTA = 0.001;

    /**
     * Tests if the EdgeWeight-centrality behaves right when
     * receiving an empty graph.
     */
    @Test
    public void testEmptyGraph() {

        EdgeWeightCentrality ewc = new EdgeWeightCentrality();
        Graph graph = TestUtil.getEmtpyGraph();

        HashMap<Edge , Double> result = ewc.getWeight(graph);

        assertTrue(result.isEmpty());
    }

    /**
     * Tests if the EdgeWeight-centrality calculates its values
     * correctly for a simple weighted graph.
     */
    @Test
    public void testOriginalWeightedGraph() {

        EdgeWeightCentrality ewc = new EdgeWeightCentrality();
        Graph graph = TestUtil.getOriginalWeightedGraph();

        Edge[] edges = graph.getEdgeList().toArray(new Edge[0]);
        HashMap<Edge, Double> result = ewc.getWeight(graph);

        for (Edge e : edges) {
            assertEquals(e.getOriginalWeight(), result.get(e), DOUBLE_DELTA);
        }
    }

    /**
     * Tests if the EdgeWeight-centrality calculates its values
     * correctly for a simple unweighted graph.
     */
    @Test
    public void testNonWeightedGraph() {

        EdgeWeightCentrality ewc = new EdgeWeightCentrality();
        Graph graph = TestUtil.getGraphMock();

        Edge[] edges = graph.getEdgeList().toArray(new Edge[0]);
        HashMap<Edge, Double> result = ewc.getWeight(graph);

        for (Edge e : edges) {
            assertEquals(0.0, result.get(e), DOUBLE_DELTA);
        }
    }

    /**
     * Tests if there is an IllegalArgumentException if the graph passed as
     * an Argument was null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        EdgeWeightCentrality ewc = new EdgeWeightCentrality();
        ewc.getWeight(null);
    }
}
