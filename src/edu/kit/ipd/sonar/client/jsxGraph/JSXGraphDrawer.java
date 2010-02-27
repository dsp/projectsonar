/**
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.client.jsxGraph;

import edu.kit.ipd.sonar.client.GraphDrawer;
import edu.kit.ipd.sonar.client.DrawableGraph;
import edu.kit.ipd.sonar.client.DrawableEdge;
import edu.kit.ipd.sonar.client.DrawableNode;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class creates the whole JSXGraph environment and puts the JSXBoard into
 * the right place.
 *
 * It is also used to calculate a jsxgraph-compliant version of the graph and
 * paste's it onto the jsxboard.
 */
public class JSXGraphDrawer implements GraphDrawer {
    /** The maximum size of a point in px. */
    private static final int MAX_NODESIZE = 15;

    /** The maximum size of a point in px. */
    private static final int MIN_NODESIZE = 8;

    /** The maximum width of a line in px. */
    private static final int MAX_EDGEWIDTH = 8;

    /** The minimum width of a line in px. */
    private static final int MIN_EDGEWIDTH = 1;

    /** Width of the node border.
     * We need this to calculate proper cuttings to
     * avoid drawing out of the screen */
    private static final int NODE_BORDERWIDTH = 2;

    /** Multiplier for the radius, because (i don't know why) jsxgraph returns
     * the point radius 4 times smaller than actual size, could be a svg bug or
     * sth... */
    private static final int RADIUS_MULTIPLIER = 4;

    /** The number of pixels we should stay away from drawing borders. */
    private static final int BORDER = MAX_NODESIZE * RADIUS_MULTIPLIER
                                        + NODE_BORDERWIDTH;

    /** Number of degress the nodes should be rotated every node iteration. */
    private static final double ANGLE_OFFSET = 45;

    /** Maximum number of degrees in a circle. */
    private static final double MAX_ANGLE = 360;

    /** The JSXBoard object we are going to draw the graph upon. */
    private JSXBoard board;

    /** The DOM Element used to attach the JSXBoard. */
    private String domElementID;

    /** The drawn points. Stored for later use (maybe) */
    private ArrayList<JSXPoint> points = new ArrayList<JSXPoint>();

    /** The drawn lines. Stored for later use (maybe) */
    private ArrayList<JSXLine> lines = new ArrayList<JSXLine>();

    /** The drawn arrows. Stored for later use (maybe) */
    private ArrayList<JSXArrow> arrows = new ArrayList<JSXArrow>();

    /** JSXPoint <-> DrawableNode association. */
    private HashMap<DrawableNode, JSXPoint> nodeToPointMap;

    /** DOM_ID of the node tooltip. */
    public static final String NODE_TOOLTIP_ID = "jsxNodeTooltip";

    /** DOM_ID of the edge tooltip. */
    public static final String EDGE_TOOLTIP_ID = "jsxEdgeTooltip";

    /** Classname of the tooltip css class. */
    public static final String TOOLTIP_CLASSNAME = "jsxTooltip";

    /** Number of max notes to animate. */
    public static final int ANIMATION_TRESHOLD = 70;

    /**
     * Creates a new JSXBoard object at the given dom-id element.
     *
     * @param domElementID the element id used to attach the jsxboard
     */
    public JSXGraphDrawer(final String domElementID) {
        this.domElementID = domElementID;
    }

