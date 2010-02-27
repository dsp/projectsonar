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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.kit.ipd.sonar.client.event.ErrorOccuredEvent;
import edu.kit.ipd.sonar.client.event.ErrorOccuredEventHandler;

/**
 * This class provides a Popup that displays errors to the user.
 * It is invoked by ErrorOccuredEvents.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public class ErrorPopup extends PopupPanel {

    /** Contains localized Strings. */
    private SonarMessages messages
            = (SonarMessages) GWT.create(SonarMessages.class);

    /**
     * The Label containing the Popup's message.
     */
    private Label messageLabel = new Label();

    /** The command to run when we're closing. */
    private Command command = null;

    /**
     * Creates a new ErrorPopup object, that displays itself on
     * ErrorOccuredEvents. The constructor needs to be called once to
     * enable this functionality.
     */
    public ErrorPopup() {
        super(true, true);
        GWT.log("ErrorPopup: initializing...", null);
        hide();
        setAnimationEnabled(true);
        setGlassEnabled(true);

        VerticalPanel contents = new VerticalPanel();
        contents.add(new Label(messages.anErrorOccured()));
        contents.add(messageLabel);
        contents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        Button button = new Button(messages.proceed(), new ClickHandler() {
                    public void onClick(final ClickEvent e) {
                        hide();
                    }
        });
        contents.add(button);
        this.add(contents);
        contents.setStyleName("errorPopupContents");
        this.setStyleName("errorPopup");

        //Register eventHandler for ErrorOccuredEvents:
        EventBus.getHandlerManager().addHandler(
                ErrorOccuredEvent.TYPE, new ErrorOccuredEventHandler() {
                    public void onErrorOccured(
                            final ErrorOccuredEvent e) {
                        GWT.log("ErrorPopup: recieved ErrorOcc.Event", null);
                        messageLabel.setText(e.getErrorMessage());
                        // This might be null, which means we do nothing.
                        command = e.getCommand();
                        center();
                        }
        });

        this.addCloseHandler(new CloseHandler<PopupPanel>() {
            public void onClose(final CloseEvent e) {
                if (command != null) {
                    DeferredCommand.addCommand(command);
                }
            }
        });
        GWT.log("ErrorPopup: initialized", null);
    }

    @Override
    protected void onPreviewNativeEvent(final NativePreviewEvent e) {
        if (e.getNativeEvent().getType().equals("keydown")) {
            hide();
        }
    }
}
