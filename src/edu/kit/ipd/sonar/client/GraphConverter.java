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
package edu.kit.ipd.sonar.client;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;

import edu.kit.ipd.sonar.client.event.DrawableGraphArrivedEvent;
import edu.kit.ipd.sonar.client.event.DrawableGraphRequestEvent;
import edu.kit.ipd.sonar.client.event.DrawableGraphRequestEventHandler;
import edu.kit.ipd.sonar.client.event.ErrorOccuredEvent;
import edu.kit.ipd.sonar.client.event.ErrorOccuredEventHandler;
import edu.kit.ipd.sonar.client.event.FinishLoadingEvent;
import edu.kit.ipd.sonar.client.event.GraphArrivedEvent;
import edu.kit.ipd.sonar.client.event.GraphArrivedEventHandler;
import edu.kit.ipd.sonar.client.event.GraphRequestEvent;
import edu.kit.ipd.sonar.client.event.StartLoadingEvent;
import edu.kit.ipd.sonar.client.rpc.DrawableGraphSpecification;
import edu.kit.ipd.sonar.client.rpc.GraphSpecification;
import edu.kit.ipd.sonar.client.rpc.GraphType;

import edu.kit.ipd.sonar.server.Node;
import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.InvalidCentralityException;
import edu.kit.ipd.sonar.server.centralities.Centrality;

import java.util.HashMap;
import java.util.Map;

