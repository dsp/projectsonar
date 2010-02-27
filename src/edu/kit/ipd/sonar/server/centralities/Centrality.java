/*
 * This file is part of Sonar.
 *$
 * This software is free software; you can redistribute it and/or$
 * modify it under the terms of the GNU Lesser General Public$
 * License version 2.1 as published by the Free Software Foundation$
 *$
 * This library is distributed in the hope that it will be useful,$
 * but WITHOUT ANY WARRANTY; without even the implied warranty of$
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU$
 * Lesser General Public License for more details.$
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.server.centralities;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The main centrality class.
 *
 * A centrality is a calculation to add weights to edges or nodes of a graph.
 * They can be used to analyze graphs.
 *<p/>
 * Sonar offers a plugin mechanism for centrality implementations. A concret
 * implementation of an algorithm to calculate weights have to implement
 * CentralityImpl.
 *<p/>
 * This class is used to transfer centrality information over the wire
 * without needing to have the actual implementations to be serializable.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class Centrality implements IsSerializable {
    /**
     * Defines possible types.
     *
     * Can easily be used to 'switch' over centrality types
     */
    public enum Type {
        /**
         * Marks that the centrality works on nodes.
         */
        NodeCentrality,

        /**
         * Marks that the centrality works on edges.
         */
        EdgeCentrality
    };

    /**
     * The name.
     */
    private String name;

    /**
     * The version.
     */
    private int version;

    /**
     * The type.
     */
    private Type type;

    /**
     * Allow packages to instantiate.
     *
     * Necessary for serialization
     */
    Centrality() {
    }


    /**
     * Instantiate an new object.
     *
     * @param name The centrality name
     * @param version The version
     * @param type The type
     */
    Centrality(final String name, final int version, final Type type) {
        this.name = name;
        this.version = version;
        this.type = type;
    }

    /**
     * Returns the centrality type.
     *
     * @return type
     */
    public Centrality.Type getType() {
        return type;
    }

    /**
     * Returns the name of the centrality.
     *
     * Every centrality has a name that is used to name
     * the centrality in the frontend.
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * The version of the centrality implementation.
     *
     * Returns the version of the Centrality. Every centrality
     * can have a positive integer as a version. It is mainly used
     * for debugging purposes and to provide information for the user.
     * It is not used in any calculation or selection.
     *
     * @return The version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Overwriten equals based on the new hash impl.
     *
     * We have to respect the new hashCode impl.
     *
     * @param o The object to compare.
     *
     * @return True if they are equal, otherwise false.
     */
    public boolean equals(final Object o) {
        if (o instanceof Centrality) {
            return o.hashCode() == hashCode();
        }

        return false;
    }

    /**
     * New hashCode impl needed to find objects after serialization.
     *
     * If we serialize an object and push it to the frontend and get it back
     * we get differnet hashCode with the Object.hashCode impl, which means
     * that we cannot compare it to our internal hashmap anymore. To be able
     * to still compare them we need to impl a hashCode that doesn't depend
     * on memory location. So we use internal data getString and getVersion
     * to ensure a stable hashCode.
     *
     * @return the hashCode.
     */
    public int hashCode() {
        return getName().concat(Integer.toString(getVersion())).hashCode();
    }
}
