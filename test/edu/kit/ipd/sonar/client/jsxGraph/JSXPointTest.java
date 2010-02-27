/*
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

import org.junit.*;
import static org.junit.Assert.*;

import edu.kit.ipd.sonar.client.Color;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;

/**
 * Test for the JSXPoint implementation.
 *
 * @author Reno Reckling
 */
public class JSXPointTest extends GWTTestCase {

    public class MyJSXBoard extends JSXBoard {
        private JavaScriptObject jso;
        public MyJSXBoard(boolean noBoard) {
            if(!noBoard) {
                jso = JavaScriptObject.createFunction();
            }
        }

        @Override
        protected JavaScriptObject getJsxBoard() {
            return jso;
        }
    }

    private static final String BOARDID = "jsxboard";

    @Override
    public String getModuleName() {
        return "edu.kit.ipd.sonar.Sonar";
    }

    public void testNullBoard() {
        try {
            JSXBoard b = null;
            JSXPoint p = new JSXPoint(b);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testNulljsxboard() {
        try {
            JSXBoard board = new MyJSXBoard(true);
            JSXPoint p = new JSXPoint(board);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testDraw() {
        // without color
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXPoint p = new JSXPoint(board);
            p.setTooltip("test");
            p.setX(new Double(10));
            p.setY(new Double(10));
            p.setRadius(new Integer(4));
            p.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }

        // without tooltip
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXPoint p = new JSXPoint(board);
            p.setColor(new Color(0,0,0));
            p.setX(new Double(10));
            p.setY(new Double(10));
            p.setRadius(new Integer(4));
            p.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }

        // without radius
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXPoint p = new JSXPoint(board);
            p.setColor(new Color(0,0,0));
            p.setTooltip("test");
            p.setX(new Double(10));
            p.setY(new Double(10));
            p.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }

        // without x
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXPoint p = new JSXPoint(board);
            p.setColor(new Color(0,0,0));
            p.setTooltip("test");
            p.setY(new Double(10));
            p.setRadius(new Integer(4));
            p.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }

        // without y
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXPoint p = new JSXPoint(board);
            p.setColor(new Color(0,0,0));
            p.setTooltip("test");
            p.setX(new Double(10));
            p.setRadius(new Integer(4));
            p.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }

        // without x+y
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXPoint p = new JSXPoint(board);
            p.setColor(new Color(0,0,0));
            p.setTooltip("test");
            p.setRadius(new Integer(4));
            p.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testAttributes() {
        JSXBoard board = new MyJSXBoard(false);
        JSXPoint p = new JSXPoint(board);
        String tip = "test";
        Color c = new Color(0,0,0);
        Double x = new Double(10);
        Double y = new Double(10);
        Integer rad = new Integer(4);
        p.setTooltip(tip);
        p.setColor(c);
        p.setX(x);
        p.setY(y);
        p.setRadius(rad);
        assertSame(tip,p.getTooltip());
        assertSame(c,p.getColor());
        assertSame(x,p.getX());
        assertSame(y,p.getY());
        assertSame(rad,p.getRadius());
    }
}

