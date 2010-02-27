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
import edu.kit.ipd.sonar.client.rpc.GraphSpecification;
import edu.kit.ipd.sonar.server.Graph;

/**
 * A GraphArrivedEvent occurs if a new Graph has arrived from the server.
 */
public class GraphArrivedEvent extends GwtEvent<GraphArrivedEventHandler> {

    /** The Graph-Object that arrived from the server. */
    private final Graph graph;

    /** The Graph Specification this graph was requested with. */
    private final GraphSpecification graphSpecification;

    /** Event Type. */
    public static final GwtEvent.Type<GraphArrivedEventHandler> TYPE
            = new GwtEvent.Type<GraphArrivedEventHandler>();

    /**
     * Returns the Graph which arrived.
     *
     * @return the Graph which arrived from the server.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Returns the GraphSpecification for the arrived graph.
     *
     * @return The GraphSpecification for this graph.
     */
    public GraphSpecification getGraphSpecification() {
        return this.graphSpecification;
    }

    /**
     * Constructs a new Instance of an GraphArrivedEvent.
     *
     * @param graph the Grpah which arrived from the server.
     * @param graphSpecification The original specification for the graph.
     */
    public GraphArrivedEvent(final Graph graph,
                             final GraphSpecification graphSpecification) {
        this.graph = graph;
        this.graphSpecification = graphSpecification;
    }

    /**
     * Needed by Gwt.
     *
     * @return An object representing the type of this event.
     */
    @Override
    public GwtEvent.Type<GraphArrivedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Needed by GWT.
     *
     * @param handler
     *            The handler which handles this event.
     */
    @Override
    public void dispatch(final GraphArrivedEventHandler handler) {
        handler.onGraphArrived(this);
    }
}

