(ns util.parallel)

(defn ppmap [grain-size f & colls]
  "Partitioned pmap, for grouping map ops together to make parallel overhead worthwile.
Like any other parallel function the call is only usefull if 'f' has a complexity of O(n²) or higher. 
(Written by Daniel Higginbotham. See http://www.braveclojure.com/concurrency)"
  (apply concat 
         (apply pmap 
                (fn [& pgroups] (doall (apply map f pgroups )))
                (map (partial partition-all grain-size) colls))))

(defn sppmap [grain-size f & colls]
  "Swapped partitioned pmap, for grouping map ops together to make parallel overhead worthwile.
Like any other parallel function the call is only usefull if 'f' has a complexity of O(n²) or higher. 
Based on ppmap, but swaps the map and pmap steps. The 'grain-size' is used to determine the size of 
the group which is calculated by the pmap call. Useable if the executed function have a hughe complexity and/or
memory useage."
  (apply concat 
         (apply map 
                (fn [& pgroups] (doall (apply pmap f pgroups )))
                (map (partial partition-all grain-size) colls))))
