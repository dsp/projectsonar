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

import edu.kit.ipd.sonar.client.Color;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import java.util.ArrayList;

/**
 * This class wraps around the JSXGraph javascript class JSXBoard.
 */
public class JSXBoard {

    /** The "resulution" of our jsxboard. */
    protected static final int SCALA = 1000;
    /** The javascriptobject jsx-board to make it accessible via java. */
    private JavaScriptObject jsxboard;
    /** The javascriptobject object for updating the tooltips. */
    private JavaScriptObject infoUpdater;
    /** List of all created points. */
    private ArrayList<JSXPoint> points = new ArrayList<JSXPoint>();
    /** List of all created lines. */
    private ArrayList<JSXLine> lines = new ArrayList<JSXLine>();
    /** List of all created arrows. */
    private ArrayList<JSXArrow> arrows = new ArrayList<JSXArrow>();
    /** The DOM id of our jsxboard. */
    private String domElementID;

    /**
     * Create a new JSXBoard.
     *
     * It will be inserted into the dom at the given id.
     *
     * @param newDomElementID The domid under which the element is be inserted.
     */
    JSXBoard(final String newDomElementID) {
        if (newDomElementID == null || newDomElementID == "") {
            throw new IllegalArgumentException("newDomElementID must not be "
                                                + "null.");
        }
        int top = DOM.getElementById(newDomElementID).
            getAbsoluteTop();
        int left = DOM.getElementById(newDomElementID).
            getAbsoluteLeft();
        jsxCreateBoard(newDomElementID, top, left);

        domElementID = newDomElementID;
    }

    /**
     * No-argument constructor for testing.
     * We need this constructor, because we can't test JSXPoint etc. otherwise.
     * The Problem ist, that JSXPoint etc. need a JSXBoard in their constructor
     * and we can't instanciate one with the domElementID because all calls to
     * the dom fail in unittests.
     * TODO: DO THIS BETTER IF POSSIBLE
     */
    JSXBoard() { };

    /**
     * Abstraction of an arrow from JSXGraph.
     *
     * @param p1 the first point of the arrow
     * @param p2 the second point of the arrow
     * @param width the width of the arrow
     * @param tooltip the tooltip of the arrow
     *
     * @return reference of the created line
     */
    JSXArrow createJSXArrow(final JSXPoint p1, final JSXPoint p2,
                            final int width, final String tooltip) {
        JSXArrow ret = new JSXArrow(this);
        ret.setPoint1(p1);
        ret.setPoint2(p2);
        ret.setWidth(width);
        ret.setTooltip(tooltip);
        arrows.add(ret);

        return ret;
    }

    /**
     * Abstraction of a line from JSXGraph.
     *
     * @param p1 the first point of the line
     * @param p2 the second point of the line
     * @param width the width of the line
     * @param tooltip the tooltip of the line
     *
     * @return reference of the created line
     */
    JSXLine createJSXLine(final JSXPoint p1, final JSXPoint p2,
                          final int width, final String tooltip) {
        JSXLine ret = new JSXLine(this);
        ret.setPoint1(p1);
        ret.setPoint2(p2);
        ret.setWidth(width);
        ret.setTooltip(tooltip);
        lines.add(ret);

        return ret;
    }

    /**
     * Abstraction of a point from JSXGraph.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param color the color of the point
     * @param tooltip the tooltip of the point
     * @param radius the radius of the point
     *
     * @return Reference to of the created point
     */
    JSXPoint createJSXPoint(final double x, final double y,
                            final Color color, final String tooltip,
                            final int radius) {
        JSXPoint ret = new JSXPoint(this);
        ret.setX(x);
        ret.setY(y);
        ret.setColor(color);
        ret.setTooltip(tooltip);
        ret.setRadius(radius);
        points.add(ret);

        return ret;
    }

    /**
     * Resets all Elements drawn on the JSXBoard.
     */
    void reset() {
        while (lines.size() > 0) {
            removeJSXLine(lines.get(0));
        }

        while (points.size() > 0) {
            removeJSXPoint(points.get(0));
        }

        this.resetInfo();
    }

