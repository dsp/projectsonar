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
package edu.kit.ipd.sonar.server

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A factory class for all possible instances of a calculator.
 *
 * The factory class is used to generate new calculator objects based on the
 * current system configuration. E.g. The calculator factory can create a cached
 * calculator object backed by global calculator if caching is enabled.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
object CalculatorFactory {
    protected val log = LoggerFactory.getLogger(getClass)

   /**
    * Initialize a new graph global calculator based on the current
    * configuration.
    *
    * @return A new calculator
    */
    def createCalculatorForGlobalGraphs() = {
        if (Configuration.getInstance().calculatorCachingEnabled()) {
            log debug "generating global calculator with caching"
            new GlobalCalculator with Caching
        } else {
            log debug "generating global calculator" 
            new GlobalCalculator
        }
    }

   /**
    * Initialize a new graph peer calculator based on the current
    * configuration.
    *
    * @return A new calculator
    */
    def createCalculatorForPeerGraphs() = {
        if (Configuration.getInstance().calculatorCachingEnabled()) {
            log debug "generating peer calculator with caching"
            new PeerCalculator with Caching
        } else {
            log debug "generating peer calculator" 
            new PeerCalculator
        }
    }
}

// vim: set ts=4 sw=4 et:
