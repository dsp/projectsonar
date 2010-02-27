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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.kit.ipd.sonar.server.AuthenticationResult;
import edu.kit.ipd.sonar.server.CalculationFailedException;
import edu.kit.ipd.sonar.server.DataException;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.NodeDoesNotExistException;
import edu.kit.ipd.sonar.server.NotAuthorizedException;
import edu.kit.ipd.sonar.server.TimeBoundary;
import edu.kit.ipd.sonar.server.User;
import edu.kit.ipd.sonar.server.centralities.Centrality;
import java.util.ArrayList;

/**
 * This interface defines the information the client can get from the server.
 */
@RemoteServiceRelativePath("rpc")
public interface RPCService extends RemoteService {

    /**
     * Get the available centralities from the server.
     *
     * The method does not reload the list. This has to be done by the sever
     * administartor.
     *
     * @throws NotAuthorizedException if the user is not authorized.
     *
     * @return An ArrayList containing the available centralities.
     */
    ArrayList<Centrality> getAvailableCentralities() throws
        NotAuthorizedException;

    /**
     * Get the time boundary over which the available data spans.
     *
     * The method returns the interval between the first created node and
     * the last created node or edge.
     *
     * @throws NotAuthorizedException if the user is not authorized.
     * @throws DataException If an error during data processing occurs.
     *
     * @return The maximal time boundary.
     */
    TimeBoundary getTimeBoundary() throws DataException, NotAuthorizedException;

    /**
     * Get the available users.
     *
     * @throws NotAuthorizedException if the user is not authorized.
     * @throws DataException If an error during data processing occurs.
     *
     * @return An ArrayList containing the users in the game.
     */
    ArrayList<User> getUserList() throws DataException, NotAuthorizedException;

    /**
     * Get the graph specified by the given values.
     *
     * The method calculates a graph without a central node.
     * This is called a global graph. If maxNodes is given only the given amount
     * of nodes are respected.
     *
     * At least one centrality needs to be a node centraltiy if a maxNodes value
     * is given, otherwise a CalculationFailedException will be thrown.
     *
     * Note that the user needs to be logged in and
     * needs admin rights to be able to use this method. Otherwise a
     * NotAuthorizedException will be thrown.
     *
     * @param timeBoundary  The time boundary for the graph.
     * @param centralities  The centralities to calculate.
     * @param maxNodes      The number of nodes in the graph.
     *
     * @throws CalculationFailedException If the calculation fails.
     * @throws NotAuthorizedException If the user is not authorized
     * @throws DataException If the database fails to retreive the data.
     *
     * @return  A graph as defined by the parameters.
     */
    Graph getGlobalGraph(TimeBoundary            timeBoundary,
                                ArrayList<Centrality>   centralities,
                                int                     maxNodes)
        throws CalculationFailedException, NotAuthorizedException,
        DataException;

    /**
     * Get the peer graph specified by the given values.
     *
     * The method calculates a graph with a central node. The central node is
     * the given user within the social network. If hops is provided only the
     * given amount of hops from the central node are respected during the
     * calculation and the resulting graph will not contain the other nodes.
     *
     * If the user is not an administrator he can only request a peer graph for
     * his own user. If the user tries to obtain a peer graph for another user a
     * NotAuthorizedException is thrown. The user has to be logged in.
     *
     * @param user          The user whose node will be the center node.
     * @param timeBoundary  The time boundary for the graph.
     * @param centralities  The centralities to calculate.
     * @param hops          The number of hops. Nodes that are more than
     *                          'hops' hops away from the central node
     *                          won't be included.
     *
     * @throws CalculationFailedException If the calculation fails.
     * @throws NodeDoesNotExistException If the given user doesn't exists.
     * @throws NotAuthorizedException If the user is not authorized
     * @throws DataException If the database fails to retreive the data.
     *
     * @return  A graph as defined by the parameters.
     */
    Graph getPeerGraph(User                  user,
                              TimeBoundary          timeBoundary,
                              ArrayList<Centrality> centralities,
                              int                   hops)
        throws CalculationFailedException, NodeDoesNotExistException,
        NotAuthorizedException, DataException;

    /**
     * Returns a hash of the database.
     *
     * This means that this function returns the same value exactly
     * until the database has changed.
     *
     * @throws DataException If an error during data processing occurs.
     * @return The hash int.
     */
    int getStateHash() throws DataException;

    /**
     * Tries to log in as a user.
     *
     * @param username  The username.
     * @param password  The password.
     *
     * @throws DataException If an error during data processing occurs.
     * @return An AuthenticationResult object representing the outcome of
     *         this call.
     */
    AuthenticationResult authenticateUser(String username,
            String password) throws DataException;

    /**
     * Tries to log in as admin.
     *
     * @param password The password.
     *
     * @return True if the authentication was successful, false otherwise.
     */
    boolean authenticateAdmin(String password);

    /**
     * Logs out.
     */
    void logout();
}
