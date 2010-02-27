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
package edu.kit.ipd.sonar.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * An AttemptAuthenticationEvent means that the user supplied credentials and
 * tries to get authenticated.
 */
public class AttemptAuthenticationEvent extends
        GwtEvent<AttemptAuthenticationEventHandler> {

    /** Event Type. */
    public static final GwtEvent.Type<AttemptAuthenticationEventHandler> TYPE
            = new GwtEvent.Type<AttemptAuthenticationEventHandler>();

    /** True when the user tries to authenticate as admin. */
    private boolean isAdmin;

    /**
     * The name of the user we want to authenticate for.
     *
     * Must be null if isAdmin is true.
     */
    private String username;

    /** The password provided by the user. */
    private String password;

    /**
     * This is needed by GWT.
     *
     * @return An object representing the Type of this event.
     */
    @Override
    public GwtEvent.Type<AttemptAuthenticationEventHandler>
            getAssociatedType() {
        return TYPE;
    }

    /**
     * This is needed by GWT.
     *
     * @param handler
     *            The handler which handles this event.
     */
    @Override
    protected void dispatch(final AttemptAuthenticationEventHandler handler) {
        handler.onAttemptAuthentication(this);
    }

    /**
     * Creates a new AttemptAuthenticationEvent.
     *
     * @param isAdmin
     *            Do we want to become admin?
     * @param username
     *            Which user are we trying to authenticate? Must be null if
     *            isAdmin is true.
     * @param password
     *            The password to authenticate with.
     */
    public AttemptAuthenticationEvent(final boolean isAdmin,
                                      final String username,
                                      final String password) {
        this.isAdmin = isAdmin;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the name of the User who wants to log in.
     *
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password the user entered.
     *
     * @return String password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns if the user wants to login as admin.
     *
     * @return boolean isAdmin
     */
    public boolean isAdmin() {
        return isAdmin;
    }
}


