(ns multi-labeling.n-fold-stratification
  (:use [util.helper])
  (:require [clojure.math.combinatorics :as COMBO]
            [multi-labeling.analyze :as ANALYSE]))

(defrecord Labeled-data [id labels] :load-ns true)

;; iterative-stratification selection for k-fold crossvalidation
(defn get-label-sets-for [input-data]
  (into #{} (map #(vector (:id %1) (:labels %1)) input-data)))

(defn add-example-for-label [actual-example-label-set example]
  (reduce #(update-in %1 [%2] (fnil conj #{}) example)
          actual-example-label-set
          (second example)))

(defn add-examples-for-label [example-set]
  (reduce #(add-example-for-label %1 %2) {} example-set))

(defn get-label-with-minimal-examples [example-set]
  (apply argmin (comp count second) (add-examples-for-label example-set)))

(defn select-set [S c_l_j]
  (let [M (first (sort-by key > (group-by val c_l_j)))]
    (if (= 1 (count (val M)))
      (first (first (val M)))
      (first (rand-nth (val (first (sort-by key (group-by #(count (val %1))
                                                          (select-keys S (map key (val M))))))))))))

(defn add-examples [example-set S expected-examples-label-set label to-add-examples]
  (loop [es example-set s S eels expected-examples-label-set examples to-add-examples]
    (if (empty? examples) [es eels s]
                          (let [selected-set (select-set s (get eels label))
                                example (first examples)]
                            (recur (disj es example)
                                   (update-in s [selected-set] (fnil conj #{}) example)
                                   (reduce #(update-in %1 [%2 selected-set] dec) eels (second example))
                                   (rest examples))))))

(defn iterative-stratification-training-elements [input-examples folds]
  "Iterative stratification selection of examples. The algorithm is taken from http://lpis.csd.auth.gr/publications/sechidis-ecmlpkdd-2011.pdf."
  (let [example-set_init (get-label-sets-for input-examples)
        r (/ 1 folds)
        D_i_init (add-examples-for-label example-set_init)
        expected-examples-label-set-init (reduce #(assoc-in %1 [(first %2) (second %2)]
                                                            (* (count (get D_i_init (first %2))) r))
                                                 {}
                                                 (COMBO/cartesian-product (keys D_i_init) (range folds)))]

    (loop [example-set example-set_init
           expected-examples-label-set expected-examples-label-set-init
           S (zipmap (range folds) (repeat (hash-set)))]
      (if (empty? example-set) (reduce #(assoc %1 (key %2) {:training (val %2)}) {} S)
                               (let [selected-label-and-examples (get-label-with-minimal-examples example-set)
                                     result (add-examples example-set S expected-examples-label-set (first selected-label-and-examples) (second selected-label-and-examples))]
                                 (recur (nth result 0) (nth result 1) (nth result 2)))))))

(defn- get-examples-for-testing-set [input-examples training-examples]
  (let [training-ids (into #{} (map first training-examples))]
    (map #(vector (:id %1) (:labels %1)) (filter #(not (contains? training-ids (:id %1))) input-examples))))

(defn calculate-n-fold-statistics [input-examples folds result]
  (let [label-imbalance (apply merge (pmap #(hash-map (key %1)
                                                      (merge (val %1)
                                                             {:training-label-imbalance (ANALYSE/calculate-label-metrics
                                                                                          (map second (:training (val %1))))
                                                              :testing-label-imbalance  (ANALYSE/calculate-label-metrics
                                                                                          (map second (get-examples-for-testing-set input-examples (:training (val %1)))))
                                                              }))
                                           result))]
    (merge label-imbalance
           {:n                        (count input-examples) :folds folds
            :complete-label-imbalance (ANALYSE/calculate-label-metrics (map :labels input-examples))}
           )))

(defn iterative-stratification-n-fold [input-examples folds & {:keys [statistics? complete? include-testing?] :or {statistics? false complete? false include-testing? false}}]
  ((if include-testing? #(reduce (fn [m x] (assoc m (key x) (assoc (val x) :testing (get-examples-for-testing-set input-examples (:training (val x)))))) {} %1)
                        identity)
   ((if complete? #(assoc %1 :complete input-examples) identity)
    (if statistics?
      (calculate-n-fold-statistics input-examples folds (iterative-stratification-training-elements input-examples folds))
      (iterative-stratification-training-elements input-examples folds))
    )))