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
import com.google.gwt.core.client.JavaScriptException;

/**
 * This Class abstracts the points of the jsxgraph library.
 */
public class JSXPoint {
    /** The native jsxpoint we abstract. */
    private JavaScriptObject jsxpoint;

    /** The board the point is drawn on. */
    private final JSXBoard board;
    /** The x coordinate of the point. */
    private Double x;
    /** The y coordinate of the point. */
    private Double y;
    /** The radius of the point. */
    private Integer radius;
    /** The tooltip of the point. */
    private String tooltip;
    /** The color of the point. */
    private Color color;
    /** Checks if the point is already drawn. */
    private boolean drawn = false;
    /** The native jsxboard to draw on. */
    private JavaScriptObject jsxboard;

    /**
     * Constructor of the point.
     * It needs the board reference to draw itself to the board.
     *
     * @param board the board the point should be drawn on
     */
    JSXPoint(final JSXBoard board) {
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
     * This function returns the color of the point.
     *
     * @return the color of the point
     */
    Color getColor() {
        return color;
    }

    /**
     * This function returns the radius of the point.
     *
     * @return the radius
     */
    Integer getRadius() {
        return radius;
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
     * This function returns the wrapped jsobject to package classes.
     *
     * @return the wrapped js object
     */
    protected JavaScriptObject getJsxPoint() {
        return jsxpoint;
    }

    /**
     * Returns the X position of the point.
     *
     * @return the x position of the point
     */
    Double getX() {
        return x;
    }

    /**
     * Returns the Y position of the point.
     *
     * @return the y position of the point
     */
    Double getY() {
        return y;
    }

    /**
     * This function sets the color of the point.
     *
     * @param newColor the new color
     */
    void setColor(final Color newColor) {
        this.color = newColor;
    }

    /**
     * This function sets the radius of the point.
     *
     * @param newRadius the new radius
     */
    void setRadius(final Integer newRadius) {
        radius = newRadius;
    }

    /**
     * This function sets the tooltip text of the point.
     *
     * @param newTooltip the new tooltip text
     */
    void setTooltip(final String newTooltip) {
        tooltip = newTooltip;
    }

    /**
     * Sets the x position of the point.
     *
     * @param x the x position of the point
     */
    void setX(final Double x) {
        this.x = x;
    }

    /**
     * Sets the y position of the point.
     *
     * @param y the y position of the point
     */
    void setY(final Double y) {
        this.y = y;
    }

    /**
     * Moves the point to the given coordinates.
     *
     * @param newX the x position the point should be moved to
     * @param newY the y position the point should be moved to
     */
    void moveTo(final Double newX, final Double newY) {
        setX(newX);
        setY(newY);
        draw();
    }

    /**
     * This function draws the point on the board.
     *
     * @param animate whether the transition should be animated
     */
    void draw(final boolean animate) {
        if (this.color == null) {
            throw new IllegalStateException("color is null");
        } else if (this.tooltip == null) {
            throw new IllegalStateException("tooltip is null");
        } else if (this.x == null) {
            throw new IllegalStateException("x is null");
        } else if (this.y == null) {
            throw new IllegalStateException("y is null");
        } else if (this.radius == null) {
            throw new IllegalStateException("radius is null");
        }

        try {
            jsxDraw(x, y, color.getRed(), color.getGreen(),
                    color.getBlue(), radius, tooltip, drawn, animate);
            drawn = true;
        } catch (JavaScriptException e) {
            GWT.log("javascriptexception in jsxline drawing", null);
            throw e;
        }
    }

    /**
     * Simple draw wrapper.
     */
    void draw() {
        draw(false);
    }


    /*
     * JSXGraph native codes goes here
     */

    /**
     * This function is the nativ code wrapper to draw the point.
     *
     * @param xC the x coordinate of the point
     * @param yC the y coordinate of the point
     * @param red integer for color red
     * @param green integer for color green
     * @param blue integer for color blue
     * @param mRadius the radius of the point
     * @param mTooltip the tooltip of the point
     * @param mDrawn whether the point is already drawn
     * @param animate whether we animate on update, using moveTo
     */
    protected native void jsxDraw(final double xC, final double yC,
                            final int red, final int green, final int blue,
                            final int mRadius, final String mTooltip,
                            final boolean mDrawn, final boolean animate) /*-{
        if (mDrawn == false) {
            this.@edu.kit.ipd.sonar.client.jsxGraph.JSXPoint::jsxpoint =
                this.@edu.kit.ipd.sonar.client.jsxGraph.JSXPoint::jsxboard.
                    createElement('point',
                        [xC,yC],
                        {face:'O',
                        fixed:false,
                        size:mRadius,
                        strokeColor:'rgb('+red+','+green+','+blue+')',
                        fillColor:'rgb('+red+','+green+','+blue+')',
                        fillOpacity:1,
                        strokeOpacity:1,
                        name:mTooltip,
                        withLabel:false}
            );
        } else {
            var point =
                this.@edu.kit.ipd.sonar.client.jsxGraph.JSXPoint::jsxpoint;
            var brd =
                this.@edu.kit.ipd.sonar.client.jsxGraph.JSXPoint::jsxboard;

            point.name = mTooltip;
            point.setProperty("size:" + mRadius);
            point.setProperty("strokeColor:rgb("+red+","+green+","+blue+")");
            point.setProperty("fillColor:rgb("+red+","+green+","+blue+")");

            if (animate == true) {
                point.moveTo([xC, yC], 1000);
                $wnd.setTimeout(function(point) {
                    return function() {
                        point.setProperty("fixed:false")};
                }(point),1001);
            } else {
                point.setPosition($wnd.JXG.COORDS_BY_USER, xC, yC);
            }
        }
    }-*/;
}
