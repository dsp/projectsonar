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
import edu.kit.ipd.sonar.server.centralities.Centrality;

/**
 * A AvailableCentralitiesArrivedEvent occurs if a list containing the available
 * Centralities has arrived.
 */
public class AvailableCentralitiesArrivedEvent extends
        GwtEvent<AvailableCentralitiesArrivedEventHandler> {

    /** The list of centralities that arrived. */
    private ArrayList<Centrality> centralities;

    /** Event Type. */
    public static final
        GwtEvent.Type<AvailableCentralitiesArrivedEventHandler> TYPE
        = new GwtEvent.Type<AvailableCentralitiesArrivedEventHandler>();

    /**
     * Returns the available centralities which arrived from the server.
     *
     * @return the available centralities.
     */
    public ArrayList<Centrality> getCentralities() {
        return centralities;
    }

    /**
     * Constructs a new Instance of an AvailableCentralitiesArrivedEvent.
     *
     * @param centralities
     *            the new List of centralities.
     */
    public AvailableCentralitiesArrivedEvent(
            final ArrayList<Centrality> centralities) {

        this.centralities = centralities;
    }

    /**
     * Needed by Gwt.
     *
     * @return An object representing the type of this event.
     */
    @Override
    public GwtEvent.Type<AvailableCentralitiesArrivedEventHandler>
            getAssociatedType() {
        return TYPE;
    }

    /**
     * Needed by GWT.
     *
     * @param handler
     *            The handler which handles this event.
     */
    @Override
    public void dispatch(
            final AvailableCentralitiesArrivedEventHandler handler) {
        handler.onAvailableCentralitiesArrived(this);
    }
}


