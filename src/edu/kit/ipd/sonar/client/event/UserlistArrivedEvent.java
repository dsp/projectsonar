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
import java.util.ArrayList;
import edu.kit.ipd.sonar.server.User;

/**
 * A UserlistArrivedEvent occurs if a list containing all Users has arrived.
 */
public class UserlistArrivedEvent extends
        GwtEvent<UserlistArrivedEventHandler> {

    /** The list of users that arrived. */
    private ArrayList<User> users;

    /** Event Type. */
    public static final GwtEvent.Type<UserlistArrivedEventHandler> TYPE
            = new GwtEvent.Type<UserlistArrivedEventHandler>();

    /**
     * Needed by Gwt.
     *
     * @return An object representing the type of this event.
     */
    @Override
    public GwtEvent.Type<UserlistArrivedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Returns the list of users which arrived from the server.
     *
     * @return the list of users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Constructs a new Instance of an UserlistArrivedEvent.
     *
     * @param users
     *            the new List of Users
     */
    public UserlistArrivedEvent(final ArrayList<User> users) {

        this.users = users;
    }

    /**
     * Needed by GWT.
     *
     * @param handler
     *            The handler which handles this event.
     */
    @Override
    public void dispatch(final UserlistArrivedEventHandler handler) {
        handler.onUserlistArrived(this);
    }
}

