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
package edu.kit.ipd.sonar.client.rpc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import edu.kit.ipd.sonar.server.TimeBoundary;
import edu.kit.ipd.sonar.server.User;
import edu.kit.ipd.sonar.server.centralities.BetweennessCentrality;
import edu.kit.ipd.sonar.server.centralities.Centrality;
import edu.kit.ipd.sonar.server.centralities.IndegreeCentrality;


/**
 * Unittests for the GraphSpecification.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class GraphSpecificationTest {

    /**
     * Tests the correct of a PeerGraphRequest.
     */
    @Test
    public void testCorrectPeerGraphCalls() {

        //Test argument handover of a legal PeerGraphRequest.
        TimeBoundary tb = new TimeBoundary(10, 20);
        User user = new User(11, "Bill Gates");
        List<Centrality> cents = new ArrayList<Centrality>();
        cents.add(new BetweennessCentrality());
        cents.add(new IndegreeCentrality());
        GraphSpecification gs = new GraphSpecification(
                    GraphType.PEER_GRAPH , 42, tb, user, cents);

        assertEquals(GraphType.PEER_GRAPH, gs.getRequestType());
        assertEquals(42, gs.getCutoff());
        assertEquals(tb, gs.getTimeBoundary());
        assertEquals(user, gs.getUser());
        assertEquals(cents, gs.getCentralities());
    }

    /**
     * Tests the correct of a GlobalGraphRequest.
     */
    @Test
    public void testCorrectGlobalGraphCalls() {

        //Test argument handover of a legal GlobalGraphRequest.
        TimeBoundary tb = new TimeBoundary(10, 20);
        List<Centrality> cents = new ArrayList<Centrality>();
        cents.add(new BetweennessCentrality());
        cents.add(new IndegreeCentrality());
        GraphSpecification gs = new GraphSpecification(
                    GraphType.GLOBAL_GRAPH , 42, tb, null, cents);

        assertEquals(GraphType.GLOBAL_GRAPH, gs.getRequestType());
        assertEquals(42, gs.getCutoff());
        assertEquals(tb, gs.getTimeBoundary());
        assertNull(gs.getUser());
        assertEquals(cents, gs.getCentralities());
    }

    /**
     * Tests the incorrect argument combination for peer and global graphs.
     */
    @Test
    public void testIncorrectUserToGraphTypeRelation() {

        TimeBoundary tb = new TimeBoundary(10, 20);
        User user = new User(11, "Bill Gates");
        List<Centrality> cents = new ArrayList<Centrality>();
        cents.add(new BetweennessCentrality());
        cents.add(new IndegreeCentrality());

        try {
            GraphSpecification gs = new GraphSpecification(
                    GraphType.PEER_GRAPH , 42, tb, null, cents);
            fail("No exception when requesting a peer graph with a null-user");
        } catch (IllegalArgumentException e) {}

        try {
            GraphSpecification gs = new GraphSpecification(
                    GraphType.GLOBAL_GRAPH , 42, tb, user, cents);
            fail("No exception when requesting"
                 + " a global graph without a null-user");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Tests if the proper exceptions are thrown
     * for illegal constructor arguments.
     */
    @Test
    public void testIllegalConstructorArguments() {

        TimeBoundary tb = new TimeBoundary(10, 20);
        User user = new User(11, "Bill Gates");
        List<Centrality> cents = new ArrayList<Centrality>();
        cents.add(new BetweennessCentrality());
        cents.add(new IndegreeCentrality());

        try {
            GraphSpecification gs = new GraphSpecification(
                    GraphType.PEER_GRAPH , 42, null, user, cents);
            fail("No exception when using a null-timeboundary");
        } catch (IllegalArgumentException e) {}

        try {
            GraphSpecification gs = new GraphSpecification(
                    GraphType.PEER_GRAPH , 42, tb, user, null);
            fail("No exception when using a  null-centrality-list");
        } catch (IllegalArgumentException e) {}

        try {
            GraphSpecification gs = new GraphSpecification(
                    GraphType.PEER_GRAPH , 42, tb,
                    user, new ArrayList<Centrality>());
            fail("No exception when using an empty centrality-list");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Tests if the equals - and hashcode-stuff works right. part 1.
     */
    @Test
    public void testEquals1() {

        TimeBoundary tb1 = new TimeBoundary(10, 20);
        User user1 = new User(11, "Bill Gates");
        List<Centrality> cents1 = new ArrayList<Centrality>();
        cents1.add(new BetweennessCentrality());
        cents1.add(new IndegreeCentrality());
        GraphSpecification gs1 = new GraphSpecification(
                GraphType.PEER_GRAPH, 20, tb1, user1, cents1);

        TimeBoundary tb2 = new TimeBoundary(10, 20);
        User user2 = new User(11, "Bill Gates");
        List<Centrality> cents2 = new ArrayList<Centrality>();
        cents2.add(new BetweennessCentrality());
        cents2.add(new IndegreeCentrality());
        GraphSpecification gs2 = new GraphSpecification(
                GraphType.PEER_GRAPH, 20, tb2, user2, cents2);

        assertEquals(gs1, gs2);
        assertEquals(gs1.hashCode(), gs2.hashCode());

        //check identity:
        assertEquals(gs1, gs1);

        //check different kind of class:
        assertFalse(gs1.equals(new Object()));

    }

    /**
     * Tests if the equals - and hashcode-stuff works right. part 2.
     */
    @Test
    public void testEquals2() {

        TimeBoundary tb1 = new TimeBoundary(10, 20);
        User user1 = new User(11, "Bill Gates");
        List<Centrality> cents1 = new ArrayList<Centrality>();
        cents1.add(new BetweennessCentrality());
        cents1.add(new IndegreeCentrality());
        GraphSpecification gs1 = new GraphSpecification(
                GraphType.PEER_GRAPH, 20, tb1, user1, cents1);

        TimeBoundary tb2 = new TimeBoundary(10, 20);
        List<Centrality> cents2 = new ArrayList<Centrality>();
        cents2.add(new BetweennessCentrality());
        cents2.add(new IndegreeCentrality());
        GraphSpecification gs2 = new GraphSpecification(
                GraphType.GLOBAL_GRAPH, 20, tb2, null, cents2);

        assertFalse(gs1.equals(gs2));
        assertFalse(gs1.hashCode() == gs2.hashCode());
    }

    /**
     * Tests if the equals - and hashcode-stuff works right. part 3.
     */
    @Test
    public void testEquals3() {

        TimeBoundary tb1 = new TimeBoundary(10, 20);
        List<Centrality> cents1 = new ArrayList<Centrality>();
        cents1.add(new BetweennessCentrality());
        cents1.add(new IndegreeCentrality());
        GraphSpecification gs1 = new GraphSpecification(
                GraphType.GLOBAL_GRAPH, 20, tb1, null, cents1);

        TimeBoundary tb2 = new TimeBoundary(10, 20);
        List<Centrality> cents2 = new ArrayList<Centrality>();
        cents2.add(new BetweennessCentrality());
        cents2.add(new IndegreeCentrality());
        GraphSpecification gs2 = new GraphSpecification(
                GraphType.GLOBAL_GRAPH, 21, tb2, null, cents2);

        assertFalse(gs1.equals(gs2));
        assertFalse(gs1.hashCode() == gs2.hashCode());
    }
}
