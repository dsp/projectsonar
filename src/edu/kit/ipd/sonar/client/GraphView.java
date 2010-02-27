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
package edu.kit.ipd.sonar.client;

import edu.kit.ipd.sonar.client.event.DrawableGraphArrivedEvent;
import edu.kit.ipd.sonar.client.event.DrawableGraphArrivedEventHandler;
import edu.kit.ipd.sonar.client.event.StartLoadingEvent;
import edu.kit.ipd.sonar.client.event.FinishLoadingEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEventHandler;
import edu.kit.ipd.sonar.client.jsxGraph.JSXGraphDrawer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Label;


/**
 * This class represents the surface, on which the graphs will be drawn.
 */
public class GraphView extends HTML {

    /** graphdrawer to draw the graph. */
    private final GraphDrawer graphdrawer;

    /** The tooltip box for jsxgraph. */
    private Label tooltipBox;

    /** The opacity of the injected tooltip window, we want this in java code
     * for cross-browser support. */
    private static final double TOOLTIP_OPACITY = 70;

    /** DOM-ID of the jsxgraph container element. */
    public static final String JSXCONTAINER_ID = "jsxBoardContainer";

    /**
     * Create a new GraphView.
     *
     * Registers itself with the event bus.
     */
    public GraphView() {
        super("<div id='" + JSXCONTAINER_ID + "' style='width:100%; "
            + "height:100%;'></div>");
        setWidth("100%");
        setHeight("100%");

        HandlerManager hm = EventBus.getHandlerManager();
        graphdrawer = new JSXGraphDrawer(JSXCONTAINER_ID);

        /* This handler is going to trigger if a new drawable graph is ready.
         * It will start a loading screen, call the graphdrawer to draw the
         * graph and then stop the loading screen
         */
        hm.addHandler(DrawableGraphArrivedEvent.TYPE,
            new DrawableGraphArrivedEventHandler() {
                public void onDrawableGraphArrived(
                        final DrawableGraphArrivedEvent event) {

                    GWT.log("GraphView: got DrawableGraphArrivedEvent,"
                            + "start loading, start drawing", null);
                    // start the loading screen
                    EventBus.getHandlerManager().fireEvent(
                        new StartLoadingEvent());

                    // draw the graph
                    graphdrawer.drawDrawableGraph(event.getDrawableGraph());

                    GWT.log("GraphView: stop loading", null);

                    // stop the loading screen
                    EventBus.getHandlerManager().fireEvent(
                        new FinishLoadingEvent());
                }
            });

        hm.addHandler(SuccessfulLogoutEvent.TYPE,
                new SuccessfulLogoutEventHandler() {
                    public void onSuccessfulLogout(
                            final SuccessfulLogoutEvent e) {
                        GWT.log("GraphView: got logout event, reseting"
                                + " board", null);
                        graphdrawer.reset();
                    }
                });
    }
}
