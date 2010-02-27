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

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate Utility Class Holds the Hibernate Sessions and controls access to
 * the hibernate database connection.
 *
 * @author Martin Reiche <martin.reiche@student.kit.edu>
 */
public final class HibernateUtil {

    /**
     * The hibernate specific configuration object.
     */
    private static org.hibernate.cfg.Configuration configuration;

    /**
     * The session factory for hibernate.
     */
    private static SessionFactory sessionFactory;

    /**
     * The logger for error logging.
     */
    private static Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    /**
     * Make sure a utility class cannot be instantiated.
     */
    private HibernateUtil() {
    }

    /**
     * Starts Hibernate.
     *
     * Be sure to startup Hibernate before acting on it! This is crucial!
     *
     * @param sonarCfg
     *            The Sonar configuration object.
     */
    static void startup(final Configuration sonarCfg) {
        try {
            log.debug("Initializing Hibernate");

            if (!sonarCfg.hibernateEnabled()) {
                log.debug("Hibernate is not enabled in the configuration. "
                        + "It will not be started.");
            } else {
                configuration = new org.hibernate.cfg.Configuration();
                configuration.configure(sonarCfg.getHibernateConfig());
                

                log.debug("Hibernate has been configured. Now try "
                        + "building Session Factory");

                /* acquiring hibernate config is complete, restart sessions */
                rebuildSessionFactory(configuration);

                log.debug("Hibernate startup has been completed.");
            }
        } catch (Throwable ex) {
            log.error("Starting up Hibernate failed while building the "
                    + "Session Factory: " + ex.getMessage());
            throw new HibernateException(
                    "SessionFactory could not be created: " + ex.getMessage());
        }

    }

    /**
     * Startup wrapper without any arguments will load the configuration
     * provided by the Sonar configuration file.
     *
     * @see HibernateUtil#startup(Configuration)
     */
    static void startup() {
        /* retrieving Sonar configuration object */
        log.debug("Not loading from external Configuration.");
        Configuration sonarCfg = Configuration.getInstance();
        startup(sonarCfg);
    }

    /**
     * Returns the previously created Hibernate configuration object.
     *
     * @return the Hibernate configuration objec
     */
    static org.hibernate.cfg.Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Rebuilds session factory. Closes old session factory and creates a new
     * one depending on a new hibernate configuration file.
     *
     * @param config
     *            new configuration for the session factory
     */
    static void rebuildSessionFactory(
            final org.hibernate.cfg.Configuration config) {
        log.debug("Rebuilding the Hibernate Session Factory from new Config");
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            /* if still open, close current session factory */
            sessionFactory.close();
        }
        /* build a new session factory and update static config */
        try {
            sessionFactory = config.buildSessionFactory();
        } catch (Exception e) {
            if (e instanceof java.sql.SQLException) {
                log.error("Building Hibernate Session failed due to"
                        + " an SQLExeption: " + e.getMessage());
            } else {
                log.error("Building Hibernate Session failed due to"
                        + " some unexpected Behaviour: " + e.getMessage());
            }
            throw new HibernateException("Database Conenction could not be"
                    + "instantiated.");
        }

        configuration = config;
    }

    /**
     * Returns the Hibernate session factory.
     *
     * @return hibernate session factory
     */
    static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            rebuildSessionFactory(configuration);
        }
        return sessionFactory;
    }

    /**
     * Shuts down the Hibernate session. All actions on Hibernate after you
     * have called this function will cause Exceptions!
     * Use it as the last method before shutting down your software system.
     */
    static void shutdown() {
        log.debug("Shutting down Hibernate...");
        try {
            getSessionFactory().close();
        } catch (Exception e) {
            // ignore.
            log.debug("Hibernate shutdown caused a database error.");
        }
        sessionFactory = null;
        log.debug("Hibernate shutdown complete.");
    }

}
