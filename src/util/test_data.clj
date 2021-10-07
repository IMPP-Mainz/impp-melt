(ns util.test-data
  (:import [java.nio.charset Charset]
           [com.google.common.hash Hashing])
  (:use [multi-labeling.n-fold-stratification]))

(defn generate-random-data [label-selection size]
  (let [labels (apply hash-set (take size (shuffle label-selection)))]
    (->Labeled-data (.toString (.hashString (Hashing/sha512)
                                          (str labels (rand) (System/nanoTime))
                                          (Charset/forName "UTF-8")))
                  labels)))

(defn generate-x-multiple-random-data
  "Generates a specified number of random labeled data.
   The passed parameter 'x' defines the number generated [[Class-data]] elements.
   The label selection is defined by 'labels', a collection of labels, and 'size-range' is an array of integer which defines the number of labels which can be selected..
   "
  [x labels size-range]
  (map #(generate-random-data labels %1) (repeatedly x #(rand-nth size-range))))
