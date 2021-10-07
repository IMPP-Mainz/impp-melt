(ns util.helper)

(defn deep-merge-with
  "Like merge-with, but merges maps recursively, applying the given fn
  only when there's a non-map at a particular level.
  (deep-merge-with + {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
                     {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}})
  -> {:a {:b {:z 3, :c 3, :d {:z 9, :x 1, :y 2}}, :e 103}, :f 4}"
  [f & maps]
  (apply
    (fn m [& maps]
      (if (every? map? maps)
        (apply merge-with m maps)
        (apply f maps)))
    maps))

(defn argmin
  ([f x] x)
  ([f x y] (if (< (f x) (f y)) x y))
  ([f x y & more] (reduce (partial argmin f) (argmin f x y) more)))

(defn argmax
  ([f x] x)
  ([f x y] (if (> (f x) (f y)) x y))
  ([f x y & more] (reduce (partial argmax f) (argmax f x y) more)))

(defn numeric? [s]
  (if-let [s (seq s)]
    (let [s (if (= (first s) \-) (next s) s)
          s (drop-while #(Character/isDigit %) s)
          s (if (= (first s) \.) (next s) s)
          s (drop-while #(Character/isDigit %) s)]
      (empty? s))))

(defn into-vec [a b]
  (concat (if (coll? a) a [a])
          (if (coll? b) b [b])
          ))

(defn to-hash-set [coll]
  (into (hash-set) coll))

(defn get-description [x]
  (cond
    (not (nil? (meta x))) (meta x)
    (nil? x) ""
    (number? x) x
    (string? x) x
    :else (if (class? (type x))
            (.getSimpleName (type x))
            (type x))))