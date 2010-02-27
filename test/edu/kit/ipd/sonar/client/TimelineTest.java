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

import com.google.gwt.junit.client.GWTTestCase;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the Timeline Class.
 *
 * @author Reno Reckling <reno.reckling@web.de>
 */
public class TimelineTest extends GWTTestCase {
    
    @Override
    public String getModuleName() {
        return "edu.kit.ipd.sonar.Sonar";
    }

    /**
     * Very minimal Test.
     * We can' test any further, because the gwt junit tools didn't work well
     * with jquery.
     */
    public void testInit() {
        try {
            Timeline line = new Timeline();
        } catch (Exception e) {
            fail("Got exception: " + e.getMessage());
        }
    }
}
