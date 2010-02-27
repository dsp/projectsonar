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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents a time boundary.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class TimeBoundary implements IsSerializable {
    /**
     * The start of the time bound.
     */
    private int start;

    /**
     * The end of the time bound.
     */
    private int end;

    /**
     * Initialize a new object.
     *
     * @param start The start date as a unix timestamp
     * @param end The end date as a unix timestap
     */
    public TimeBoundary(final int start, final int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Initialize a new object.
     */
    protected TimeBoundary() {
    }

    /**
     * Returns the start time of the boundary.
     *
     * @return The start time
     */
    public int getStart() {
        return start;
    }

    /**
     * Returns the end time of the boundary.
     *
     * @return The end time
     */
    public int getEnd() {
        return end;
    }

    /**
     * Returns true of the given unix time lies withing
     * the boundary.
     *
     * @param time The time to check
     * @return True if within bounds, otherwise false
     */
    public boolean inBoundary(final int time) {
        return (time <= end && time >= start);
    }

    /**
     * Check if the timeboundary contains the given one.
     *
     * Checks if the interval of the given time boundary lays fully in the
     * current one.
     *
     * @param t The time boundary to compare.
     *
     * @return True if equals, otherwise false.
     */
    public boolean contains(final TimeBoundary t) {
        return t.getStart() >= getStart() && t.getEnd() <= getEnd();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TimeBoundary)) {
            return false;
        }
        TimeBoundary o = (TimeBoundary) other;
        return this.start == o.start && this.end == o.end;
    }

    @Override
    public int hashCode() {
        //Checkstyle: Start ignoring magic numbers
        // Just something so the order is important.
        return 79 * this.start + this.end;
    }
}