    /**
     * Resets the infoboxes.
     * This function is mainlyy useful, if you alt-tab out auf sonar
     * or on logout, to hide the infos from previous users.
     */
    void resetInfo() {
        this.jsResetInfo();
    }

    /**
     * Removes the given arrow from the JSXBoard.
     *
     * @param a the arrow to remove
     */
    void removeJSXArrow(final JSXArrow a) {
        int index = arrows.indexOf(a);
        if (index != -1) {
            if (a.getJsxArrow() != null) {
                jsxRemoveObject(a.getJsxArrow());
            } else {
                GWT.log("WARNING: jsxline == null on removeJSXArrow", null);
            }
            arrows.remove(index);
        }
    }

    /**
     * Removes the given point from the JSXBoard.
     *
     * @param p the point to remove
     */
    void removeJSXPoint(final JSXPoint p) {
        int index = points.indexOf(p);
        if (index != -1) {
            if (p.getJsxPoint() != null) {
                jsxRemoveObject(p.getJsxPoint());
            } else {
                GWT.log("WARNING: jsxpoint == null on removeJSXLine", null);
            }
            points.remove(index);
        }
    }

    /**
     * Removes the given line from the JSXBoard.
     *
     * @param l the line to remove
     */
    void removeJSXLine(final JSXLine l) {
        int index = lines.indexOf(l);
        if (index != -1) {
            if (l.getJsxLine() != null) {
                jsxRemoveObject(l.getJsxLine());
            } else {
                GWT.log("WARNING: jsxline == null on removeJSXLine", null);
            }
            lines.remove(index);
        }
    }

    /**
     * Start the autoupdate function of the JSXBoard.
     */
    void startUpdate() {
        jsxSetUpdate(true);
    }

    /**
     * Stops the autoupdate function of the JSXBoard.
     */
    void stopUpdate() {
        jsxSetUpdate(false);
    }

    /**
     * Returns the pixel height of the jsxboard.
     *
     * @return the pixel height
     */
    int getHeight() {
        Element b = DOM.getElementById(domElementID);
        if (b != null) {
            return b.getClientHeight();
        }
        GWT.log("board element not find in getHeight", null);
        return 0;
    }

    /**
     * Returns the pixel width of the jsxboard.
     *
     * @return the pixel width
     */
    int getWidth() {
        Element b = DOM.getElementById(domElementID);
        if (b != null) {
            return b.getClientWidth();
        }
        GWT.log("board element not find in getWidth", null);
        return 0;
    }

    /**
     * Returns the wrapped jsxboard to classes within the package.
     *
     * @return the wrapped jsxboard
     */
    protected JavaScriptObject getJsxBoard() {
        return jsxboard;
    }

