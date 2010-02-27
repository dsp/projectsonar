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
 * This exception can occur if a calculation in the Calculator classes
 * fails unexpect.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class CalculationFailedException extends SonarException {
    /**
     * Instantiate a new exception.
     */
    public CalculationFailedException() {
        super();
    }

    /**
     * Instantiate a new exception with a message.
     *
     * @param msg The message
     */
    public CalculationFailedException(final String msg) {
        super(msg);
    }
}
