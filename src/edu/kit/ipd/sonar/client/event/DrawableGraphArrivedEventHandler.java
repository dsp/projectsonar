/*
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import com.google.gwt.event.shared.EventHandler;

/**
 * Handles DrawableGraphArrivedEvents.
 */
public interface DrawableGraphArrivedEventHandler extends EventHandler {

    /**
     * The handler's method.
     *
     * @param event
     *            The event to be handled.
     */
    void onDrawableGraphArrived(DrawableGraphArrivedEvent event);
}

