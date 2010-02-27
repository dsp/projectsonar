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

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Database connection Interface.
 *
 * This interface describe the methods needed by Sonar to access any database.
 * Sonar doesn't need to be used together with a relational database, but the
 * every database used needs to make sure that it provides a way to generate
 * graphs and authenticate users.
 *
 * @author Martin Reiche <martin.reiche@student.kit.edu>
 */
interface Database {
    /**
     * Returns the graph from the data source.
     *
     * @throws DataException
     *             if error while connecting to Database
     * @return the graph from the database
     */
    Graph getGraph() throws DataException;

    /**
     * Authenticates a user at the Database.
     *
     * @param username
     *            The username of the user to be authenticated
     * @param password
     *            The password of the specified user
     * @throws DataException
     *             if error while connecting to Database
     * @return the User that could successfully be authenticated at the database
     *         if no authentication was possible, NULL is returned.
     */
    User authenticate(String username, String password) throws DataException;

    /**
     * Returns an array list of all Users that can authenticate at the database.
     *
     * @throws DataException
     *             if error while connecting to database.
     * @return array list of all users
     */
    ArrayList<User> getUserList() throws DataException;

    /**
     * Returns the data base connection on which the data base subsystem builds
     * its queries.
     *
     * @return the underlying data base connection as a java.sql.Connection
     *         objec
     * @throws DataException
     *             if the underlying data base connection could not be accessed
     */
    Connection getUnderLyingConnection() throws DataException;
}
