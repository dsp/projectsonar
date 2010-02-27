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
 * This exception can occur if an invalid time boundary was passed.
 *
 * An example would be that the frontend provides a TimeBoundary which has
 * negative values.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class InvalidTimeBoundaryException extends SonarException {
}
