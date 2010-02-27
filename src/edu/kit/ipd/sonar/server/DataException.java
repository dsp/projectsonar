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
 * Thrown if there is an exception during the retreiving or processing of data.
 *
 * @author Martin Reiche <martin.reiche@student.kit.edu>
 */
public class DataException extends SonarException {
    /**
     * Initialize a new object.
     */
    public DataException() {
        super();
    }

    /**
     * Instantiate a new Exception with a message.
     *
     * @param msg The message
     */
    public DataException(final String msg) {
        super(msg);
    }
}
