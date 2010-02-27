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
import edu.kit.ipd.sonar.client.rpc.DrawableGraphSpecification;

/**
 * A DrawableGraphRequestEvent occurs if a new DrawableGraph is
 * requested within the system.
 */
public class DrawableGraphRequestEvent extends
        GwtEvent<DrawableGraphRequestEventHandler> {

    /** The Specification of the Graph to draw. */
    private DrawableGraphSpecification specification;

    /** Event Type. */
    public static final GwtEvent.Type<DrawableGraphRequestEventHandler> TYPE
            = new GwtEvent.Type<DrawableGraphRequestEventHandler>();

    /**
     * Returns the specification of the requested DrawableGraph.
     *
     * @return the specification of the requested DrawableGraph.
     */
    public DrawableGraphSpecification getDrawableGraphSpecification() {
        return specification;
    }

    /**
     * Constructs a new Instance of an DrawableGraphRequestEvent.
     *
     * @param specification
     *            the specification of the requested DrawableGraph.
     */
    public DrawableGraphRequestEvent(
            final DrawableGraphSpecification specification) {

        this.specification = specification;
    }

    /**
     * Needed by Gwt.
     *
     * @return An object representing the type of this event.
     */
    @Override
    public GwtEvent.Type<DrawableGraphRequestEventHandler>
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
    public void dispatch(final DrawableGraphRequestEventHandler handler) {
        handler.onDrawableGraphRequest(this);
    }
}

