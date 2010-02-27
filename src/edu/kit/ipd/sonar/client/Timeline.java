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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.dom.client.Element;
import edu.kit.ipd.sonar.server.TimeBoundary;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEvent;
import edu.kit.ipd.sonar.client.event.SuccessfulAuthenticationEventHandler;
import edu.kit.ipd.sonar.client.event.AvailableTimeBoundaryArrivedEvent;
import edu.kit.ipd.sonar.client.event.AvailableTimeBoundaryArrivedEventHandler;
import edu.kit.ipd.sonar.client.event.AvailableTimeBoundaryRequestEvent;
import edu.kit.ipd.sonar.client.event.StartLoadingEvent;
import edu.kit.ipd.sonar.client.event.FinishLoadingEvent;

/**
 * Represents a timeline on the screen, where the user can select the
 * time boundary for the data to be displayed.
 */
public class Timeline extends Composite {
    /** The ID of the Timeslider. */
    private static final String TIMELINE_ID = "timeslider";
    /** The ID of the Canvas. */
    private static final String CANVAS_ID = "canvas";
    /** The Class of the Canvas. */
    private static final String CANVAS_CLASS = "tlcanvas";
    /** Default x resoltion. */
    public static final int DEFAULT_XRES = 1000;
    /** Default y resoltion. */
    public static final int DEFAULT_YRES = 80;
    /** xresolution of the canvas. */
    private int xresolution = DEFAULT_XRES;
    /** yresolution of the canvas. */
    private int yresolution = DEFAULT_YRES;
    /** Our maximum/minimum time boundarys. */
    private TimeBoundary timeboundary;
    /** The Panel we use to order our widgets. */
    private VerticalPanel vpanel = new VerticalPanel();
    /** This values checks whether we are already injected. */
    private boolean injected = false;
    /** The canvas to draw on, as an html element. */
    private HTML canvas;
    /** Am i displaying a loading screen?. */
    private boolean isLoading = false;

    /**
     * Returns the currently selected time boundary.
     *
     * @return The selected time boundary
     */
    TimeBoundary getSelectedTimeBoundary() {
        return new TimeBoundary(
            jsGetFirst(), jsGetSecond());
    }

    /**
     * Creates the TimeLine object.
     * The TimeLine will register itself for all interesting events with
     * the event handler.
     */
    public Timeline() {
        initWidget(vpanel);
        HTML slider = new HTML("<div id='"
                        + TIMELINE_ID + "'></div>");
        vpanel.add(slider);
        vpanel.setCellWidth(slider, "100%");
        canvas = new HTML("<canvas class='" + CANVAS_CLASS + "' id='"
                + CANVAS_ID + "' height='" + yresolution + "px' "
                + "width='" + xresolution + "px' "
                + ">a</canvas>");
        vpanel.add(canvas);

        //get new timeboundarys on login
        EventBus.getHandlerManager().addHandler(
            SuccessfulAuthenticationEvent.TYPE,
            new SuccessfulAuthenticationEventHandler() {
                public void onSuccessfulAuthentication(
                    final SuccessfulAuthenticationEvent e) {
                        isLoading = true;
                        EventBus.getHandlerManager().fireEvent(
                            new StartLoadingEvent());
                        EventBus.getHandlerManager().fireEvent(
                            new AvailableTimeBoundaryRequestEvent());
                    };
            });

        //listen for new timeline
        EventBus.getHandlerManager().addHandler(
            AvailableTimeBoundaryArrivedEvent.TYPE,
            new AvailableTimeBoundaryArrivedEventHandler() {
                public void onAvailableTimeBoundaryArrived(
                    final AvailableTimeBoundaryArrivedEvent e) {
                        if (injected) {
                            updateTimeline(e.getTimeBoundary());
                        } else {
                            timeboundary = e.getTimeBoundary();
                        }
                        if (isLoading) {
                            isLoading = false;
                            EventBus.getHandlerManager().fireEvent(
                                new FinishLoadingEvent());
                        }
                };
            });

        DeferredCommand.addCommand(new Command() {
            public void execute() {
                injectSlider();
            }
        });

        Window.addResizeHandler(new ResizeHandler() {
            public void onResize(final ResizeEvent event) {
                updateCanvasScaling();
            } });
    }

    /**
     * This function injects the slider into the dom.
     * This function must not be called in the constructor, user deferred
     * commands!
     */
    private void injectSlider() {
        jsInjectSlider();
        injected = true;
    }

    /**
     * Updates the scaling of the canvas, meaning the
     * x and y resoltion.
     * We need this when resizing the window, because it
     * otherwise stretches the fonts in the canvas.
     */
    private void updateCanvasScaling() {
        if (timeboundary != null) {
            updateTimeline(timeboundary);
        }
    }

    /**
     * Update the timeline with a new timeboundary.
     *
     * @param newBoundary the new TimeBoundary
     */
    private void updateTimeline(final TimeBoundary newBoundary) {
        Integer newStart = null;
        Integer newEnd = null;
        if (this.timeboundary == null) {
            newStart = newBoundary.getStart();
            newEnd = newBoundary.getEnd();
        }
        this.timeboundary = newBoundary;

        jsUpdateTimeline(newBoundary.getStart(), newBoundary.getEnd(),
                        newStart, newEnd);
        updateTimelineDescription(newBoundary.getStart(), newBoundary.getEnd(),
                                jsGetFirst(),
                                jsGetSecond());
    }

