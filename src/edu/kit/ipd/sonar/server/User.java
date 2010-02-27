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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Class containing data of one user.
 *
 * @author Reno Reckling
 */
public class User implements IsSerializable {

    /** The userid of the user. */
    private Integer userid;

    /** The username of the user. */
    private String name;

    /**
     * Initializes a new user.
     *
     * @param intid the id
     * @param name the username
     * @throws IllegalArgumentException
     */
    public User(final Integer intid, final String name) {
        if (intid == null) {
            throw new IllegalArgumentException("userid must not be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }

        this.userid = intid;
        this.name = name;
    }

    /**
     * Default constructor.
     */
    protected User() { }

    /**
     * Returns the name.
     *
     * @return the username
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the userid.
     * @return the user id
     */
    private Integer getUserid() {
        return userid;
    }

    /**
     * Sets the user id.
     * @param userid user ID
     */
    private void setUserid(final Integer userid) {
        this.userid = userid;
    }

    /**
     * Sets the user name.
     * @param name username
     */
    private void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the userid.
     *
     * @return the userid
     */
    public Integer getId() {
        return this.userid;
    }

    /**
     * Two users are equal if their userids are equal.
     * @param o the user to test for equality
     * @return true if equal, false else
     */
    @Override
    public boolean equals(final Object o) {
        if (o instanceof User) {
            User u = (User) o;
            return this.hashCode() == u.hashCode();
        }
        return false;
    }

    /**
     * Returns the userid as hash code.
     * @return hash value
     */
    public int hashCode() {
        return userid;
    }
}
