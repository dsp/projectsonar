/*
 * This file is part of Sonar.
 *
 * This software is free software; you can redistribute it and/or$
 * modify it under the terms of the GNU Lesser General Public$
 * License version 2.1 as published by the Free Software Foundation$
 *
 * This library is distributed in the hope that it will be useful,$
 * but WITHOUT ANY WARRANTY; without even the implied warranty of$
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU$
 * Lesser General Public License for more details.$
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.server;

import edu.kit.ipd.sonar.server.centralities.Centrality;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a node in a graph.
 *
 * A node is connected to other nodes by an Edge and
 * is part of a Graph.
 *
 * Nodes have to make sure that it's hashCode implementation returns the same
 * value before and after a serialization step, making it possible that client
 * and server refer to the same hash value
 *
 * @see Annotable
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class Node implements Annotable, IsSerializable {
    /**
     * An array that holds all subscriped listener.
     */
    private transient HashSet<AnnotableListener> listeners;
    /**
     * An array of edges connected with this node.
     */
    private HashSet<Edge> edges;

    /**
     * The id of the node which identified the node
     * in the database.
     */
    private int id = -1;

    /**
     * The name of the node as stored in the databse.
     * Usually used in the frontend
     */
    private String name;

    /**
     * The creation time stored as a Unix timestamp.
     */
    private int createTime;

    /**
     * Initial weight of the edge.
     */
     private Double originalWeight = null;

    /**
     * Mapping of centrality to weight for this node.
     */
    private HashMap<Centrality, Double> weightMapping;

    /**
     * Initialize a new Object.
     */
    Node() {
        weightMapping = new HashMap<Centrality, Double>();
        listeners = new HashSet<AnnotableListener>();
        edges = new HashSet<Edge>();
    }

    /**
     * Initialize a new Object with a given id, name and creationTime.
     *
     * @param id The id of the node from the databasis
     * @param name The name of the node to be display on the frontend
     * @param time The creation time as a Unix timestamp
     */
    public Node(final int id, final String name, final int time) {
        this();
        this.id = id;
        this.name = name;
        this.createTime = time;
    }

    /**
     * Initialize a new object with a given set of Edges.
     *
     * @param id The id of the node from the databasis
     * @param name The name of the node to be display on the frontend
     * @param time The creation time as Unix timestamp
     * @param edges Initial edges
     */
    public Node(final int id, final String name, final int time,
        final Edge[] edges) {
        this(id, name, time);
        this.edges = new HashSet<Edge>(Arrays.asList(edges));
    }

    /**
     * Add a new edge.
     *
     * @param e The edge to add
     * @throws NodeDoesNotExistException If the edge doesn't contain this node.
     */
    public void addEdge(final Edge e) throws NodeDoesNotExistException {
        if (!e.isOutgoingEdge(this) && !e.isIncomingEdge(this)) {
            throw new NodeDoesNotExistException();
        }
        edges.add(e);
    }

    /**
     * Create a clean copy of the Node without any edges.
     *
     * Clean copies are used to generate a new instance of the node with
     * the same id, name and create time.
     *
     * @return A clean copy of the node
     */
    public Node getCleanCopy() {
        return new Node(id, name, createTime);
    }

    /**
     * Return the edges connected with this node.
     *
     * @return A set of edges
     */
    public Set<Edge> getEdges() {
        return edges;
    }

    /**
     * Return the Node id provided by the database to identify the node.
     *
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * Return the name of the node.
     *
     * The name of the node is just used in the frontend to give the user
     * a user readable name of the node. This can for example be the name of a
     * player in a social network.
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * The creation time of the node.
     *
     * Returns the time when the node was created in the database as a UNIX
     * timestamp.
     *
     * @return The time
     */
    public int getTime() {
        return createTime;
    }

    /**
     * Add a weight for a given centrality.
     *
     * @see Annotable#addWeight(Centrality c, double weight)
     *
     * @param c The centrality to add
     * @param weight The weight for the object
     */
    public void addWeight(final Centrality c, final Double weight) {
        weightMapping.put(c, weight);
        /* notify */
        for (AnnotableListener l : listeners) {
            l.newWeightEvent(c, weight);
        }
    }

    /**
     * Returns a mapping between defined centralities of this annotable
     * and their value.
     *
     * @see Annotable#getCentralities()
     *
     * @return A mapping of centralities to their values.
     */
    public HashMap<Centrality, Double> getCentralities() {
        return weightMapping;
    }

    /**
     * Returns the weight of this node for a given centrality.
     *
     * @see Annotable#getWeightForCentrality(Centrality c)
     *
     * @param c The requested centrality for which the weight should
     *          be returned
     * @throws InvalidCentralityException Thrown if the given centrality is
     * @return The weight
     */
    public double getWeightForCentrality(final Centrality c)
        throws InvalidCentralityException {
        if (weightMapping.containsKey(c)) {
            return weightMapping.get(c).doubleValue();
        }

        throw new InvalidCentralityException(c);
    }

    /**
     * Add a listener to the observer pattern described
     * in Annotable#addListener.
     *
     * @see Annotable#addListener(AnnotableListener listener)
     *
     * @param listener The listener implementation.
     */
    public void addListener(final AnnotableListener listener) {
        listeners.add(listener);
    }

    /**
     * If two nodes have the same id they are equal.
     *
     * @param o The object to check
     *
     * @return True of equals, otherwise false.
     */
    public boolean equals(final Object o) {
        if (o instanceof Node) {
            Node n = (Node) o;
            return n.getId() == getId();
        }
        return false;
    }

    /**
     * Overwrite hashCode.
     *
     * Two nodes are equal if their id is the same.
     *
     * @return The hashCode
     */
    public int hashCode() {
        if (id < 0) {
            return super.hashCode();
        }

        return id;
    }

    /**
     * Sets the original weight.
     *
     * @param weight The original weight
     */
    void setOriginalWeight(final Double weight) {
        originalWeight = weight;
    }

    /**
     * Returns the original weight from the database
     *
     * The edges provided by the database can store an initial weight used in
     * the calculations of centralities.
     * If no original weight is set, null is returned.
     *
     * @return The weight
     */
    public Double getOriginalWeight() {
        return originalWeight;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Returns the creation time of the node.
     * @return creation time
     */
    private int getCreateTime() {
        return createTime;
    }

    /**
     * Sets the creation time for the node.
     * @param createTime The creation time.
     */
    private void setCreateTime(final int createTime) {
        this.createTime = createTime;
    }

    /**
     * Sets the id of the Node.
     * @param id node ID
     */
    private void setId(final int id) {
        this.id = id;
    }

    /**
     * Sets the Name of the Node.
     * @param name name of the node
     */
    private void setName(final String name) {
        this.name = name;
    }
}
