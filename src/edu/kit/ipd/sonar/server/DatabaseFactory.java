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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database Factory. Handles the creation of database connections.
 *
 * @author Martin Reiche <martin.reiche@student.kit.edu>
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public final class DatabaseFactory {

    /**
     * The database most currently "produced" by the factory.
     */
    private static Database currentDb = null;

    /**
     * The logger for error logging.
     */
    private static Logger log = LoggerFactory.getLogger(DatabaseFactory.class);

    /**
     * Returns an Instance of a Database as specified within the configuration.
     *
     * @param config
     *            The configuration file to load the Database instance with
     * @return A valid Database object. If Hibernate is activated, this is a
     *         HibernateDatabase object
     */
    static synchronized Database createInstance(final Configuration config)
        throws DataException {

         if (config.hibernateEnabled()) {
            currentDb = getHibernateDatabase(config);
            return currentDb;

         // if other Database types than hibernate are supported,
         // check for them here and instantiate them if needed.

         } else {
             log.error("Database Connection without Hibernate is not yet"
                        + "implemented. Abort.");
             return null;
         }
    }

    /** The Instance of HibernateDatabase held by the factory. */
    private static Database hibernateDbInstance = null;

    /**
     * Provides synchronized Singleton-access to the
     * instance of the hibernateDatabase.
     *
     * @param config The Configuration-object with the current preferences.
     * @return The Instance of the HibernateDatabase used.
     */
    private static synchronized Database getHibernateDatabase(
                                         final Configuration config)
        throws DataException {
        if (hibernateDbInstance == null) {
            HibernateUtil.startup(config);
            hibernateDbInstance = new HibernateDatabase();
        }
        return hibernateDbInstance;
    }


    /**
     * Returns the current Database instance.
     * Might be null if no instance has been
     * created yet or the creation failed.
     *
     * @return the current database. Might be NULL!
     */
    public static Database getCurrentInstance() {
        return currentDb; // might be null
    }

    /** Private constructor as this class has only static methods. */
    private DatabaseFactory() {
    }
}
