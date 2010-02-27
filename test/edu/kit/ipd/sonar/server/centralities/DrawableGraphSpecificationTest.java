/**
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
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

import java.util.List;
import java.util.ArrayList;

import com.google.gwt.junit.client.GWTTestCase;

import edu.kit.ipd.sonar.client.VisualizationMethod;
import edu.kit.ipd.sonar.client.rpc.DrawableGraphSpecification;
import edu.kit.ipd.sonar.client.rpc.GraphType;
import edu.kit.ipd.sonar.server.TimeBoundary;
import edu.kit.ipd.sonar.server.User;

public class DrawableGraphSpecificationTest extends GWTTestCase{

    @Override
    public String getModuleName() {
        return "edu.kit.ipd.sonar.Sonar";
    }

    public void testEquals() {
        Centrality cent1
                = new Centrality("CentralityName", 1,
                              Centrality.Type.NodeCentrality);
        Centrality cent2
                = new Centrality("CentralityName", 1,
                              Centrality.Type.NodeCentrality);

        List<Centrality> clist1 = new ArrayList<Centrality>();
        List<Centrality> clist2 = new ArrayList<Centrality>();
        clist1.add(cent1);
        clist2.add(cent2);
        List<VisualizationMethod> vlist1 = new ArrayList<VisualizationMethod>();
        List<VisualizationMethod> vlist2 = new ArrayList<VisualizationMethod>();
        vlist1.add(VisualizationMethod.DISTANCE);
        vlist2.add(VisualizationMethod.DISTANCE);
        DrawableGraphSpecification dgs1
                = new DrawableGraphSpecification(GraphType.PEER_GRAPH, 2,
                        new TimeBoundary(5,10), new User(5, "name"),
                        clist1, vlist1);
        DrawableGraphSpecification dgs2
                = new DrawableGraphSpecification(GraphType.PEER_GRAPH, 2,
                        new TimeBoundary(5,10), new User(5, "name"),
                        clist2, vlist2);
        DrawableGraphSpecification dgs3
                = new DrawableGraphSpecification(GraphType.PEER_GRAPH, 9,
                        new TimeBoundary(5,10), new User(5, "name"),
                        clist2, vlist2);
        assertTrue(dgs1.equals(dgs2));
        assertFalse(dgs1.equals(dgs3));
    }
}
