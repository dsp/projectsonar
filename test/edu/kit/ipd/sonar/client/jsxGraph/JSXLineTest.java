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
/**
 * Test for the JSXLine implementation.
 *
 * @author Reno Reckling
 */
public class JSXLineTest extends GWTTestCase {

    public class MyJSXBoard extends JSXBoard {
        private JavaScriptObject jso;
        public MyJSXBoard(boolean noBoard) {
            if (!noBoard) {
                jso = JavaScriptObject.createFunction();
            }
        }

        @Override
        protected JavaScriptObject getJsxBoard() {
            return jso;
        }
    }

    @Override
    public String getModuleName() {
        return "edu.kit.ipd.sonar.Sonar";
    }

    public void testNullBoard() {
        try {
            JSXBoard b = null;
            JSXLine l = new JSXLine(b);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testNulljsxboard() {
        try {
            JSXBoard board = new MyJSXBoard(true);
            JSXLine l = new JSXLine(board);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testWithoutWidth() {
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXLine l = new JSXLine(board);
            l.setPoint1(new JSXPoint(board));
            l.setPoint2(new JSXPoint(board));
            l.setTooltip("test");
            l.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testWithoutTooltip() {
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXLine l = new JSXLine(board);
            l.setPoint1(new JSXPoint(board));
            l.setPoint2(new JSXPoint(board));
            l.setWidth(2);
            l.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testWithoutP1() {
        try {
            JSXBoard board = new MyJSXBoard(false);

            JSXLine l = new JSXLine(board);
            l.setPoint1(new JSXPoint(board));
            l.setWidth(2);
            l.setTooltip("test");
            l.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testWithoutP2() {
        try {
            JSXBoard board = new MyJSXBoard(false);

            JSXLine l = new JSXLine(board);
            l.setTooltip("test");
            l.setPoint2(new JSXPoint(board));
            l.setWidth(2);
            l.setTooltip("test");
            l.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testAttributes() {
        JSXBoard board = new MyJSXBoard(false);

        JSXLine l = new JSXLine(board);
        l.setTooltip("test");
        JSXPoint p1 = new JSXPoint(board);
        JSXPoint p2 = new JSXPoint(board);
        int width = 10;
        String tooltip = "test";

        l.setPoint1(p1);
        l.setPoint2(p2);
        l.setWidth(width);
        l.setTooltip(tooltip);
        assertSame(l.getPoint1(),p1);
        assertSame(l.getPoint2(),p2);
        assertSame(l.getWidth(),width);
        assertSame(l.getTooltip(),tooltip);
    }
}

