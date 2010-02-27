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

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents an edge between nodes of a graph.
 *
 * An edge is always directed and can be annoted as well as serialized. The edge
 * class is used by both the server and the client.
 *
 * Edges have to make sure that it's hashCode implementation returns the same
 * value before and after a serialization step, making it possible that client
 * and server refer to the same hash value
 *
 * @see Annotable
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class Edge implements Annotable, IsSerializable, Serializable {
    /**
     * The initial value of the hash from hashCode.
     */
    private static final int HASH_INIT = 5381;

    /**
     * Number to multiply the hash with.
     * 33 is the usual one used with bernsteins hash.
     */
    private static final int HASH_MULTIPLY = 33;

    /**
     * An array that holds all subscriped listener.
     */
    private transient HashSet<AnnotableListener> listeners;

    /**
     * The node where the edge points to.
     */
    private Node destinationNode;

    /**
    * The node where the edge begins.
    */
    private Node sourceNode;

    /**
    * Initial weight of the edge.
    */
    private Double originalWeight = null;

     /**
      * The creation time stored as a Unix timestamp.
      */
    private int createTime;

    /**
     * Mapping of centrality to weight for this edge.
     */
    private HashMap<Centrality, Double> weightMapping;

    /**
     * Initialize a new object.
     */
    Edge() {
        weightMapping = new HashMap<Centrality, Double>();
        listeners = new HashSet<AnnotableListener>();
    }

    /**
     * Initialize a new Edge-Object with an source- and destination-node.
     *
     * @param source The node where this edge comes from.
     * @param destination The node where this edge points to.
     */
    public Edge(final Node source, final Node destination) {
        this();
        sourceNode = source;
        destinationNode = destination;

        try {
            if (source != null) {
                source.addEdge(this);
            }
            if (destination != null) {
                destination.addEdge(this);
            }
        } catch (Exception e) {
            /* TODO: add logging */
        }
    }

    /**
     * Initialize a new object with an incoming and outgoing node and a time.
     *
     * @param source The node where this edge comes from.
     * @param destination The node where this edge points to.
     * @param time The creation time
     */
    public Edge(final Node source, final Node destination, final int time) {
        this(source, destination);
        createTime = time;
    }

    /**
     * Create a clean copy of the Edge without any nodes.
     *
     * @return The clean Edge
     */
    public Edge getCleanCopy() {
        Edge edge = new Edge(null, null, createTime);
        edge.setOriginalWeight(getOriginalWeight());

        return edge;
    }

    /**
     * Returns the source node.
     *
     * @return The source node
     */
    public Node getSourceNode() {
        return sourceNode;
    }

    /**
     * Returns the original weight from the database.
     *
     * The edges provided by the database can store an initial weight used in
     * the calculations of centralities. If no original weight is set,
     * null is returned.
     *
     * @return The weight
     */
    public Double getOriginalWeight() {
        return originalWeight;
    }

    /**
     * Returns the destination node.
     *
     * @return the destination node
     */
    public Node getDestinationNode() {
        return destinationNode;
    }

    /**
     * The creation time of the edge.
     *
     * Returns the time when the edge was created in the databasis as a unix
     * timestamp.
     *
     * @return The time
     */
    public int getTime() {
        return createTime;
    }

    /**
     * Check if this Edge is an outgoing edge of the given node.
     *
     * The comparison is based on the references, not on the content;
     *
     * @param n The node to test
     * @return True if it's an outgoing edge otherwise false
     */
    public boolean isOutgoingEdge(final Node n) {
        return (sourceNode != null && sourceNode == n);
    }

    /**
     * Check if this Edge is an incoming edge of the given node.
     *
     * The comparison is based on the references, not on the content;
     *
     * @param n The node to test
     * @return True if it's an incoming edge otherwise false
     */
    public boolean isIncomingEdge(final Node n) {
        return (destinationNode != null && destinationNode == n);
    }

    /**
     * Sets the original weight.
     *
     * The original weight is a weight that is provided by the database or any
     * other source and is constant over time. This value is not provided by
     * centralities. Centralities should use addWeight instead.
     *
     * @see Edge#addWeight(Centrality c, double weight)
     *
     * @param weight The original weight
     */
    void setOriginalWeight(final Double weight) {
        originalWeight = weight;
    }

    /**
     * Add a weight for a given centrality.
     *
     * @see Annotable#addWeight(Centrality c, double weight)
     *
     * @param c The centrality to add
     * @param weight The weight for the object
     */
    public void addWeight(final Centrality c, final double weight) {
        weightMapping.put(c, weight);
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
     * Two edges are equals if their destination and source edes match.
     *
     * @param o The object to check
     *
     * @return True if equals, otherwise false.
     */
    public boolean equals(final Object o) {
        if (o instanceof Edge) {
            Edge e = (Edge) o;
            return e.hashCode() == hashCode();
        }
        return false;
    }

    /**
     * New hashCode.
     *
     * We have to implement hashCode base on the information
     * of the nodes to which the edge belongs. We have to make sure
     * that the hashcode of two edges with the same nodes but different
     * directions are not the same.
     *
     * @return The hashCode
     */
    @Override
    public int hashCode() {
        Node source = getSourceNode();
        Node dest = getDestinationNode();
        if (source == null || dest == null) {
            /* fallback */
            return super.hashCode();
        } else {
            /* lazy impl of the bernstein DJBX33A */
            int hash = HASH_INIT;
            hash = HASH_MULTIPLY * hash + source.getId();
            hash = HASH_MULTIPLY * hash + dest.getId();
            return hash;
        }
    }

    /**
     * Returns the creation time of Edge.
     *
     * @return the creation time
     */
    private int getCreateTime() {
        return createTime;
    }

    /**
     * Sets the creation time of the Edge.
     *
     * @param createTime the time of creation
     */
    private void setCreateTime(final int createTime) {
        this.createTime = createTime;
    }

    /**
     * Sets the destination node of the edge.
     *
     * @param destinationNode the destination node.
     */
    private void setDestinationNode(final Node destinationNode) {
        this.destinationNode = destinationNode;
    }

    /**
     * Sets the source node of the edge.
     *
     * @param sourceNode the source node
     */
    private void setSourceNode(final Node sourceNode) {
        this.sourceNode = sourceNode;
    }
}
