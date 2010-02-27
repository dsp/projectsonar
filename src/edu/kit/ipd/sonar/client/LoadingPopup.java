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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.PopupPanel;

import edu.kit.ipd.sonar.client.event.FinishLoadingEvent;
import edu.kit.ipd.sonar.client.event.FinishLoadingEventHandler;
import edu.kit.ipd.sonar.client.event.StartLoadingEvent;
import edu.kit.ipd.sonar.client.event.StartLoadingEventHandler;

/**
 * This class represents a popup that indicates a loading process to the user.
 * Fire a StartLoadingEvent on the EventBus to cause the show and a
 * FinishLoadingEvent to cause it to hide. Note that the popup only
 * hides itself again if it recieves a FinishLoadingEvent for every
 * StartLoadigEvent it recieved in the past.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public class LoadingPopup extends PopupPanel {

    /** stores how many loading-screens are called at the moment. */
    private int callcounter = 0;

    /**
     * Create a new LoadingPopup object.
     * Registers itself with the event bus so that it reacts to
     * StartLoadingEvents and FinishLoadingEvents.
     * Should be called once during frontend-startup.
     */
    public LoadingPopup() {
        super();
        GWT.log("LoadingPopup: initializing...", null);
        hide();
        setGlassEnabled(true);

        this.setStyleName("loadingPopup");

        //Register eventHandler for StartLoadingEvents:
        EventBus.getHandlerManager().addHandler(
                StartLoadingEvent.TYPE,
                new StartLoadingEventHandler() {
                    public void onStartLoading(
                            final StartLoadingEvent e) {
                        callcounter++;
                        GWT.log("LoadingPopup: recieved StartLoadingEvent."
                                + " Counter is " + callcounter, null);
                        if (callcounter > 0) {
                            GWT.log("LoadingPopup: showing Popup ", null);
                            center();
                            }
                        }
                });
        //Register eventHandler for FinishLoadingEvents:
        EventBus.getHandlerManager().addHandler(
                FinishLoadingEvent.TYPE,
                new FinishLoadingEventHandler() {
                    public void onFinishLoading(
                            final FinishLoadingEvent e) {
                        callcounter--;
                        GWT.log("LoadingPopup: recieved FinishLoadingEvent,"
                                + " Counter is " + callcounter, null);
                        if (callcounter <= 0) {
                            GWT.log("LoadingPopup: hiding Popup ", null);
                            hide();
                            }
                        }
                });


        GWT.log("LoadingPopup: initialized.", null);
    }
}