/**
 * A GraphConverter object converts Graphs into DrawableGraphs.
 *
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public class GraphConverter {
    // Checkstyle: This is no utility class
    
    /** Contains localized Strings. */
    private SonarMessages messages
            = (SonarMessages) GWT.create(SonarMessages.class);

    /** The last specification sent. */
    private DrawableGraphSpecification lastDGSpecification = null;

    /** The handler manager. */
    private final HandlerManager handlerManager;

    /**
     * Creates a new GraphConverter object.
     *
     * The object registers itself with the provided handler manager.
     *
     * @param handlerManager The handler manager to send and receive events.
     */
    public GraphConverter(final HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
        this.handlerManager.addHandler(DrawableGraphRequestEvent.TYPE,
                new DrawableGraphRequestEventHandler() {
                    public void onDrawableGraphRequest(
                            final DrawableGraphRequestEvent event) {
                        handleDrawableGraphRequest(
                                event.getDrawableGraphSpecification());
                    }
                });

        this.handlerManager.addHandler(GraphArrivedEvent.TYPE,
                new GraphArrivedEventHandler() {
                    public void onGraphArrived(final GraphArrivedEvent event) {
                        handleGraphArrived(event.getGraph(),
                                           event.getGraphSpecification());
                    }
                });
    }

    private ErrorOccuredEventHandler errorHandler
            = new ErrorOccuredEventHandler() {
                public void onErrorOccured(final ErrorOccuredEvent e) {
                    if (errorHandlerRegistration != null) {
                        errorHandlerRegistration.removeHandler();
                        errorHandlerRegistration = null;
                        handlerManager.fireEvent(new FinishLoadingEvent());
                    }
                }
    };

    private HandlerRegistration errorHandlerRegistration = null;

    /**
     * Handles a graph request.
     *
     * @param spec The DrawableGraphSpecification for the requested Graph.
     */
    private void handleDrawableGraphRequest(
            final DrawableGraphSpecification spec) {
        /*
         * This just asks for the Graph.
         * Future versions might implement caching here.
         */
        this.lastDGSpecification = spec;

        this.handlerManager.fireEvent(new StartLoadingEvent());
        this.errorHandlerRegistration
                = this.handlerManager.addHandler(ErrorOccuredEvent.TYPE,
                                                 errorHandler);
        this.handlerManager.fireEvent(
                new GraphRequestEvent(spec.getGraphSpecification()));
    }

    /** The size if not used as visualization. */
    private static final double DEFAULT_SIZE = 0.5;

    /** Central node visualization color. */
    private static final Color CENTRAL_NODE_COLOR = new Color(0xe9, 0x8f, 0x0);

    /** The color if not used as visualization. */
    private static final Color DEFAULT_COLOR = new Color(0x0, 0x91, 0xe5);

    /** The width if not used as visualization. */
    private static final double DEFAULT_WIDTH = 0;

    /** Saturation of the standard node color. */
    private static final float NODE_STD_SAT = 0.7f;

    /** Hue of the standard node color. */
    private static final int NODE_STD_HUE = 202;

    /** Value (lightness) of the standard node color. */
    private static final float NODE_STD_VAL = 0.25f;

    /** The variation of the nodes saturation. */
    private static final float NODE_SAT_VARIATION = 0.3f;

    /** The variation of the nodes value (lightness). */
    private static final float NODE_VAL_VARIATION = 0.5f;

    /** Number format used to format the tooltip numbers. */
    private static final NumberFormat NUMBER_FORMAT
        = NumberFormat.getFormat("0.00");

    /**
     * Handles an arriving graph.
     *
     * @param graph The arrived Graph
     * @param spec  The GraphSpecification for the arrived graph.
     */
    private void handleGraphArrived(final Graph graph,
                                    final GraphSpecification spec) {
        GWT.log("Graphconverter got graph with " + graph.getNodeList().size()
                    + " Nodes", null);
        GWT.log("Graphconverter got graph with " + graph.getEdgeList().size()
                    + " Edges", null);

        if (lastDGSpecification == null
                || lastDGSpecification.getGraphSpecification() != spec) {
            // This isn't the last graph we requested, let's ignore it.
            return;
        }

        if (graph.getNodeList().isEmpty()) {
            this.handlerManager.fireEvent(
                    new DrawableGraphArrivedEvent(null));
        }

        // At this point, we assume that the given parameters are consistent.

        HashMap<Integer, DrawableNode> nodeMap
            = new HashMap<Integer, DrawableNode>();

        for (Map.Entry<Integer, Node> entry : graph.getNodeList().entrySet()) {
            Node node = entry.getValue();

            StringBuilder tooltipSB = new StringBuilder();
            tooltipSB.append(messages.name() + ": ");
            tooltipSB.append(node.getName());

            double distance = getDefaultDistance();
            double size = DEFAULT_SIZE;
            Color color = DEFAULT_COLOR;

            // We need the index to access the visualization method,
            // so no for (Centrality : ...) loop here :(
            for (int i = 0; i < spec.getCentralities().size(); i++) {
                Centrality c = spec.getCentralities().get(i);

                if (c.getType() != Centrality.Type.NodeCentrality) {
                    continue;
                }

                double weight;
                try {
                    weight = node.getWeightForCentrality(c);
                } catch (InvalidCentralityException e) {
                    this.handlerManager.fireEvent(
                        new ErrorOccuredEvent(
                                messages.errorWhileConvertingNodes()));
                    return;
                }
                double min = graph.getCentralitiesMinWeights().get(c);
                double max = graph.getCentralitiesMaxWeights().get(c);
                double nWeight = (weight - min) / (max - min);

                tooltipSB.append("<br>&bull;&nbsp;" + c.getName() + ": ");
                tooltipSB.append(NUMBER_FORMAT.format(weight));
                tooltipSB.append(" (" + NUMBER_FORMAT.format(nWeight) + ")");

                switch (lastDGSpecification.getVisualizationMethods().get(i)) {
                    case SIZE:
                        size = nWeight;
                        break;
                    case DISTANCE:
                        if (spec.getRequestType() == GraphType.PEER_GRAPH) {
                            // Checkstyle: Start ignoring magic numbers
                            // Map the distances to 0.1 - 1.0
                            distance = (1 - (nWeight * 0.9));
                            // Checkstyle: Stop ignoring magic numbers
                        } else {
                            distance = (1 - nWeight);
                        }
                        break;
                    case COLOR:
                        color = colorFromDouble(nWeight);
                        break;
                    default:
                        this.handlerManager.fireEvent(
                            new ErrorOccuredEvent(
                                messages.errorUnknownVisualizationMethod()));
                        return;
                }
            }

            if (graph.getCentralNode() == entry.getValue()) {
                distance = 0d;
                color = CENTRAL_NODE_COLOR;
            }
            nodeMap.put(entry.getKey(),
                        new DrawableNode(tooltipSB.toString(),
                                         distance, size, color));
        }

        // Convert the edges
        ArrayList<DrawableEdge> edgeList = new ArrayList<DrawableEdge>();

        for (Edge edge : graph.getEdgeList()) {
            StringBuilder tooltipSB = new StringBuilder();
            tooltipSB.append(messages.edgeFromTo(edge.getSourceNode(),
                                                 edge.getDestinationNode()));

            double width = DEFAULT_WIDTH;

            int index = lastDGSpecification.getVisualizationMethods().
                    indexOf(VisualizationMethod.LINEWIDTH);
            if (index > -1) {
                Centrality c = spec.getCentralities().get(index);

                double weight;
                try {
                    weight = edge.getWeightForCentrality(c);
                } catch (InvalidCentralityException e) {
                    this.handlerManager.fireEvent(
                        new ErrorOccuredEvent(
                                messages.errorWhileConvertingEdges()));
                    return;
                }

                tooltipSB.append("<br>&bull;&nbsp;");
                tooltipSB.append(c.getName());
                tooltipSB.append(": " + NUMBER_FORMAT.format(weight));

                double min = graph.getCentralitiesMinWeights().get(c);
                double max = graph.getCentralitiesMaxWeights().get(c);
                if (max - min != 0) {
                    double normalizedWeight = (weight - min) / (max - min);

                    tooltipSB.append(" (");
                    tooltipSB.append(NUMBER_FORMAT.format(normalizedWeight));
                    tooltipSB.append(")");

                    width = normalizedWeight;
                }
            }

            edgeList.add(new DrawableEdge(tooltipSB.toString(), width,
                    nodeMap.get(edge.getSourceNode().getId()),
                    nodeMap.get(edge.getDestinationNode().getId())));
        }

        // Return the graph
        ArrayList<DrawableNode> nodeList
                = new ArrayList<DrawableNode>(nodeMap.values());
        this.handlerManager.fireEvent(
                new DrawableGraphArrivedEvent(new DrawableGraph(nodeList,
                                                                edgeList)));

        // We're done, stop loading now.
        errorHandlerRegistration.removeHandler();
        this.handlerManager.fireEvent(new FinishLoadingEvent());
    }

    /** The current random number to return. */
    private static double rnd = Math.random();
    /** Number of times we returned rnd. */
    private static int turn = 0;

    /** Gets a pseudo-random value for distance.
     *
     * Just looks nicer than real ones. Used when no distance is specified.
     *
     * @return A random value between 0 and 1, but repeating 4 times.
     */
    private double getDefaultDistance() {
        // Checkstyle: Start ignoring magic numbers
        if (turn == 5) {
        // Checkstyle: Stop ignoring magic numbers
            turn = 0;
            rnd = Math.random();
        }
        turn += 1;
        return rnd;
    }

    /**
     * Maps 0-1 on red-green.
     *
     * @param normalizedWeight A number between 0 and 1 inclusive.
     *
     * @return The corresponding color between dark red and light green.
     */
    private Color colorFromDouble(final double normalizedWeight) {
        /* HSV value 202 degree hue , 100% saturation and 35% value
         * is the same as RGB 0,57,89 which is our nice blue. We then
         * modify the saturation and the value (lightness). */

        /* okay don't be scared here, we just use an exponential scale to
         * make the changes between nearly identical values more visible
         * and we have the nice effects. Make sure that value is still a linear
         * scale as otherwise the changes would be too drastic and lower
         * values cannot be distinguished */
        float exp = (float) Math.exp(normalizedWeight * 2 - 2);
        return ColorUtil.transformHSVtoRGB(
                NODE_STD_HUE, NODE_STD_SAT * exp + NODE_SAT_VARIATION,
                NODE_STD_VAL + (float) normalizedWeight * NODE_VAL_VARIATION);
    }
}
