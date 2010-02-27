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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.DockPanel;

import com.google.gwt.core.client.GWT;

/**
 * This class arranges a Menu, a GraphView and a Timeline.
 */
public class NormalScreen extends Composite {

    /** The Dockpanel used for our layout. */
    private DockPanel dockpanel = new DockPanel();

    /**
     * Creates a new NormalScreen object.
     *
     * Registers itself with the event bus.
     */
    public NormalScreen() {
        dockpanel.setHeight("100%");
        dockpanel.setWidth("100%");
        initWidget(dockpanel);

        GraphView graphview = new GraphView();
        Label banner = new Label();
        banner.setStyleName("banner");
        Timeline timeline = new Timeline();
        timeline.setWidth("100%");
        timeline.getElement().setClassName("timeslider");

        Menu menu = new Menu(timeline);

        dockpanel.add(banner, DockPanel.NORTH);
        dockpanel.setCellHeight(banner, "1px");
        dockpanel.add(timeline, DockPanel.SOUTH);
        dockpanel.setCellHeight(timeline, "70px");
        dockpanel.add(menu, DockPanel.WEST);
        dockpanel.setCellWidth(menu, "1px");
        dockpanel.add(graphview, DockPanel.CENTER);

        GWT.log("NormalScreen created", null);
    }
}
