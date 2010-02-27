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

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



/**
 * A Singleton providing access to the global Configuration.
 *
 * This Class provides access to values specified in the configuration.xml.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public final class Configuration {

    /**
     * The relative Path where to find the configuration.xml file.
     */
    private static final String RELATIVE_CONFIG_FILE_PATH =
        "edu/kit/ipd/sonar/server/configuration.xml";

    /** The Singleton-Instance of the Configuration. */
    private static Configuration instance
            = new Configuration(RELATIVE_CONFIG_FILE_PATH);

    /**
     * The relative Path where to find the configuration.xsd file.
     */
    private static final String RELATIVE_SCHEMA_PATH =
        "edu/kit/ipd/sonar/server/configuration.xsd";

    /** The provided Version of the Centrality-Plugin-API.
     * Has to be set here mannually to increase it. */
    private final int apiVersion = 0;

    /** The Admin Password that provides access
     * to the extended functions of Sonar. */
    private final String adminPassword;

    /** The Hash algorithm used to store User Passwords in the database. */
    private final String userPwHashAlgorithm;

    /** Specifies if the centrality-calculation will be cached. */
    private final boolean calculatorCachingEnabled;

    /** Specifies if the database-requests will be cached. */
    private final boolean databaseCachingEnabled;

    /** Specifies if hibernate is used to access the database. */
    private final boolean hibernateEnabled;

    /** The path to the hibernate-configuration-file. */

    private final URL hibernateConfigURL;

    /**
     * A private constructor used to create the Singleton-instance.
     *
     * Reads in the Configurations from the configuration.xml-file.
     *
     * @param filePath The Path where to find the configuration File.
     */
    private Configuration(final String filePath) {

        Logger log = LoggerFactory.getLogger(Configuration.class);

        //Get the
        Document configFileDOM = getDOMFromXMLFile(filePath);
        if (configFileDOM != null) {

            log.debug("parsing values out of the configuration file.");

            // Parse the config-values out of the DOM
            // and set the attributes:

            // Check values stored under the Tag <security>
            NodeList list = configFileDOM.getElementsByTagName("security");
            Element elem = (Element) list.item(0);
            adminPassword = elem.getAttribute("adminPass");
            userPwHashAlgorithm
                    = elem.getAttribute("userPasswordAlgorithm");

            // Check (optional) values stored under the Tag <centralities>
            // Check (optional) values stored under the Tag <caching>
            // if nothing is specified, use false as the default value.

            NodeList cachingTags
                    = configFileDOM.getElementsByTagName("caching");
            if (cachingTags.getLength() >= 1) {
                list = configFileDOM.getElementsByTagName("database");
                elem = (Element) list.item(0);
                if (elem.getAttribute("enabled").equalsIgnoreCase("true")) {
                    databaseCachingEnabled = true;
                } else {
                    databaseCachingEnabled = false;
                }

                list = configFileDOM.getElementsByTagName("calculator");
                elem = (Element) list.item(0);
                if (elem.getAttribute("enabled").equalsIgnoreCase("true")) {
                    calculatorCachingEnabled = true;
                } else {
                    calculatorCachingEnabled = false;
                }
            } else {
                databaseCachingEnabled = false;
                calculatorCachingEnabled = false;

            }

            // Check values stored under the Tag <database><hibernate>
            list = configFileDOM.getElementsByTagName("hibernate");
            elem = (Element) list.item(0);
            String hibernateConfigPath = elem.getAttribute("config");

            //try to resolve the path to the hibernate Config:
            hibernateConfigURL =
                getClass().getClassLoader().getResource(hibernateConfigPath);
            hibernateEnabled = true;


        } else {
            log.info("Init with default values");
            //set default values:
            hibernateConfigURL = null;
            hibernateEnabled = false;
            databaseCachingEnabled = false;
            calculatorCachingEnabled = false;
            userPwHashAlgorithm = "MD5";
            adminPassword = "";
        }

    }


    /**
     * Looks for an XML-File at the given path,
     * checks it against the defined schema and
     * returns it as an Document-Object.
     * @param filePath the path where to look for the file.
     * @return The Configuarion-DOM. null if an error occured.
     */
    private Document getDOMFromXMLFile(final String filePath) {

        Logger log = LoggerFactory.getLogger(Configuration.class);
        DocumentBuilder parser;
        ClassLoader loader = getClass().getClassLoader();

        Document configFileDOM = null;
        log.info("parsing configuration file");
        // parse the XML-config file into a DOM tree:
        try {

            log.info("search classpath for " + filePath);
            URL xmlUrl = loader.getResource(filePath);

            if (null == xmlUrl) {
                throw new IOException("Cannot find configuration file");
            }

            log.info("found configuration file in " + xmlUrl);
            parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            configFileDOM = parser.parse(xmlUrl.openStream());

            log.debug("the parsing of the configuration file was successfull");

        } catch (Exception e) {
            log.error("Exception while parsing config file: "
                    + e.getMessage());
            return null;
        }

        // check if the xml-file is valid:
        try {
            // create a SchemaFactory capable of understanding WXS schemas
            SchemaFactory factory
             = SchemaFactory.newInstance(
                     XMLConstants.W3C_XML_SCHEMA_NS_URI);

            log.info("search classpath for " + RELATIVE_SCHEMA_PATH);
            URL xmlUrl = loader.getResource(RELATIVE_SCHEMA_PATH);

            if (null == xmlUrl) {
                throw new IOException(
                        "Cannot find configuration schema file");
            }

            log.info("found configuration file in " + xmlUrl);
            // load a WXS schema, represented by a Schema instance
            Source schemaFile
            = new StreamSource(xmlUrl.openStream());

            Schema schema = factory.newSchema(schemaFile);

            // check the config file for schema-compliance
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(configFileDOM));

            log.debug("the validation of the"
                    + " configuration file was successfull");

        } catch (Exception e) {
            log.error("Exception while validating the config File: "
                    + e.getMessage());
            return null;
        }

        return configFileDOM;
    }

    /**
     * Returns the Singleton-Instance of the Configuration.
     * @return The Singleton-Instance of this class.
     */
    public static Configuration getInstance() {
        return instance;
    }


    /**
     * Returns the version of the centrality-plugin-API supported.
     * @return The provided API Version
     */
    public int getAPIVersion() {
        return apiVersion;
    }

    /**
     * Returns if database caching is enabled.
     * @return if database caching is enabled.
     */
    public boolean databaseCachingEnabled() {
        return databaseCachingEnabled;
    }

    /**
     * Returns if calculator caching is enabled.
     * @return if calculator caching is enabled.
     */
    public boolean calculatorCachingEnabled() {
        return calculatorCachingEnabled;
    }

    /**
     * Return if Hibernate support is set in the configuration file or not.
     * @return true if Hibernate is activated.
     */
    public boolean hibernateEnabled() {
        return hibernateEnabled;
    }

    /**
     * Returns the URL to the Hibernate configuration file.
     * @return url to the Hibernate configuration file. Null if not found.
     */
    public URL getHibernateConfig() {
        return hibernateConfigURL;
    }

    /**
     * Returns the admin password that is set in the configuration file.
     * @return the admin password.
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Returns the Hash algorithm used to store user passwords in the database.
     * Is either "MD5" "SHA-1" "SHA-256" or "SHA-512".
     * @return the hash algorithm used.
     */
    public String getUserPasswordHashAlgorithm() {
        return userPwHashAlgorithm;
    }


}
