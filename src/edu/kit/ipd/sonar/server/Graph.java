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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The internally used structure to represent a graph independently
 * from the Data Source.
 *
 * Every graph consist of edges and nodes, with nodes connected to
 * each other by an edge.  The graph also maintains the list of nodes
 * and edges in the graph as well as their maximum time boundary.
 * Adding nodes should only be done using the graph object. The graph
 * is append only.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class Graph implements IsSerializable, AnnotableListener {
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
     * Current time boundary.
     */
    private TimeBoundary timeBound;

    /**
     * A list of centralities used in the graph.
     */
    private HashSet<Centrality> centralityList;

    /**
     * Tracks the mapping between the current minimum value
     * for a centrality.
     */
    private HashMap<Centrality, Double> minCentrality;

    /**
     * Tracks the mapping between the current maximum value
     * for a centrality.
     */
    private HashMap<Centrality, Double> maxCentrality;

    /**
     * Internal edge list.
     */
    private Set<Edge> edgeList;
    /**
     * Internal node list.
     */
    private HashMap<Integer, Node> nodeList;

    /**
     * The central node of the graph.
     * Can be null
     */
    private Node centralNode;

    /**
     * Initialize a new object.
     */
    Graph() {
        minCentrality = new HashMap<Centrality, Double>();
        maxCentrality = new HashMap<Centrality, Double>();
        edgeList = new HashSet<Edge>();
        nodeList = new HashMap<Integer, Node>();
        centralityList = new HashSet<Centrality>();
    }

    /**
     * Adds an edge to the graph.
     *
     * Both nodes have to be part of the graph already.
     *
     * @param edge The edge to be added to the graph
     */
    public void addEdge(final Edge edge) {
        if (!nodeList.containsValue(edge.getSourceNode())) {
            addNode(edge.getSourceNode());
        }

        if (!nodeList.containsValue(edge.getDestinationNode())) {
            addNode(edge.getDestinationNode());
        }

        edgeList.add(edge);
        updateTimeBound(edge.getTime());
        updateCentralities(edge.getCentralities());
        listenOnAnnotable(edge);
    }

    /**
     * Adds a new node to the graph.
     *
     * If the node exists, nothing happens.
     *
     * @param node The node to add.
     */
    public void addNode(final Node node) {
        nodeList.put(node.getId(), node);
        updateTimeBound(node.getTime());
        updateCentralities(node.getCentralities());
        listenOnAnnotable(node);
    }

    /**
     * Returns a list of used nodes in the graph.
     *
     * The returned HashMap will have the Ids of the nodes, as
     * provided by the database, as keys and the nodes itself as values.
     *
     * @return The mapped node list
     */
    public HashMap<Integer, Node> getNodeList() {
        return nodeList;
    }

    /**
     * Returns the node identified by the given id.
     *
     * @param id The id of the node to return
     * @throws NodeDoesNotExistException if the given node doesn't exists.
     * @return the node or null if it doesn't exist
     */
    public Node getNodeById(final Integer id) throws NodeDoesNotExistException {
        Node n = nodeList.get(id);
        if (null == n) {
            throw new NodeDoesNotExistException();
        }

        return n;
    }

    /**
     * Returns a set of edges that are in the graph.
     *
     * @return The set of edges
     */
    public Set<Edge> getEdgeList() {
        return edgeList;
    }

    /**
     * Returns a hash that identifies the change state of the Graph. Therefore
     * uses overridden hashCode implementation.
     * @return hash value
     */
    public int getStateHash() {
        return this.hashCode();
    }

    /**
     * If two graphs have the same hashcode they are equal.
     *
     * @param o The object to check
     *
     * @return True of equals, otherwise false.
     */
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Graph) {
            Graph n = (Graph) o;
            return n.hashCode() == this.hashCode();
        }
        return false;
    }

    /**
     * Overwrite hashCode.
     *
     * Two graphs are equal if and only if they share 100% the same edges and
     * nodes.
     *
     * NOTE: This hash function is straight forward. It may not work properly
     * in special use cases.
     *
     * @return The hashCode
     */
    @Override
    public int hashCode() {
        int hash = HASH_INIT;

        /* acquiring all edges, hash them and add */
        for (Edge e : edgeList) {
            hash += HASH_MULTIPLY * hash + e.hashCode();
        }

        /* acquire nodes, hash indirectly and add */
        for (Node n : nodeList.values()) {
            hash += HASH_MULTIPLY * hash + n.getId();
        }

        return hash;
    }

    /**
     * Returns a set of all used centralities in the annotable objects
     * of a Graph.
     *
     * @return A set of centralities.
     */
    public Set<Centrality> getCentralities() {
        return centralityList;
    }

    /**
     * Return a mapping between centralities of the graph and their maximum
     * values within the graph.
     *
     * @return The mapping
     */
    public HashMap<Centrality, Double> getCentralitiesMaxWeights() {
        return maxCentrality;
    }

    /**
     * Return a mapping between centralities of the graph and their minimum
     * values within the graph.
     *
     * @return The mapping
     */
    public HashMap<Centrality, Double> getCentralitiesMinWeights() {
        return minCentrality;
    }

    /**
     * Returns the time interval in which all nodes and edges of the graph are.
     *
     * @return A time boundary
     */
    public TimeBoundary getMaxTimeBoundary() {
        return timeBound;
    }

    /**
     * Returns the node of the graph that should be displayed central.
     *
     * @return The central node of the graph
     */
    public Node getCentralNode() {
        return centralNode;
    }

    /**
     * Set the central node of the graph.
     *
     * A graph can contain a central node for a special cases of graphs (peer
     * graphs). The central node provided by the database, but rather set by the
     * calculators.
     *
     * @param node The node
     *
     * @throws NodeDoesNotExistException if the provided node is not null and
     * does not exists.
     */
    void setCentralNode(final Node node) throws NodeDoesNotExistException {
        if (null != node && !nodeList.containsValue(node)) {
            throw new NodeDoesNotExistException();
        }

        centralNode = node;
    }

    /**
     * Update the max time boundary based on the given time.
     *
     * The method will update the max time boundary if the given
     * point in time is earlier or later than the current max time boundary
     *
     * @param time The time to compare the current boundary with
     */
    private void updateTimeBound(final int time) {
        if (null == timeBound) {
            /* initialize the time bound. We cannot do this before. */
            timeBound = new TimeBoundary(time, time);
        } else {
            if (time < timeBound.getStart()) {
                timeBound = new TimeBoundary(time, timeBound.getEnd());
            } else if (time > timeBound.getEnd()) {
                timeBound = new TimeBoundary(timeBound.getStart(), time);
            }
        }
    }

    /**
     * Update the min/max centrality value of the graph based on the
     * given centrality, value mapping.
     *
     * @see updateCentralityBounds
     * @param map The mapping
     */
    private void updateCentralities(final HashMap<Centrality, Double> map) {
        centralityList.addAll(map.keySet());
        for (Map.Entry<Centrality, Double> e : map.entrySet()) {
            updateCentralityBounds(e.getKey(), e.getValue());
        }
    }

    /**
     * Updates the tracked max/min value of the given Centrality if necessary.
     *
     * The method checks if the current tracked maximum value for the
     * given centrality is set or less/greater than the given double.
     * Then it updates it the max/min value. Max/min values are tracked
     * per centrality. There is no overall max/min value.
     *
     * @param c The centrality to check for.
     * @param d The value
     */
    private void updateCentralityBounds(final Centrality c, final Double d) {
        if (minCentrality.containsKey(c)) {
            if (minCentrality.get(c) > d) {
                minCentrality.put(c, d);
            }
        } else {
            minCentrality.put(c, d);
        }

        if (maxCentrality.containsKey(c)) {
            if (maxCentrality.get(c) < d) {
                maxCentrality.put(c, d);
            }
        } else  {
            maxCentrality.put(c, d);
        }
    }

    /**
     * Implements an AnnotableListener.
     *
     * Update our centrality list and update the maximum and minimum values of
     * weights whenever we get notified by an annotable object.
     *
     * @param centrality the centrality to add the weight zu
     * @param weight the new weight
     */
    public void newWeightEvent(final Centrality centrality,
            final double weight) {
        centralityList.add(centrality);
        updateCentralityBounds(centrality, weight);
    }

    /**
     * Add a listener to an annotable that keeps track of a newWeightEvent.
     *
     * @param a The annotable
     */
    private void listenOnAnnotable(final Annotable a) {
        a.addListener(this);
    }
}
