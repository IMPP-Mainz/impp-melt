(ns multi-labeling.analyze
  (:require
    [clojure.math.combinatorics :as C]
    [incanter.core :as I-C]
    [incanter.stats :as I-S]
    [multi-labeling.export :as EXPORT]
    [clojure.core.matrix.dataset :as DATASETS])
  (:use [util.helper]
        [util.parallel]))

(defn- count-labels [counting-map label-set]
  (reduce #(update-in %1 [%2] (fnil inc 0)) counting-map label-set))

(defn calculate-IRLb [label-coll]
  (let [label-vals (sort-by second > (reduce #(count-labels %1 %2) {} (map #(if (set? %1) %1 (into #{} %1)) label-coll)))]
    (reduce #(assoc %1 (first %2)
                       (double (/ (second (first label-vals))
                                  (second %2))
                               )) (sorted-map) label-vals)))

(defn count-coocurences [label-coll]
  (reduce #(assoc %1 %2 1) {}
          (map #(into (sorted-set) %1) (C/combinations label-coll 2))))

(defn calculate-coocurences [label-coll & {:keys [parallel?] :or {parallel? true}}]
  (apply merge-with + ((if parallel? pmap map)
                       count-coocurences label-coll)))

(defn calculate-label-metrics [label-coll]
  (let [IRLb (calculate-IRLb label-coll)
        meanIR (I-S/mean (vals IRLb))
        k (count (keys IRLb))
        label-frequencies (reduce #(assoc %1 (key %2) (count (val %2))) {} (group-by identity label-coll))
        instances (count label-coll)
        card (I-S/mean (map count label-coll))
        label-diversity (count label-frequencies)]
    (sorted-map :IRLb (into (sorted-map-by (fn [key1 key2]
                                             (compare [(get IRLb key2) key2]
                                                      [(get IRLb key1) key1])))
                            IRLb)
                :MeanIR meanIR
                :k k
                :cooccurrence (calculate-coocurences label-coll)
                :instances instances
                :label-sets label-frequencies
                :label-set-frequency-quantile (I-S/quantile (vals label-frequencies))
                :label-set-frequency-mean (I-S/mean (vals label-frequencies))
                :label-set-frequency-median (I-S/median (vals label-frequencies))
                :label-set-frequency-kurtosis (I-S/kurtosis (vals label-frequencies))
                :label-set-frequency-skewness (I-S/skewness (vals label-frequencies))
                :label-set-frequency-sd (I-S/sd (vals label-frequencies))
                :label-diversity label-diversity
                :label-diversity-normalized (double (/ label-diversity instances))
                :P-min (double (/ (count (filter #(= 1 (count %1)) label-coll)) instances))
                :MaxIR (apply max (vals IRLb))
                :SDIR (I-S/sd (vals IRLb))
                :MedianIR (I-S/median (vals IRLb))
                :CVIR (/ (I-S/sd (vals IRLb)) meanIR)
                :CARD card
                :DENS (/ card k)
                :MEDIAN (I-S/median (map count label-coll))
                )))

(defn calculate-label-correlation [label-colls labels & {:keys [parallel?] :or {parallel? true}}]
  {:pre [(every? string? labels)]}
  (let [initial-vector (zipmap labels (repeat 0))
        dataset (I-C/to-dataset ((if parallel? pmap map)
                                 #(merge initial-vector (zipmap %1 (repeat 1.0)))
                                 label-colls))]
    {:correlation (I-C/dataset (DATASETS/column-names dataset)
                               (I-S/correlation dataset))
     :data        dataset
     :label-order (zipmap (DATASETS/column-names dataset) (range))
     }))

(defn dataset->map [dataset]
  (let [columns (DATASETS/column-names dataset)]
    (apply merge (map #(hash-map (key %1) (zipmap columns (val %1))) (DATASETS/to-map dataset))
           )))

(defn save-label-correlation
  ([file-name label-colls labels]
   (let [dataset (:correlation (calculate-label-correlation label-colls labels))]
     (EXPORT/write-csv-file
       (str file-name ".csv")
       (concat [:name] (sort labels))
       (map #(assoc %2 :name %1) (DATASETS/column-names dataset) (DATASETS/row-maps dataset))))))

