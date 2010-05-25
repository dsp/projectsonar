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
package edu.kit.ipd.sonar.server.centralities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.sonar.server.Configuration;

/**
 * A class loader for the centrality plugin system.
 *
 * The class loads centralities from a directory defined by the configuration.
 * Every class that is loaded needs to implement the Centrality interface.
 *
 * A plugin is a jar file that contains all classes needed to execute the
 * centrality. The jar file is a standard jar file.For example the class
 * <p/>
 * <code>edu.kit.ipd.sonar.server.centralities.OutdegreeCentrality</code>
 * <p/>
 * needs to be placed as
 * <p/>
 * <code>edu/kit/ipd/sonar/server/centralities/OutdegreeCentrality.class</code>
 * <p/>
 * into the jar as. You can use non-java runtimes such as Scala but you have to
 * provide the runtime either in the jar or put the jar into the classpath. This
 * can be done by copying the runtime into the <code>WEB-INF/lib/</code>
 * directory.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class CentralityLoader {

    /**
     * The global loader instance.
     */
    private static CentralityLoader instance = null;

    /**
     * A set of loaded edge centralities.
     */
    private ArrayList<CentralityImpl> centralities;

    /**
     * The logging framework.
     */
    private static Logger log = LoggerFactory.getLogger(CentralityLoader.class);

    /**
     * Check if already loaded.
     */
    private boolean loaded = false;

    /**
     * The standard loader.
     */
    private ClassLoader loader = CentralityLoader.class.getClassLoader();

    /**
     * Load a list of jars.
     *
     * Load all classes from given jar files.
     *
     * @throws Exception if loading fails.
     * @return A list of classes
     */
    protected List<Class<?>> loadJars()
        throws Exception {

        Thread.currentThread().setContextClassLoader(loader);
        LinkedList<Class<?>> results = new LinkedList<Class<?>>();
        InputStream in = loader.getResourceAsStream("plugins.config");

        if (null == in) {
            log.warn("plugins.config not found");
            return results;
        }

        BufferedReader d
            = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = d.readLine()) != null) {
            try {
                results.add(loader.loadClass(line));
            } catch (Exception e) {
                log.error(e.toString());
            }
        }

        return results;
    }

    /**
     * Returns the available centralities implementing the NodeCentrality
     * interface.
     *
     * @return An array of available centralities
     */
    public ArrayList<CentralityImpl> getAvailableCentralities() {
        return centralities;
    }

    /**
     * Load all available centralities from the centrality directory and check
     * if they are valid implementations of Centrality.
     *
     * This methos reloads all centraliies from the plugins directory. Note that
     * the old classes are not unloaded.
     *
     */
    public void reload() {
        log.info("reload centralities");

        centralities = new ArrayList<CentralityImpl>();
        try {
            for (Class<?> clazz : loadJars()) {
                addClass(clazz);
            }
        } catch (ClassNotFoundException cie) {
            log.error(cie.toString());
        } catch (Exception ie) {
            log.error(ie.toString());
        } finally {
            loaded = true;
        }
    }

    /**
     * Add a class to the list of found centralities if possible.
     *
     * If found it instantiates the class.
     *
     * @throws InstantiationException If instantiation fails.
     * @throws IllegalAccessException If we are not allowed to load.
     * @param toInstantiate The class to check
     */
    protected void addClass(final Class<?> toInstantiate)
        throws InstantiationException, IllegalAccessException {
        if (CentralityImpl.class.isAssignableFrom(toInstantiate)) {
            CentralityImpl impl = (CentralityImpl) toInstantiate.newInstance();

            String name = toInstantiate.getCanonicalName();
            int version = impl.getVersion();
            int reqVersion = impl.getRequiredAPIVersion();
            int apiVersion = Configuration.getInstance().getAPIVersion();

            log.info("try to load centrality: " + name);
            log.info("  centrality version:   " + version);
            log.info("  requires api version: "
                + reqVersion + " (provided is " + apiVersion + ")");
            if (reqVersion <= apiVersion) {
                centralities.add(impl);
                log.info("  completed.");
            } else {
                log.warn("  cannot load centrality " + name);
                log.warn("  api version " + reqVersion
                    + " doesn't match provided version " + apiVersion + ".");
            }
        }
        /* only chuck norris can cast any class to Centrality */
    }

    /**
     * Construct a new object.
     */
    protected CentralityLoader() {
        centralities = new ArrayList<CentralityImpl>();
        loaded = false;
    }

    /**
     * Singleton interface for CentralityLoader.
     *
     * Creates a new instance or returns the existing instance.
     *
     * @return The instance
     */
    public static synchronized CentralityLoader createInstance() {
        if (null == instance) {
           instance = new CentralityLoader();
        }

        return instance;
    }
}
