(ns multi-labeling.export
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [ultra-csv.core :as ULTRA-CSV]
            [cheshire.core :refer :all]
            [clojure.java.io :refer [file]]
            ))

(defn write-json-file [^String file-path json-data]
  (spit file-path (generate-string json-data {:pretty true})))

(defn write-csv-file [path columns row-data]
  (let [headers (map name columns)
        rows (mapv #(mapv % columns) row-data)]
    (with-open [file (io/writer path)]
      (csv/write-csv file (cons headers rows) :separator \;))))

(defn read-csv-file [^String path]
  (ULTRA-CSV/read-csv path))