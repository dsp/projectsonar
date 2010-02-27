; 
; This file is part of Sonar.
; 
; Sonar is free software: you can redistribute it and/or modify
; it under the terms of the GNU General Public License as published by
; the Free Software Foundation, version 2 of the License
; 
; Sonar is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU General Public License for more details.
; 
; You should have received a copy of the GNU General Public License
; along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
; 
; Author: David Soria Parra <david.parra@student.kit.edu>
;
; Notes:
;   For some well known reasons we have to work around a small bug in the
;   current plugin system. We have to put the generated jar as well as the
;   clojure runtime _both_ into the classpath _and_ the jar for this
;   centrality into the plugin directory.

(ns edu.kit.ipd.sonar.server.centralities.Outdegree
    (:import
        (edu.kit.ipd.sonar.server.centralities Centrality Centrality$Type CentralityImpl)
        (java.util HashMap))
    (:gen-class
        :extends edu.kit.ipd.sonar.server.centralities.CentralityImpl))

; we are a node centrality
(defn -getType [this] Centrality$Type/NodeCentrality)

; name
(defn -getName [this] "Outdegree (Clojure)")

; current version
(defn -getVersion [this] 0)

; min requirements
(defn -getRequiredAPIVersion [this] 0)

; the actual calculation
(defn -getWeight[this graph]
  (let [nodes (.values (.getNodeList graph))
        result (new HashMap)]
    (doseq [node nodes]
      (.put result node
        (double
          (count
            (filter
              #(.isOutgoingEdge % node) (.getEdgeList graph))))))
    result)
)
