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

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Command;
import edu.kit.ipd.sonar.client.event.ErrorOccuredEvent;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.event.logical.shared.CloseEvent;

/**
 * Tests the errorpopup.
 *
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public class ErrorPopupTest extends GWTTestCase {

    ErrorPopup ep = null;

    @Override
    public String getModuleName() {
        return "edu.kit.ipd.sonar.Sonar";
    }

    @Override
    protected void gwtSetUp() {
        ep = new ErrorPopup();
    }

    /**
     * Test showing the errorpopup.
     */
    public void testShow() {
        EventBus.getHandlerManager().fireEvent(new ErrorOccuredEvent("test"));

        delayTestFinish(500);

        DeferredCommand.addCommand(new Command() {
            public void execute() {
                assertTrue(ep.isShowing());
                CloseEvent.fire(ep, ep);
                finishTest();
            }
        });
    }

    /**
     * Test if a provided command is executed correctly.
     */
    public void testCommand() {
        Command c = new Command() {
            public void execute() {
                finishTest();
            }
        };

        EventBus.getHandlerManager().fireEvent(
                new ErrorOccuredEvent("test", c));
        delayTestFinish(500);
        CloseEvent.fire(ep, ep);
    }
}
