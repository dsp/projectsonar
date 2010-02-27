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
import com.google.gwt.user.client.Command;

/**
 * A ErrorOccuredEvent is created if an error about that the user has to be
 * notified occures.
 */
public class ErrorOccuredEvent extends GwtEvent<ErrorOccuredEventHandler> {

    /** The error message. */
    private String message;

    /** The command to be run when the user acknowledged the error. */
    private Command command;

    /** Event Type. */
    public static final GwtEvent.Type<ErrorOccuredEventHandler> TYPE
                        = new GwtEvent.Type<ErrorOccuredEventHandler>();

    /**
     * Returns the error message.
     *
     * @return The error message
     */
    public String getErrorMessage() {
        return message;
    }

    /**
     * Returns the contained command.
     *
     * @return The command.
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * Constructs a new Instance of an ErrorOccuredEvent.
     *
     * @param message The error-message for this event.
     */
    public ErrorOccuredEvent(final String message) {
        this(message, null);
    }

    /**
     * Constructs a new Instance of an ErrorOccuredEvent.
     *
     * @param message The error message for this event.
     * @param command The command to execute when the user
     *                acknowledged the error.
     */
    public ErrorOccuredEvent(final String message, final Command command) {
        this.message = message;
        this.command = command;
    }

    /**
     * Needed by Gwt.
     *
     * @return An object representing the type of this event.
     */
    @Override
    public GwtEvent.Type<ErrorOccuredEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Needed by GWT.
     *
     * @param handler
     *            The handler which handles this event.
     */
    @Override
    public void dispatch(final ErrorOccuredEventHandler handler) {
        handler.onErrorOccured(this);
    }
}


