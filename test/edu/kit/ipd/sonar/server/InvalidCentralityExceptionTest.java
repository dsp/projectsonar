/*
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.ipd.sonar.server;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.kit.ipd.sonar.server.centralities.Centrality;
import edu.kit.ipd.sonar.server.TestUtil;

/**
 * Tests the InvalidCentralityException-Class.
 * @author Reno Reckling <reno.reckling@web.de>
 */
public class InvalidCentralityExceptionTest {

    /**
     * Tests whether the data is stored properly.
     */
    @Test
    public void testInvalidArg() {
        Centrality cent = TestUtil.getNodeCentrality();
        InvalidCentralityException ex = new InvalidCentralityException(cent);
        try {
            throw ex;
        } catch (InvalidCentralityException e) {
            //expected
            assertSame(ex, e);
            assertSame(ex.getCentrality(), e.getCentrality());
        }
    }

}
