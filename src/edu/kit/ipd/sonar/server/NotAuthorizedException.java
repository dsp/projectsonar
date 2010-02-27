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
 * Can ooccur if a client is not authorized to view the requested information.
 *
 * An exmaple is that the user is not logged in as an administrator but tries to
 * retreive a graph for global view using getGlobalGraph in RPCServiceImpl.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class NotAuthorizedException extends SonarException {
}
