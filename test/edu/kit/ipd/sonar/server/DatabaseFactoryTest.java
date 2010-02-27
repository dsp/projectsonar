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

/**
 * Test class for the Database factory class.
 * @author Martin Reiche <martin.reiche@student.kit.edu>
 */
public class DatabaseFactoryTest {

    /**
     * Tests the createInstance function of the Database factory.
     */
    @Test
    public void testCreateInstance() {
        Configuration config = TestUtil.getConfiguration(
            "edu/kit/ipd/sonar/server/sampleconfig.xml");

        assertNull(DatabaseFactory.getCurrentInstance());

        Database db;
        try {
            db = DatabaseFactory.createInstance(config);
            assertNotNull(db);
            assertSame(db, DatabaseFactory.createInstance(config));
        } catch (DataException e) {
            fail();
            e.printStackTrace();
        }

    }

    /**
     * Tests the getInstance function of the database factory.
     */
    @Test
    public void testGetInstance() {
        Configuration config = TestUtil.getConfiguration(
        "edu/kit/ipd/sonar/server/sampleconfig.xml");

        try {
            DatabaseFactory.createInstance(config);
        } catch (DataException e) {
            fail();
            e.printStackTrace();
        }
        assertNotNull(DatabaseFactory.getCurrentInstance());
    }

}