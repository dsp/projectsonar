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

import java.io.File;

/**
 * Tests the parsing of the configuration.xml-file.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class ConfigurationTest {


    /**
     * Tests if the valid test-file testconfig1.xml is interpreted correctly.
     */
    @Test
    public void testCorrectCompleteConfig() {
        Configuration config = TestUtil.getConfiguration(
                "edu/kit/ipd/sonar/server/testconfig1.xml");

        assertEquals("foobar", config.getAdminPassword());
        assertEquals("SHA-1", config.getUserPasswordHashAlgorithm());
        assertTrue(config.databaseCachingEnabled());
        assertTrue(config.calculatorCachingEnabled());
        assertEquals(getClass().getClassLoader().getResource(
                        "edu/kit/ipd/sonar/server/testconfig1.xml"),
                    config.getHibernateConfig());
        assertTrue(config.hibernateEnabled());

    }

    /**
     * Tests if the non-schema-compliant test-file testconfig2.xml
     * causes the Configuration-Class to return its default-values.
     */
    @Test
    public void testNotSchemaCompliant() {
        Configuration config = TestUtil.getConfiguration(
                "edu/kit/ipd/sonar/server/testconfig2.xml");

        assertEquals("", config.getAdminPassword());
        assertEquals("MD5", config.getUserPasswordHashAlgorithm());
        assertFalse(config.databaseCachingEnabled());
        assertFalse(config.calculatorCachingEnabled());
        assertNull(config.getHibernateConfig());
        assertFalse(config.hibernateEnabled());

    }

    /**
     * Tests if default values are returned if the config-file does not exist.
     */
    @Test
    public void testNotExistingConfig() {
        Configuration config = TestUtil.getConfiguration(
                "edu/kit/ipd/sonar/server/iDontExist.xml");

        assertEquals("", config.getAdminPassword());
        assertEquals("MD5", config.getUserPasswordHashAlgorithm());
        assertFalse(config.databaseCachingEnabled());
        assertFalse(config.calculatorCachingEnabled());
        assertNull(config.getHibernateConfig());
        assertFalse(config.hibernateEnabled());
    }

    /**
     * Tests if default values are returned if the config-file is no valid xml.
     */
    @Test
    public void testInvalidSyntax() {
        Configuration config = TestUtil.getConfiguration(
                "edu/kit/ipd/sonar/server/testconfig4.xml");

        assertEquals("", config.getAdminPassword());
        assertEquals("MD5", config.getUserPasswordHashAlgorithm());
        assertFalse(config.databaseCachingEnabled());
        assertFalse(config.calculatorCachingEnabled());
        assertNull(config.getHibernateConfig());
        assertFalse(config.hibernateEnabled());
    }

    /**
     * Tests if no options for caching are specified,
     * the default values for these options (false/false) are returned.
     */
    @Test
    public void testDefaultCachingOpts() {
        Configuration config = TestUtil.getConfiguration(
                "edu/kit/ipd/sonar/server/testconfig5.xml");

        assertEquals("SHA-1", config.getUserPasswordHashAlgorithm());

        assertFalse(config.databaseCachingEnabled());
        assertFalse(config.calculatorCachingEnabled());
    }

}
