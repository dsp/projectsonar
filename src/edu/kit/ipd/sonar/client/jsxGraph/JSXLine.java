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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JavaScriptException;

/**
 * This Class abstracts the lines of the jsxgraph library.
 */
public class JSXLine {
    /** The jsxboard to draw on. */
    private final JSXBoard board;
    /** The source point of the line. */
    private JSXPoint source;
    /** The destination point of the line. */
    private JSXPoint destination;
    /** The tooltip of the line. */
    private String tooltip;
    /** The width of the line. */
    private int width;
    /** Checks if the point is already drawn. */
    private boolean drawn = false;
    /** The native jsxboard to draw on. */
    private JavaScriptObject jsxboard;
    /** The native jsxline we abstract. */
    private JavaScriptObject jsxline;

    /**
     * Constructor of the line.
     * It needs the board reference to draw itself to the board.
     *
     * @param board the board the point should be drawn on
     */
    JSXLine(final JSXBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("board must not be null.");
        }
        if (board.getJsxBoard() == null) {
            throw new IllegalArgumentException("board.jsxboard must not be"
                                                + " null.");
        }
        this.board = board;
        jsxboard = board.getJsxBoard();
    }

    /**
     * This function returns the source point of the line.
     *
     * @return the source point
     */
    JSXPoint getPoint1() {
        return source;
    }

    /**
     * This function returns the destination point of the line.
     *
     * @return the destination point
     */
    JSXPoint getPoint2() {
        return destination;
    }

    /**
     * This function returns the tooltip text of the point.
     *
     * @return the tooltip text
     */
    String getTooltip() {
        return tooltip;
    }

    /**
     * This function returns the width of the line.
     *
     * @return the width
     */
    int getWidth() {
        return width;
    }

    /**
     * This function returns the wrapped jsobject to package classes.
     *
     * @return the wrapped js object
     */
    protected JavaScriptObject getJsxLine() {
        return jsxline;
    }

    /**
     * This function sets the source point of the line.
     *
     * @param point the new source point
     */
    void setPoint1(final JSXPoint point) {
        source = point;
    }

    /**
     * This function sets the destination point of the line.
     *
     * @param point the new destination point
     */
    void setPoint2(final JSXPoint point) {
        destination = point;
    }

    /**
     * This function sets the tooltip text of the line.
     *
     * @param newTooltip the new tooltip text
     */
    void setTooltip(final String newTooltip) {
        tooltip = newTooltip;
    }

    /**
     * This function sets the width of the line.
     *
     * @param newWidth the new width of the line
     */
    void setWidth(final int newWidth) {
        width = newWidth;
    }

    /**
     * This function triggers the jsxgraph drawing.
     */
    void draw() {
        if (source == null) {
            throw new IllegalStateException("source is null");
        } else if (destination == null) {
            throw new IllegalStateException("destination is null");
        } else if (source.getJsxPoint() == null) {
            throw new IllegalStateException("source jsxpoint is null");
        } else if (destination.getJsxPoint() == null) {
            throw new IllegalStateException("destination jsxpoint is null");
        } else if (tooltip == null) {
            throw new IllegalStateException("tooltip is null");
        }

        try {
            jsxDraw(source.getJsxPoint(), destination.getJsxPoint(),
                    width, tooltip, drawn);
            this.drawn = true;
        } catch (JavaScriptException e) {
            GWT.log("javascriptexception in jsxline drawing", null);
            throw e;
        }
    }

    /*
     * JSXGraph native codes goes here
     */

    /**
     * This function is the nativ code wrapper to draw the line.
     * @param p1 the source point
     * @param p2 the destination point
     * @param mWidth the width of the line
     * @param mTooltip the tooltip of the line
     * @param mDrawn whether the line was already drawn
     */
    protected native void jsxDraw(final JavaScriptObject p1,
                                final JavaScriptObject p2,
                                final int mWidth,
                                final String mTooltip,
                                final boolean mDrawn) /*-{
        if (mDrawn == true) {
            var line =
                this.@edu.kit.ipd.sonar.client.jsxGraph.JSXLine::jsxline;
            var brd = this.@edu.kit.ipd.sonar.client.jsxGraph.JSXLine::jsxboard;
            brd.removeObject(line);
        }

        this.@edu.kit.ipd.sonar.client.jsxGraph.JSXLine::jsxline =
            this.@edu.kit.ipd.sonar.client.jsxGraph.JSXLine::jsxboard.
                createElement('line',
                    [p1,p2],
                    {strokeWidth:mWidth,
                    strokeColor:'grey',
                    strokeOpacity: 0.2,
                    straightFirst:false,
                    straightLast:false,
                    name:mTooltip}
                );
    }-*/;
}
