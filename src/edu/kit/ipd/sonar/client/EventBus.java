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

package edu.kit.ipd.sonar.client;

import com.google.gwt.event.shared.HandlerManager;

/**
 * The Class Eventbus provides static access to a Singleton-like-Instance of a
 * GWT-HandlerManager, which is used to Handle Events within Sonar's Frontend.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public final class EventBus {

    /**
     * The static HandlerManager-Instance of the EventBus.
     */
    private static HandlerManager handlerManagerInstance
        = new HandlerManager(null);

    /**
     * The static method getHandlerManager() returns the projects
     * HandlerManager. It always returns the same instance of the object.
     *
     * @return The HandlerManager of the EventBus
     */
    public static HandlerManager getHandlerManager() {
        return handlerManagerInstance;

    }

    /**
     * Protected Constructor for the EventBus, as all its methods are static and
     * it never needs to be called.
     */
    protected EventBus() {
    }
}
