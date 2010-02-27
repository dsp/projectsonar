/**
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
package edu.kit.ipd.sonar.client.event;

import com.google.gwt.event.shared.GwtEvent;
import edu.kit.ipd.sonar.server.User;

/**
 * A SuccessfulAuthenticationEvent occurs if a user could autheticate
 * himself successfully.
 */
public class SuccessfulAuthenticationEvent extends
        GwtEvent<SuccessfulAuthenticationEventHandler> {

    /**
     * The User-Object of the user who logged in.
     *
     * NULL if admin.
     */
    private User user;

    /** Indicating that the user logged in as admin. */
    private boolean isAdmin;

    /** Event Type. */
    public static final
        GwtEvent.Type<SuccessfulAuthenticationEventHandler> TYPE
            = new GwtEvent.Type<SuccessfulAuthenticationEventHandler>();

    /**
     * Needed by Gwt.
     *
     * @return An object representing the type of this event.
     */
    @Override
    public GwtEvent.Type<SuccessfulAuthenticationEventHandler>
            getAssociatedType() {
        return TYPE;
    }

    /**
     * Returns the User who logged in.
     *
     * @return the user who logged in. NULL if the user is admin.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns if we just authenticated as admin.
     *
     * @return Whether we have administrative privileges.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Constructs a new Instance of an SuccessfulAuthenticationEvent.
     *
     * @param user the User who logged in. NULL if the user is admin.
     * @param isAdmin true if the user has admin-rights.
     */
    public SuccessfulAuthenticationEvent(final User user,
                                         final boolean isAdmin) {

        this.user = user;
        this.isAdmin = isAdmin;
    }

    /**
     * Needed by GWT.
     *
     * @param handler
     *            The handler which handles this event.
     */
    @Override
    public void dispatch(
            final SuccessfulAuthenticationEventHandler handler) {
        handler.onSuccessfulAuthentication(this);
    }
}


