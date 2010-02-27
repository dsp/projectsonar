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
 * Test for the JSXArrow implementation.
 *
 * @author Reno Reckling
 */
public class JSXArrowTest extends GWTTestCase {

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
            JSXArrow a = new JSXArrow(b);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testNulljsxboard() {
        try {
            JSXBoard board = new MyJSXBoard(true);
            JSXArrow a = new JSXArrow(board);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testWithoutWidth() {
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXArrow a = new JSXArrow(board);
            a.setPoint1(new JSXPoint(board));
            a.setPoint2(new JSXPoint(board));
            a.setTooltip("test");
            a.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testWithoutTooltip() {
        try {
            JSXBoard board = new MyJSXBoard(false);
            JSXArrow a = new JSXArrow(board);
            a.setPoint1(new JSXPoint(board));
            a.setPoint2(new JSXPoint(board));
            a.setWidth(2);
            a.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testWithoutP1() {
        try {
            JSXBoard board = new MyJSXBoard(false);

            JSXArrow a = new JSXArrow(board);
            a.setPoint1(new JSXPoint(board));
            a.setWidth(2);
            a.setTooltip("test");
            a.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testWithoutP2() {
        try {
            JSXBoard board = new MyJSXBoard(false);

            JSXArrow a = new JSXArrow(board);
            a.setTooltip("test");
            a.setPoint2(new JSXPoint(board));
            a.setWidth(2);
            a.setTooltip("test");
            a.draw();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testAttributes() {
        JSXBoard board = new MyJSXBoard(false);

        JSXArrow a = new JSXArrow(board);
        a.setTooltip("test");
        JSXPoint p1 = new JSXPoint(board);
        JSXPoint p2 = new JSXPoint(board);
        int width = 10;
        String tooltip = "test";

        a.setPoint1(p1);
        a.setPoint2(p2);
        a.setWidth(width);
        a.setTooltip(tooltip);
        assertSame(a.getPoint1(),p1);
        assertSame(a.getPoint2(),p2);
        assertSame(a.getWidth(),width);
        assertSame(a.getTooltip(),tooltip);
    }
}

