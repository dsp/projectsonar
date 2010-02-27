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
import edu.kit.ipd.sonar.client.DrawableGraph;

/**
 * A DrawableGraphArrivedEvent occurs if a new DrawableGraph is ready to be
 * drawn.
 */
public class DrawableGraphArrivedEvent extends
        GwtEvent<DrawableGraphArrivedEventHandler> {

    /** The DrawableGraph-Object that is ready to be drawn. */
    private DrawableGraph dgraph;

    /** Event Type. */
    public static final GwtEvent.Type<DrawableGraphArrivedEventHandler> TYPE
            = new GwtEvent.Type<DrawableGraphArrivedEventHandler>();

    /**
     * Returns the DrawableGraph which is ready to be drawn.
     *
     * @return the DrawableGraph.
     */
    public DrawableGraph getDrawableGraph() {
        return dgraph;
    }

    /**
     * Constructs a new Instance of an DrawableGraphArrivedEvent.
     *
     * @param dgraph The DrawableGraph that is ready to be drawn
     */
    public DrawableGraphArrivedEvent(final DrawableGraph dgraph) {
        this.dgraph = dgraph;
    }

    /**
     * Needed by Gwt.
     *
     * @return An object representing the type of this event.
     */
    @Override
    public GwtEvent.Type<DrawableGraphArrivedEventHandler>
            getAssociatedType() {
        return TYPE;
    }

    /**
     * Needed by GWT.
     *
     * @param handler The handler which handles this event.
     */
    @Override
    public void dispatch(final DrawableGraphArrivedEventHandler handler) {
        handler.onDrawableGraphArrived(this);
    }
}

