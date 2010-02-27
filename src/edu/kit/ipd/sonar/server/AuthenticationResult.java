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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents the answer to an user authentication attempt.
 *
 * A client that makes and authenticateUser call to the rpc service will
 * get an authentification result.
 *
 * The class is primarly used as a transfer structure between server and
 * client. It is only used if the client tries to authenticate a user.
 * Administration logins are handled differently due as Sonar is not backed by a
 * access right database.
 *
 * Objects of this class are immutable.
 *
 * @author Kevin-Simon Kohlmeyer
 */
public class AuthenticationResult implements IsSerializable {

    /** The user that logged in, null if isSuccessful is false. */
    private User user;

    /** True if the login attempt was successful. */
    private boolean isSuccessful;

    /**
     * Returns the user that logged in, or null if the attempt was
     * unsuccessful.
     *
     * @return The logged in user or null.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Returns whether the login attempt was successful.
     *
     * @return True if the attempt succeeded.
     */
    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    /**
     * Creates a new Authentication result.
     *
     * @param isSuccessful Wether this attempt succeeded.
     * @param user The user. Must be null is unsuccessful.
     */
    AuthenticationResult(final boolean isSuccessful, final User user)  {
        if (isSuccessful && user == null) {
            throw new IllegalArgumentException("User mustn't be null on "
                                             + "successful login.");
        }
        if (!isSuccessful && user != null) {
            throw new IllegalArgumentException("User must be null on failed "
                                             + "login.");
        }
        this.isSuccessful = isSuccessful;
        this.user = user;
    }

    /**
     * Default constructor for serializability.
     */
    protected AuthenticationResult() {
    }
}
