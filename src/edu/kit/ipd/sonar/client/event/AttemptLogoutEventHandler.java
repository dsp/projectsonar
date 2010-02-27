/*
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

import com.google.gwt.event.shared.EventHandler;

/**
 * An AttemptLogoutEventHandler handles AttemptLogoutEvents.
 */
public interface AttemptLogoutEventHandler extends EventHandler {

    /**
     * The handler's method. Gets called if an AttemptLogoutEvent has occured.
     *
     * @param event The event to Handle
     */
    void onAttemptLogout(AttemptLogoutEvent event);
}