    /**
     * Creates a jsxgraph-compliant version of the graph including all necessary
     * coordinates and draws it on the jsxboard.
     *
     * @param graph the graph to draw
     */
    public void drawDrawableGraph(final DrawableGraph graph) {
        GWT.log("JSXGraphDrawer got graph with " + graph.getNodeList().size()
                    + " Nodes", null);
        GWT.log("JSXGraphDrawer got graph with " + graph.getEdgeList().size()
                    + " Edges", null);
        /** Hashmap to determine elements per distance. */
        HashMap<Double, Integer> elPerDist = new HashMap<Double, Integer>();
        double maxDist = 0;
        double maxSize = 0;
        double maxWidth = 0;
        double lastDistance = 0;
        double x, y;
        boolean animate = graph.getNodeList().size() <= ANIMATION_TRESHOLD;
        int pointCount = 0;
        int globalPointCount = 0;
        int paneCount = 0;

        if (board == null) {
            board = new JSXBoard(domElementID);
        }
        board.resetInfo();
        board.stopUpdate();

        /** calculates the x and y scalings to avoid out-of-screen drawing. */
        double xscaling = JSXBoard.SCALA
            - (JSXBoard.SCALA / board.getWidth()) * BORDER;
        double yscaling = JSXBoard.SCALA
            - (JSXBoard.SCALA / board.getHeight()) * BORDER;

        /** Clear the mapping list. */
        nodeToPointMap = new HashMap<DrawableNode, JSXPoint>(
            graph.getNodeList().size());

        /** Sort points by distance. */
        sortPointList(graph);

        /** Clear not needed points. */
        while (points.size() > graph.getNodeList().size()
                && points.size() > 0) {
            board.removeJSXPoint(points.get(points.size() - 1));
            points.remove(points.size() - 1);
        }

        /** Clear not needed lines. */
        while (lines.size() > 0) {
            board.removeJSXLine(lines.get(lines.size() - 1));
            lines.remove(lines.size() - 1);
        }

        /** Find out how many nodes per Distance are there. */
        for (DrawableNode node : graph.getNodeList()) {
            Integer tmp = elPerDist.get(node.getDistance());
            if (tmp == null) {
                elPerDist.put(node.getDistance(), 1);
            } else {
                elPerDist.put(node.getDistance(), tmp.intValue() + 1);
            }

            maxDist = Math.max(maxDist, node.getDistance());
            maxSize = Math.max(maxSize, node.getSize());
        }

        double xscaleDelta = xscaling / maxDist;
        double yscaleDelta = yscaling / maxDist;

        /** Cycle through the nodes to draw the points. */
        for (DrawableNode node : graph.getNodeList()) {
            if (lastDistance != node.getDistance()) {
                lastDistance = node.getDistance();
                pointCount = 0;
                paneCount++;
            }
            pointCount++;
            globalPointCount++;
            /** Normalized x/y coordinate. */
            x = normalizedX(node, elPerDist, pointCount, xscaleDelta,
                        ANGLE_OFFSET * paneCount);
            y = normalizedY(node, elPerDist, pointCount, yscaleDelta,
                        ANGLE_OFFSET * paneCount);

            int newRadius = (int) (node.getSize() / maxSize * MAX_NODESIZE);
            newRadius = Math.max(newRadius, MIN_NODESIZE);

            JSXPoint p;

            if (globalPointCount - 1 >= points.size()) {
                if (animate) {
                    p = board.createJSXPoint(
                            0,
                            0,
                            node.getColor(),
                            node.getTooltip(),
                            newRadius
                            );
                    p.draw();
                    p.setX(x);
                    p.setY(y);
                } else {
                    p = board.createJSXPoint(
                            x,
                            y,
                            node.getColor(),
                            node.getTooltip(),
                            newRadius
                            );
                }
                points.add(p);
            } else {
                p = points.get(globalPointCount - 1);
                p.setX(x);
                p.setY(y);
                p.setColor(node.getColor());
                p.setTooltip(node.getTooltip());
                p.setRadius(newRadius);
            }
            p.draw(animate);
            nodeToPointMap.put(node, p);
            lastDistance = node.getDistance();
        }

        /** Cycle through the edges to find the maxWidth. */
        for (DrawableEdge edge : graph.getEdgeList()) {
            maxWidth = Math.max(maxWidth, edge.getWidth());
        }

        double widthDelta = MAX_EDGEWIDTH / maxWidth;

        /** Cycle through the edges to draw the lines. */
        for (DrawableEdge edge : graph.getEdgeList()) {
            JSXLine line;
            int newWidth = (int) (edge.getWidth() * widthDelta);
            newWidth = Math.max(newWidth, MIN_EDGEWIDTH);

            line = board.createJSXLine(
                    nodeToPointMap.get(edge.getSource()),
                    nodeToPointMap.get(edge.getDestination()),
                    newWidth,
                    edge.getTooltip()
                    );

            lines.add(line);

            line.draw();
        }

        board.startUpdate();

        /** Counteract a bug with all infos selected on draw */
        board.resetInfo();
    }

    /**
     * This method resets the board.
     * Including the infoboxes.
     */
    public void reset() {
        if (this.board != null) {
            this.board.reset();
        }
        this.points = new ArrayList<JSXPoint>();
        this.lines = new ArrayList<JSXLine>();
        this.arrows = new ArrayList<JSXArrow>();
    }

    /*
     * Helper functions
     */

    /**
     * This function sorts the Pointlist of the graph by distance.
     *
     * @param graph the graph to sort
     */
    private void sortPointList(final DrawableGraph graph) {
        Collections.sort(graph.getNodeList(),
            new Comparator<DrawableNode>() {
                public int compare(final DrawableNode n1,
                        final DrawableNode n2) {
                    return new Double(n1.getDistance()).compareTo(
                                                    n2.getDistance());
                }
            });
    }

    /**
     * This function returns a jsxboard-normalized x coordinate.
     *
     * It tages into account which node is drawn, how many other nodes are
     * there, the maximum distance between all nodes from the center and the
     * scaling factor.
     *
     * @param node the node
     * @param elPerDist hashmap containing relation between distance an number
     * of elements at this distance
     * @param nodeNr the counter for this distance to determine how many points
     * have already been drawn at this distance
     * @param scaling the scaling multiplier to fit on the jsxboard
     * @param offsetAngle the angle the result should be offsetted with
     *
     * @return the normalized x coordinate
     */
    private double normalizedX(final DrawableNode node,
                                final HashMap<Double, Integer> elPerDist,
                                final int nodeNr,
                                final double scaling,
                                final double offsetAngle) {
        GWT.log("drawing angle: " + (MAX_ANGLE / elPerDist.get(
                            node.getDistance())) * nodeNr, null);
        return Math.cos(Math.toRadians(MAX_ANGLE / elPerDist.get(
                    node.getDistance()) * nodeNr + offsetAngle)
                ) * node.getDistance() * scaling;
    }

    /**
     * This function returns a jsxboard-normalized y coordinate.
     *
     * It tages into account which node is drawn, how many other nodes are
     * there, the maximum distance between all nodes from the center and the
     * scaling factor.
     *
     * @param node the node
     * @param elPerDist hashmap containing relation between distance an number
     * of elements at this distance
     * @param nodeNr the counter for this distance to determine how many points
     * have already been drawn at this distance
     * @param scaling the scaling multiplier to fit on the jsxboard
     * @param offsetAngle the angle the result should be offsetted with
     *
     * @return the normalized y coordinate
     */
    private double normalizedY(final DrawableNode node,
                                final HashMap<Double, Integer> elPerDist,
                                final int nodeNr,
                                final double scaling,
                                final double offsetAngle) {
        return Math.sin(Math.toRadians(MAX_ANGLE / elPerDist.get(
                    node.getDistance()) * nodeNr + offsetAngle)
                ) * node.getDistance() * scaling;
    }
}
