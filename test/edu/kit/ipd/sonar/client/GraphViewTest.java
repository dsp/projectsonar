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

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import edu.kit.ipd.sonar.client.DrawableGraph;
import edu.kit.ipd.sonar.client.DrawableEdge;
import edu.kit.ipd.sonar.client.DrawableNode;
import edu.kit.ipd.sonar.client.event.DrawableGraphArrivedEvent;
import edu.kit.ipd.sonar.client.event.DrawableGraphArrivedEventHandler;
import edu.kit.ipd.sonar.client.event.StartLoadingEvent;
import edu.kit.ipd.sonar.client.event.StartLoadingEventHandler;
import edu.kit.ipd.sonar.client.event.FinishLoadingEvent;
import edu.kit.ipd.sonar.client.event.FinishLoadingEventHandler;

/**
 * Tests the Color-Class.
 *
 * @author Reno Reckling <reno.reckling@web.de>
 */
public class GraphViewTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "edu.kit.ipd.sonar.Sonar";
    }

    /**
     * Test whether we could initialize GraphView.
     * We can't test more, because the rest depends on external javascript
     * librarys and fails on testing.
     */
    public void testInit() {
        GraphView view = new GraphView();
        assertNotNull(view);
    }
}