    /**
     * Update the timeline description with new values.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @param start the first selected value
     * @param end the second selected value
     */
    private void updateTimelineDescription(
            final int min, final int max, final int start, final int end) {
        Element mycanvas = canvas.getElement().getFirstChildElement();
        xresolution = mycanvas.getClientWidth();
        yresolution = mycanvas.getClientHeight();
        mycanvas.setAttribute("width", xresolution + "px");
        mycanvas.setAttribute("height", yresolution + "px");
        this.jsUpdateTimelineDescription(min, max, start, end);
    }

    /**
     * Callback for jquery, called on slide.
     */
    private void onSlide() {
        if (this.timeboundary != null) {
            TimeBoundary selected = this.getSelectedTimeBoundary();
            updateTimelineDescription(
                    this.timeboundary.getStart(),
                    this.timeboundary.getEnd(),
                    selected.getStart(),
                    selected.getEnd());
        }
    }

    /**
     * Native code goes below.
     */

    /**
     * This method returns the first value of the timeslider.
     *
     * @return the first value of the timeslider
     */
    private native int jsGetFirst() /*-{
        var sliderid =
            @edu.kit.ipd.sonar.client.Timeline::TIMELINE_ID;
        return parseInt($wnd.$("#" + sliderid).slider("values", 0));
    }-*/;

    /**
     * This method returns the second value of the timeslider.
     *
     * @return the second value of the timeslider
     */
    private native int jsGetSecond() /*-{
        var sliderid =
            @edu.kit.ipd.sonar.client.Timeline::TIMELINE_ID;
        return parseInt($wnd.$("#" + sliderid).slider("values", 1));
    }-*/;

    /**
     * This method injects the code to create the jquery range-slider.
     * WARNING: This code has to be called _AFTER_ the Timeline constructor
     * because otherwise jquery will not be able to find our dom-element and
     * will just fail.
     */
    private native void jsInjectSlider() /*-{
        var sliderid =
            @edu.kit.ipd.sonar.client.Timeline::TIMELINE_ID;
        $wnd.$("#" + sliderid).slider({
            range: true,
            min: 0,
            max: 0,
            values: [0, 0],
            slide: function(ctx) {
                return function(event, ui) {
                    ctx.@edu.kit.ipd.sonar.client.Timeline::onSlide()();
                }
            }(this)});
    }-*/;

    /**
     * Renders the timeline description.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @param start the first selected value
     * @param end the second selected value
     */
    private native void jsUpdateTimelineDescription(
            final int min, final int max, final int start, final int end) /*-{

        var canvasid = @edu.kit.ipd.sonar.client.Timeline::CANVAS_ID;
        var xres = this.@edu.kit.ipd.sonar.client.Timeline::xresolution;
        var yres = this.@edu.kit.ipd.sonar.client.Timeline::yresolution;

        function placer(cotx,text,angle,x,y,color) {
            cotx.save();
            cotx.fillStyle = color;
            var radangle = angle * ($wnd.Math.PI / 180);
            var m11 = $wnd.Math.cos(radangle);
            var m12 = $wnd.Math.sin(radangle);
            var m21 = -$wnd.Math.sin(radangle);
            var m22 = $wnd.Math.cos(radangle);
            var dx = m11 * x + m12 * y;
            var dy = m21 * x + m22 * y;
            cotx.rotate(radangle);
            cotx.fillText(text, dx, dy);
            cotx.restore();
        }

        var context = $wnd.document.getElementById(canvasid).getContext("2d");
        context.font = "3mm helvetica";
        context.clearRect(0,0,xres,yres);

        var delta = xres/(max-min);

        var xcoordstart = delta * (start - min);

        var textwidthstart = context.measureText(start).width;

        var xcoordend = delta * (end - min);

        var textwidthend = context.measureText(end).width;

        if ( xcoordend + textwidthend > xres ) {
            xcoordend = xres - textwidthend;
            if ( xcoordend - xcoordstart < 20 ) {
                xcoordstart = xcoordend - 20;
            }
        } else if ( xcoordend - xcoordstart < 20 ) {
            xcoordend = xcoordstart + 20;
        }

        placer(context, min, 0, 5, 15,"#404040");
        placer(context, start, 30, xcoordstart, 25,"#ffffff");
        placer(context, end, 30, xcoordend, 25,"#ffffff");
        placer(context, max, 0, xres - context.measureText(max).width,
                    15,"#404040");
        }-*/;

    /**
     * This method updates the jquery range-slider with the new boundarys.
     *
     * @param min the start of the timeline
     * @param max the end of the timeline
     * @param newStart the new start value to be set
     * @param newEnd the new end value to be set
     */
    private native void jsUpdateTimeline(final int min,
            final int max, final Integer newStart, final Integer newEnd) /*-{
        var sliderid =
            @edu.kit.ipd.sonar.client.Timeline::TIMELINE_ID;

        var oldStartValue = $wnd.$("#" + sliderid).slider("values", 0);
        var oldEndValue = $wnd.$("#" + sliderid).slider("values", 1);

        if ( newStart != undefined ) {
            oldStartValue = newStart;
        }

        if ( newEnd != undefined ) {
            oldEndValue = newEnd;
        }

        // set the new min
        $wnd.$("#" + sliderid).slider("option", "min", min);

        //animate the change and adjust values if necessary
        if (oldStartValue < min) {
            $wnd.$("#" + sliderid).slider("values", 0, min, true);
        } else {
            $wnd.$("#" + sliderid).slider("values", 0, oldStartValue, true);
        }

        // set the new max
        $wnd.$("#" + sliderid).slider("option", "max", max);

        //animate the change and adjust values if necessary
        if (oldEndValue > max) {
            $wnd.$("#" + sliderid).slider("values", 1, max, true);
        } else {
            $wnd.$("#" + sliderid).slider("values", 1, oldEndValue, true);
        }
    }-*/;
}
