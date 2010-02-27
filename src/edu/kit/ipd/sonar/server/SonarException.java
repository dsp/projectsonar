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
package edu.kit.ipd.sonar.server;

/**
 * Superclass for all exceptions used within the Sonar project.
 *
 * This class is primarly a superclass for all exception within the Sonar
 * project and is usually not used directly. It can be used to have easier
 * catch casese.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class SonarException extends Exception {
    /**
     * Instantiate a new object.
     */
    public SonarException() {
        super();
    }

    /**
     * Instantiate an ew object with a message.
     *
     * @param msg The message
     */
    public SonarException(final String msg) {
        super(msg);
    }
}