    /**
     * Wraps around the board-creation via JSNI.
     *
     * In this function, i remove the global jsxgraph license text,
     * because we don't want it.
     * Then we create the jsxboard in the given DOM-Element.
     * Later i override the highlight functions as suggested by jsxgraph to
     * display the tooltips.
     * Finally i wrap this board reference back into java to make it accessible
     * from within our app.
     *
     * @param domID the dom-element id
     * @param fixTop value of the jsxBoardContainer
     * @param fixLeft value of the jsxBoardContainer
     */
    private native void jsxCreateBoard(final String domID,
                                        final int fixTop,
                                        final int fixLeft) /*-{
        $wnd.JXG.JSXGraph.licenseText = "";

        var scala = @edu.kit.ipd.sonar.client.jsxGraph.JSXBoard::SCALA;
        var nodetooltipid =
            @edu.kit.ipd.sonar.client.jsxGraph.JSXGraphDrawer::NODE_TOOLTIP_ID;
        var edgetooltipid =
            @edu.kit.ipd.sonar.client.jsxGraph.JSXGraphDrawer::EDGE_TOOLTIP_ID;

        var nodeInfobox = $wnd.document.getElementById(nodetooltipid);
        var edgeInfobox = $wnd.document.getElementById(edgetooltipid);

        var infoUpdater = new function(nodebox, edgebox) {
            var self = this;
            self.edgebox = edgebox;
            self.nodebox = nodebox;
            self.edgelist = new Array();
            self.nodelist = new Array();

            self.removeFromList = function(ar,elem) {
                var i = 0;
                for(var e in ar) {
                    if(ar[e].el === elem)
                        ar.splice(i,1);
                    i++;
                }
            }

            self.addBR = function(string) {
                if(string.substring(string.length - 4) != "<br>"
                        && string.substring(string.length - 5) != "<br/>"
                        && string.substring(string.length - 6) != "<br />")
                    string += "<br>";
                return string;
            }

            this.addNodeinfo = function(newel, newtext) {
                self.nodelist.push({el:newel, text:newtext});
                self.nodeUpdate();
            }

            this.removeNodeinfo = function(el) {
                self.removeFromList(self.nodelist, el);
                self.nodeUpdate();
            }

            this.addEdgeinfo = function(newel, newtext) {
                self.edgelist.push({el:newel, text:newtext});
                self.edgeUpdate();
            }

            this.reset = function() {
                self.nodelist = new Array();
                self.edgelist = new Array();
                self.nodebox.innerHTML = "";
                self.edgebox.innerHTML = "";
            }

            this.removeEdgeinfo = function(el) {
                self.removeFromList(self.edgelist, el);
                self.edgeUpdate();
            }

            self.edgeUpdate = function() {
                self.edgebox.innerHTML = "";
                for(var el in self.edgelist) {
                    self.edgebox.innerHTML += self.addBR(
                                    self.edgelist[el].text);
                }
                self.edgebox.innerHTML += "<br>";
            }

            self.nodeUpdate = function() {
                self.nodebox.innerHTML = "";
                for(var el in self.nodelist) {
                    self.nodebox.innerHTML += self.addBR(
                                    self.nodelist[el].text);
                }
                self.nodebox.innerHTML += "<br>";
            }
        }(nodeInfobox, edgeInfobox);

        this.@edu.kit.ipd.sonar.client.jsxGraph.JSXBoard::infoUpdater =
            infoUpdater;

        $wnd.JXG.Line.prototype.highlight = function(){
            infoUpdater.addEdgeinfo(this, this.name);
            this.board.renderer.highlight(this); // highlight the line
        }

        $wnd.JXG.Line.prototype.noHighlight = function(){
            infoUpdater.removeEdgeinfo(this);
            this.board.renderer.noHighlight(this); // dehighlight the line
        }

        $wnd.JXG.Point.prototype.highlight = function(){
            infoUpdater.addNodeinfo(this, this.name);
            this.board.renderer.highlight(this); // highlight the line
        }

        $wnd.JXG.Point.prototype.noHighlight = function(){
            infoUpdater.removeNodeinfo(this);
            this.board.renderer.noHighlight(this); // dehighlight the line
        }

        this.@edu.kit.ipd.sonar.client.jsxGraph.JSXBoard::jsxboard =
            $wnd.JXG.JSXGraph.initBoard(domID,
                {boundingbox:[-scala,scala,scala,-scala]});
    }-*/;

    /**
     * This method is a native wrapper to delete a object from the jsxboard.
     *
     * @param obj the jsxobject to remove
     */
    private native void jsxRemoveObject(final JavaScriptObject obj) /*-{
        var board = this.@edu.kit.ipd.sonar.client.jsxGraph.JSXBoard::jsxboard;
        board.removeObject(obj);
    }-*/;

    /**
     * This method is a native wrapper to start and stop the jsxboard updating.
     *
     * @param update 1 to start updating, 0 to stop it
     */
    private native void jsxSetUpdate(final boolean update) /*-{
        var board = this.@edu.kit.ipd.sonar.client.jsxGraph.JSXBoard::jsxboard;
        if (update) {
            board.unsuspendUpdate();
        } else {
            board.suspendUpdate();
        }
    }-*/;

    /**
     * This method resets the info texts in the infoupdater object.
     */
    private native void jsResetInfo() /*-{
        var infoUpdater =
            this.@edu.kit.ipd.sonar.client.jsxGraph.JSXBoard::infoUpdater;
        infoUpdater.reset();
    }-*/;
}
