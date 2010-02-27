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

import edu.kit.ipd.sonar.client.event.FinishLoadingEvent;
import edu.kit.ipd.sonar.client.event.StartLoadingEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEventHandler;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEventHandler;
import edu.kit.ipd.sonar.client.rpc.RPCHandler;
import edu.kit.ipd.sonar.client.rpc.RPCService;
import edu.kit.ipd.sonar.client.rpc.RPCServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point class defining <code>onModuleLoad()</code>.
 *
 * @author Kevin-Simon Kohlmeyer
 */
public class GuiController implements EntryPoint {

    /** The index of the NormalScreen in the deckpanel. */
    private static final int NORMAL_SCREEN = 0;

    /** The index of the LoginScreen in the deckpanel. */
    private static final int LOGIN_SCREEN = 1;

    /** The deckpanel holding the login screen and the normal screen. */
    private final DeckPanel deckPanel = new DeckPanel();

    /**
     * This is the entry point method.
     */
    public final void onModuleLoad() {
        new RPCHandler(EventBus.getHandlerManager(),
                (RPCServiceAsync) GWT.create(RPCService.class));
        /* Init the LoadingPopup*/
        LoadingPopup loadingPopup = new LoadingPopup();

        /* show the loading screen */
        EventBus.getHandlerManager().fireEvent(
                new StartLoadingEvent());


        /* Init the ErrorPopup */
        ErrorPopup errorPopup = new ErrorPopup();

        /* Start the GraphConverter */
        GraphConverter graphconverter
                = new GraphConverter(EventBus.getHandlerManager());

        /* Do not change the order of this or our constants will break. */
        deckPanel.add(new NormalScreen());
        deckPanel.add(new LoginScreen());
        deckPanel.showWidget(LOGIN_SCREEN);

        RootPanel.get().add(deckPanel);

        /* Register for Events */
        HandlerManager hm = EventBus.getHandlerManager();

        /*
         * When the user authenticated successfully, we need to hide the
         * login screen and show the normal screen instead.
         */
        hm.addHandler(SuccessfulAuthenticationEvent.TYPE,
                new SuccessfulAuthenticationEventHandler() {
                    public void onSuccessfulAuthentication(
                            final SuccessfulAuthenticationEvent e) {
                        GWT.log("GuiController: got auth event, switching"
                                + " to normal screen", null);
                        deckPanel.showWidget(NORMAL_SCREEN);
                        }
                });

        hm.addHandler(SuccessfulLogoutEvent.TYPE,
                new SuccessfulLogoutEventHandler() {
                    public void onSuccessfulLogout(
                            final SuccessfulLogoutEvent e) {
                        GWT.log("GuiController: got logout event, switching"
                                + " to login screen", null);
                        deckPanel.showWidget(LOGIN_SCREEN);
                    }
                });

        /* hide the loading screen */
        EventBus.getHandlerManager().fireEvent(
                new FinishLoadingEvent());
    }
}
