(defproject impp-melt "0.1.0-SNAPSHOT"
  :description "Utility functions to use in a multi labeling environment."
  :url "http://example.com/FIXME"
  :license {:name "GPL-3.0"
            :url  "https://choosealicense.com/licenses/gpl-3.0"
            :comment "GNU General Public License v3.0"
            :year 2021
            :key "gpl-3.0"}
  :authors [{:name  "Alexandra Núñez"
             :email "anunez@impp.de"}
            {:name  "Marcus Lindner"
             :email "mlindner@impp.de"}
            ]

  :dependencies [
                 ;; Core dependencies
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/core.memoize "0.7.2"]
                 [org.clojure/math.combinatorics "0.1.6"]
                 [net.mikera/core.matrix "0.62.0"]
                 [net.mikera/vectorz-clj "0.48.0"]
                 [ultra-csv "0.2.3"]
                 [incanter "1.9.3"]
                 [com.google.code.gson/gson "2.8.5"]
                 [com.google.guava/guava "28.1-jre"]

                 ;; Im- & Exports
                 [cheshire "5.3.1"]

                 ;; Logging
                 [org.clojure/tools.logging "1.1.0"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [org.slf4j/slf4j-simple "1.7.25"]
                 [org.apache.logging.log4j/log4j-core "2.13.0"]
                 [org.apache.logging.log4j/log4j-api "2.13.0"]
                 ]

  :plugins [[lein-licenses "0.2.2"]
            [lein-license "1.0.0"]
            ]
  :resource-paths ["resources/"]

  )
