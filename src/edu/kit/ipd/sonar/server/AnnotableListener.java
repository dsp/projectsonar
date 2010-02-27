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

import edu.kit.ipd.sonar.server.centralities.Centrality;

/**
 * Listener interface for an observer pattern to notify structures
 * that nest Annotable objects about changes in the annotable object.
 *
 * Annotable objects have to notify objects whenever a new weight was
 * added to the object. The listener interfaces defines the interface
 * use to callback the listener.
 *
 * This interface is part of an observer pattern provided by Annotable
 * and AnnotableListener.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
interface AnnotableListener {
    /**
     * Call whenever a new weight with a centrality is added to an annotable
     * object.
     *
     * @see Annotable#addWeight(Centrality c, double weight)
     *
     * @param c The added centrality
     * @param weight The added weight
     */
    void newWeightEvent(Centrality c, double weight);
}
