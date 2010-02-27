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

/**
 * A factory class for all possible instances of a calculator.
 *
 * The factory class is used to generate new calculator objects based on the
 * current system configuration. E.g. The calculator factory can create a cached
 * calculator object backed by global calculator if caching is enabled.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
final class CalculatorFactory {
    /**
     * Do not allow to instantiate a new instance from the outside.
     */
    private CalculatorFactory() {
    }

    /**
     * Initialize a new graph global calculator based on the current
     * configuration.
     *
     * @return A new calculator
     */
    static Calculator createCalculatorForGlobalGraphs() {
        Calculator calc = new GlobalCalculator();
        if (Configuration.getInstance().calculatorCachingEnabled()) {
            return new CachedCalculator(calc);
        } else {
            return calc;
        }
    }

    /**
     * Initialize a new graph peer calculator based on the curren
     * configuration.
     *
     * @return A new calculator
     */
    static Calculator createCalculatorForPeerGraphs() {
        Calculator calc = new PeerCalculator();
        if (Configuration.getInstance().calculatorCachingEnabled()) {
            return new CachedCalculator(calc);
        } else {
            return calc;
        }
    }
}
