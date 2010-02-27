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

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.kit.ipd.sonar.server.TimeBoundary;
import edu.kit.ipd.sonar.server.AuthenticationResult;
import edu.kit.ipd.sonar.server.centralities.Centrality;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.User;
import java.util.ArrayList;

/**
 * This interface provides asynchronous versions of the
 * functions defined in RPCService.
 */
public interface RPCServiceAsync {

    /**
     * Get the available centralities from the server.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void getAvailableCentralities(
            AsyncCallback<ArrayList<Centrality>> callback);

    /**
     * Get the time boundary over which the available data spans.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void getTimeBoundary(AsyncCallback<TimeBoundary> callback);

    /**
     * Get the available users.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void getUserList(AsyncCallback<ArrayList<User>> callback);

    /**
     * Get the graph specified by the given values.
     *
     * @param timeBoundary  The time boundary for the graph.
     * @param centralities  The centralities to calculate.
     * @param maxNodes      The number of nodes in the graph.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void getGlobalGraph(TimeBoundary timeBoundary,
            ArrayList<Centrality> centralities, int maxNodes,
            AsyncCallback<Graph> callback);

    /**
     * Get the peer graph specified by the given values.
     *
     * @param user          The user whose node will be the center node.
     * @param timeBoundary  The time boundary for the graph.
     * @param centralities  The centralities to calculate.
     * @param hops          The number of hops. Nodes that are more than
     *                          'hops' hops away from the central node
     *                          won't be included.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void getPeerGraph(User user, TimeBoundary timeBoundary,
            ArrayList<Centrality> centralities, int hops,
            AsyncCallback<Graph> callback);

    /**
     * Returns a hash of the database.
     *
     * This means that this function returns the same value exactly
     * until the database has changed.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void getStateHash(AsyncCallback<Integer> callback);

    /**
     * Tries to log in as a user.
     *
     * @param username  The username.
     * @param password  The password.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void authenticateUser(String username, String password,
            AsyncCallback<AuthenticationResult> callback);

    /**
     * Tries to log in as admin.
     *
     * @param password The password.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void authenticateAdmin(String password, AsyncCallback<Boolean> callback);

    /**
     * Logs out.
     *
     * @param callback  An AsyncCallback that gets called when the data is
     *                  available.
     */
    void logout(AsyncCallback<Void> callback);
}
