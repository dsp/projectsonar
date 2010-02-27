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
import edu.kit.ipd.sonar.server.TimeBoundary;

/**
 * A AvailableTimeBoundaryArrivedEvent occurs if a new TimeBoundary has
 * arrived from the server.
 */
public class AvailableTimeBoundaryArrivedEvent extends
        GwtEvent<AvailableTimeBoundaryArrivedEventHandler> {

    /** The new TimeBoundary. */
    private TimeBoundary timeBoundary;

    /** Event Type. */
    public static final
        GwtEvent.Type<AvailableTimeBoundaryArrivedEventHandler> TYPE
            = new GwtEvent.Type<AvailableTimeBoundaryArrivedEventHandler>();

    /**
     * Returns the new TimeBoundary.
     *
     * @return the new TimeBoundary.
     */
    public TimeBoundary getTimeBoundary() {
        return timeBoundary;
    }

    /**
     * Constructs a new Instance of an AvailableTimeBoundaryArrivedEvent.
     *
     * @param timeBoundary
     *            the TimeBoundary which arrived from the server.
     */
    public AvailableTimeBoundaryArrivedEvent(
            final TimeBoundary timeBoundary) {
        this.timeBoundary = timeBoundary;
    }

    /**
     * Needed by Gwt.
     *
     * @return An object representing the type of this event.
     */
    @Override
    public GwtEvent.Type<AvailableTimeBoundaryArrivedEventHandler>
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
            final AvailableTimeBoundaryArrivedEventHandler handler) {
        handler.onAvailableTimeBoundaryArrived(this);
    }
}

