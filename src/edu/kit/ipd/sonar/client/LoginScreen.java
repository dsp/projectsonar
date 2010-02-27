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
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;

import edu.kit.ipd.sonar.client.event.AttemptAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.ErrorOccuredEvent;
import edu.kit.ipd.sonar.client.event.ErrorOccuredEventHandler;
import edu.kit.ipd.sonar.client.event.FailedAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.FailedAuthenticationEventHandler;
import edu.kit.ipd.sonar.client.event.FinishLoadingEvent;
import edu.kit.ipd.sonar.client.event.StartLoadingEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEventHandler;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulLogoutEventHandler;

/**
 * This class represents a screen where the user can log in.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public class LoginScreen extends Composite {

    /** Stores the strings. */
    private SonarMessages messages
            = (SonarMessages) GWT.create(SonarMessages.class);

    /** A place for the user to enter his login name. */
    private TextBox userNameBox = new TextBox();

    /** The user can enter a password here. */
    private PasswordTextBox passwordBox = new PasswordTextBox();

    /**
     * The ErrorOccuredHandler that is registered when we try to
     * log in.
     *
     * We need that so we can remove the loading screen when a server
     * fail happens.
     */
    private final ErrorOccuredEventHandler errorHandler
            = new ErrorOccuredEventHandler() {
                public void onErrorOccured(final ErrorOccuredEvent e) {
                    // Stupid GWT HandlerManager fires events instant,
                    // but removes Handlers deferredly (is that even a word?)
                    if (errorHandlerRegistration != null) {
                        errorHandlerRegistration.removeHandler();
                        errorHandlerRegistration = null;
                        EventBus.getHandlerManager().fireEvent(
                                new FinishLoadingEvent());
                    }
                }
    };

    /** Registration for the ErrorHandler. */
    private HandlerRegistration errorHandlerRegistration = null;

    /** Toggles administator login. */
    private ToggleButton adminButton
        = new ToggleButton(
            new Image(GWT.getModuleBaseURL() + "images/adminbutton_up.png"),
            new Image(GWT.getModuleBaseURL() + "images/adminbutton_down.png"));

    /** Used to set focus. Must be deferred cause we're not shown when run. */
    private Command setFocusCommand = new Command() {
        public void execute() {
            if (adminButton.isDown()) {
                passwordBox.setFocus(true);
            } else {
                userNameBox.setFocus(true);
                userNameBox.selectAll();
            }
        }
    };

    /** Constructor. Registers independently for events. */
    public LoginScreen() {
        // Set up structure
        FlexTable loginMenu = new FlexTable();
        loginMenu.setStyleName("loginMenu");
        loginMenu.setWidget(0, 0, new Label(messages.userNameLabel()));
        loginMenu.setWidget(0, 1, userNameBox);
        userNameBox.setStyleName("loginBox");
        loginMenu.setWidget(0, 2, adminButton);
        adminButton.setStyleName("adminButton");
        loginMenu.setWidget(1, 0, new Label(messages.passwordLabel()));
        loginMenu.setWidget(1, 1, passwordBox);
        passwordBox.setStyleName("loginPasswordBox");
        Button loginButton = new Button(messages.loginButtonText());
        loginMenu.setWidget(2, 1, loginButton);
        loginMenu.getCellFormatter().setHorizontalAlignment(2, 1,
                HasHorizontalAlignment.ALIGN_RIGHT);
        loginMenu.getFlexCellFormatter().setColSpan(1, 1, 2);
        loginMenu.getFlexCellFormatter().setColSpan(2, 1, 2);

        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setStyleName("loginScreen");
        hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        hPanel.add(loginMenu);
        Image loginLogo = new Image(GWT.getModuleBaseURL()
                                    + "images/logo_login.png");
        loginLogo.setStyleName("loginLogo");
        hPanel.add(loginLogo);

        SimplePanel workaround = new SimplePanel();
        workaround.add(hPanel);
        initWidget(workaround);

        // Set various stuff
        int index = 1;
        userNameBox.setTabIndex(index++);
        passwordBox.setTabIndex(index++);
        loginButton.setTabIndex(index++);

        DeferredCommand.addCommand(setFocusCommand);

        // Event Handlers
        userNameBox.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(final KeyDownEvent e) {
                if (e.getNativeKeyCode() == KEY_ENTER) {
                    passwordBox.setFocus(true);
                    passwordBox.selectAll();
                }
            }
        });

        passwordBox.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(final KeyDownEvent e) {
                if (e.getNativeKeyCode() == KEY_ENTER) {
                    attemptAuth();
                }
            }
        });

        loginButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent e) {
                attemptAuth();
            }
        });

        adminButton.addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent e) {
                        if (adminButton.isDown()) {
                            userNameBox.setText(messages.administratorName());
                            userNameBox.setEnabled(false);
                        } else {
                            userNameBox.setText("");
                            userNameBox.setEnabled(true);
                        }
                        passwordBox.setText("");
                        DeferredCommand.addCommand(setFocusCommand);
                    }
        });

        EventBus.getHandlerManager().addHandler(FailedAuthenticationEvent.TYPE,
                new FailedAuthenticationEventHandler() {
                    public void onFailedAuthentication(
                            final FailedAuthenticationEvent e) {
                        failedAuth();
                    }
        });

        EventBus.getHandlerManager().addHandler(
                SuccessfulAuthenticationEvent.TYPE,
                new SuccessfulAuthenticationEventHandler() {
                    public void onSuccessfulAuthentication(
                            final SuccessfulAuthenticationEvent e) {
                        successfulAuth();
                    }
        });

        EventBus.getHandlerManager().addHandler(
                SuccessfulLogoutEvent.TYPE,
                new SuccessfulLogoutEventHandler() {
                    public void onSuccessfulLogout(
                            final SuccessfulLogoutEvent e) {
                        DeferredCommand.addCommand(setFocusCommand);
                    }
        });
    }

    /** Called when we want to try to log in. */
    private void attemptAuth() {
        if (errorHandlerRegistration != null) {
            errorHandlerRegistration.removeHandler();
            errorHandlerRegistration = null;
        }
        EventBus.getHandlerManager().fireEvent(new StartLoadingEvent());
        errorHandlerRegistration = 
                EventBus.getHandlerManager().addHandler(ErrorOccuredEvent.TYPE,
                                                        errorHandler);
        if (adminButton.isDown()) {
            EventBus.getHandlerManager().fireEvent(
                    new AttemptAuthenticationEvent(true, null,
                                                   passwordBox.getText()));
        } else {
            EventBus.getHandlerManager().fireEvent(
                    new AttemptAuthenticationEvent(false,
                                                   userNameBox.getText(),
                                                   passwordBox.getText()));
        }
        passwordBox.setText("");
    }

    /** Called when the authentication succeeded. */
    private void successfulAuth() {
        adminButton.setDown(false);
        userNameBox.setText("");
        userNameBox.setEnabled(true);
        passwordBox.setText("");
        if (errorHandlerRegistration != null) {
            errorHandlerRegistration.removeHandler();
            errorHandlerRegistration = null;
        }
        EventBus.getHandlerManager().fireEvent(new FinishLoadingEvent());
    }

    /** Called when the authentication failed. */
    private void failedAuth() {
        if (errorHandlerRegistration != null) {
            errorHandlerRegistration.removeHandler();
            errorHandlerRegistration = null;
        }
        EventBus.getHandlerManager().fireEvent(new FinishLoadingEvent());
        EventBus.getHandlerManager().fireEvent(
                new ErrorOccuredEvent(messages.wrongCredentialsNote(),
                                      setFocusCommand));
    }

}
