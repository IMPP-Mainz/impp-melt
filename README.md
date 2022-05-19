# IMPP-MELT
The IMPP Multi-Lableing Tool is a project to process multi labled data. 

## Preliminary information
The code snippets used in this tutorial can be executed in the namespace ``multi-labeling.test``. This namespace contains all the needed dependencies to execute these code snippets. 

## Creating random data examples
With the function ``generate-x-multiple-random-data`` in the namespace ``util.test-data`` allows it to create randomly a specified number of randomly labeled elements which have a field ``:id`` and ``:labels``.
The field ``:id`` is used to identify the elements and is unique and the field ``:labales`` contains a set of labels for this element. 
The parameter ``x`` defines the number of elements which are generated, ``labels`` the labels from which the function can choose and ``size-range`` defines how many labels should be selected.  

The following code is an example where 4 labeled elements should be created with either 1 or 3 labels selected from "Flu", "Cold", "Asthma", "Hay Fever" or "Pollen Allergy"   
```clojure
(generate-x-multiple-random-data 4 ["Flu" "Cold" "Asthma" "Hay Fever" "Pollen Allergy"] [1 3])
=>
(#multi_labeling.n_fold.class-data{:id "700967eedeb55b5960211e321657f436dc62e28b43547ff364316788cc671e4d1c7b72ff0fa9094279141bb464d9865bad87a0dba60c01fdc2dc5a1cfa863282",
                                   :labels #{"Flu"}}
  #multi_labeling.n_fold.class-data{:id "042d54a89d324a6691ef51146fbb15fb64fe3ddd86cffb063edc1888684f9b200bfe1b2fc86a17a159bf7e3d7381129617f582df96753d332dfc89f5304b094f",
                                    :labels #{"Flu"}}
  #multi_labeling.n_fold.class-data{:id "f22f2414ad93307366c815ce43803019584534d6a591d77bd346c11b5f1e6744aa3fdb1fedaa4f7321d74a8739a4e8a6f059d99a8aa33e11319fb986b6121d79",
                                    :labels #{"Hay Fever" "Flu" "Asthma"}}
  #multi_labeling.n_fold.class-data{:id "13bc99e54143ef36a33deb0556e42dd260a9df0c0937dd38c0372c3ed90b30937944b5a2fa224cd893f7305ed64c4bab305e11de43b6e17402cc1a65c5a02d53",
                                    :labels #{"Asthma"}})
```

## Metrics
Deeper information about the metrics used in this project can be found in [[1]](#1). 
The function ``calculate-label-metrics`` which calculates the different metrics can be found in the namespace calculate-label-metrics ``multi-labeling.analyze``.
``calculate-label-metrics`` expects as parameter ``label-coll`` a collection which in turn contains collections (sets, lists or vectors) of labels. 

In the example below, the metrics are calculated for 10,000 randomly generated examples. Since due ``calculate-label-metrics`` only needs collections of labels and not the whole data objects, the labels are extracted beforehand.
Please note that some labels appear more than once in the vector. In this way we simulate the fact that certain labels should be selected more often than others.
``:instances`` is the number of instances for which the metrics are calculated, in our case 10,000, and ``:k`` the number of unique labels, here 5.

```clojure
(calculate-label-metrics
    (map :labels (generate-x-multiple-random-data 10000 ["Flu" "Flu" "Cold" "Asthma" "Asthma" "Asthma" "Hay Fever" "Pollen Allergy"] [1 3 4])))
  =>
  {:instances 10000,
   :k 5,
   :CARD 2.2784,
   :MEDIAN 3.0,
   :DENS 0.45568,
   :P-min 0.3377,
   :IRLb {"Cold"           2.119940029985007,
          "Hay Fever"      2.103540612912824,
          "Pollen Allergy" 2.081861012956419,
          "Flu"            1.257559587335468,
          "Asthma"         1.0},
   :MaxIR 2.119940029985007,
   :MeanIR 1.7125802486379436,
   :MedianIR 2.081861012956419,
   :CVIR 0.3157964964321817,
   :SDIR 0.5408268423788172,
   :label-diversity 27,
   :label-diversity-normalized 0.0027,
   :label-set-frequency-mean 370.3703703703704,
   :label-set-frequency-median 293.0,
   :label-set-frequency-quantile (51.0 164.5 293.0 431.0 1283.0),
   :label-set-frequency-kurtosis 0.7588101879689013, 
   :label-set-frequency-skewness 1.197633711031254,
   :label-set-frequency-sd 307.05964694278333,
   :label-sets {#{"Flu" "Asthma"} 789,
                #{"Hay Fever" "Asthma" "Cold"} 293,
                #{"Pollen Allergy" "Flu" "Cold"} 159,
                #{"Flu" "Asthma" "Cold"} 781,
                #{"Flu" "Cold"} 51,
                #{"Pollen Allergy" "Hay Fever" "Cold"} 77,
                #{"Hay Fever" "Flu"} 61,
                #{"Hay Fever"} 437,
                #{"Pollen Allergy" "Hay Fever" "Asthma" "Cold"} 133,
                #{"Hay Fever" "Flu" "Asthma" "Cold"} 329,
                #{"Pollen Allergy"} 425,
                #{"Pollen Allergy" "Hay Fever" "Flu" "Asthma"} 305,
                #{"Pollen Allergy" "Flu"} 52,
                #{"Hay Fever" "Flu" "Asthma"} 744,
                #{"Pollen Allergy" "Asthma"} 222,
                #{"Pollen Allergy" "Hay Fever" "Asthma"} 343,
                #{"Pollen Allergy" "Flu" "Asthma" "Cold"} 290,
                #{"Asthma" "Cold"} 231,
                #{"Hay Fever" "Asthma"} 203,
                #{"Cold"} 417,
                #{"Asthma"} 1283,
                #{"Flu"} 815,
                #{"Pollen Allergy" "Asthma" "Cold"} 314,
                #{"Hay Fever" "Flu" "Cold"} 170,
                #{"Pollen Allergy" "Hay Fever" "Flu"} 176,
                #{"Pollen Allergy" "Hay Fever" "Flu" "Cold"} 90,
                #{"Pollen Allergy" "Flu" "Asthma"} 810},
   :cooccurrence {#{"Asthma" "Flu"} 4048,
                  #{"Cold" "Flu"} 1870,
                  #{"Flu" "Hay Fever"} 1875,
                  #{"Pollen Allergy" "Flu"} 1882,
                  #{"Pollen Allergy" "Asthma"} 2417,
                  #{"Asthma" "Cold"} 2371,
                  #{"Cold" "Hay Fever"} 1092,
                  #{"Asthma" "Hay Fever"} 2350,
                  #{"Pollen Allergy" "Cold"} 1063,
                  #{"Pollen Allergy" "Hay Fever"} 1124}}
```
### Cardinality and median
Cardinality (``:CARD``) is the average number of labels per instance where the ``:MEDIAN`` is the median number of labels per instances.
In the example above the examples have on average 2.2784 labels and 50% of the examples have hat least 3 labels.

### Density 
Density (``:DENS``) is the cardinality divided by the number of labels and thus a normalised representation of the cardinality.
The smaller the value, the greater the distribution of the labels in the examples.

### Percentage of instances with one label (P-min)
``:P-min`` ist the percentage of instances with only one label. In our case 33.77 % of the instances have only one label.

### Imbalance Metrics 
[1]](#1) presents some metrics for determining label imbalance, some of which have been implemented.

#### Imbalance ratio per Label (IRLbl)
The imbalance ratio per Label ``:IRBLbl`` metric shows the imbalance of all labels to the label that occurs most in the set. It shows the imbalance of all labels to the label that occurs the most in the set. In our example, the label that occurs the most is "Asthma" which has a value of 1.0. Compared to the label "Pollen Allergy", "Asthma" occurs 2.081861012956419 times more often, which is why "Pollen Allergy" has a value of 2.081861012956419.

#### Maximal imbalance ratio (MaxIR), mean imbalance ratio (MeanIR) and median imbalance ratio (MedianIR)
These metrics are global assessment metrics for the imbalance of a dataset. Maximal imbalance ratio ``:MaxIR`` is the highest IRBbl value, in our example 2,111, and mean imbalance ratio ``:MeanIR`` is the average IRLbl value. In our case it is 1.7125802486379436 and means that the label "Asthma" occurs on average 1.7125802486379436 times more often than the other labels.
In addition, the median imbalance ratio ``:MedianIR`` is used, the median of IRLbl values, which in our case is 2.081861012956419 and means that for 50% of the labels "Asthma" occurs more than 2.081861012956419 times.

#### Coefficient of variation for the average imbalance ratio (CVIR)
The coefficient of variation for the average imbalance ratio helps to identify whether a high ``MeanIR`` is caused by only some labels with extreme imbalance levels or many labels with a high IRLbl.

#### Label diversity information
The label diversity (``:label-diversity``) is the number of distinct label sets in the instances and ``:label-diversity-normalized`` is the normalized label diversity. The distinct label sets and their occurrence can be found in ``:label-sets``

As suggested in [[1]](#1) further statistical information such as mean (``:label-set-frequency-mean``), median (``:label-set-frequency-median``),  quantile (``:label-set-frequency-quantile``), kurtosis (``:label-set-frequency-kurtosis``), skewness (``:label-set-frequency-skewness``) and standard deviation (``:label-set-frequency-sd``) is also displayed.  

### Cooccurrence
The last metric is the cooccurrence (``:cooccurrence``) between the labels, which gives information about how often which combination of two labels occurs.
In our example, the combination "Asthma" and "Flu" occurs the most times with 4048.

## Label correlation
The function ``calculate-label-correlation`` which calculates the [correlation](https://en.wikipedia.org/wiki/Correlation) of labels can be found in the namespace ``multi-labeling.analyze``.
``calculate-label-correlation`` expects as parameter ``label-colls`` the label collections and as ``labels`` a collection of the labels to be considered, which must be of type string. As an additional parameter, ``:parallel?`` can be passed the information whether the process should work in parallel or not. If ``:parallel?`` is ``true``, then parallel functions will be used, otherwise only sequential functions.
```clojure
(def result (calculate-label-correlation
              (map :labels (generate-x-multiple-random-data 10000 ["Flu" "Cold" "Asthma" "Hay Fever" "Pollen Allergy"] [1 3 4]))
              ["Flu" "Cold" "Asthma" "Hay Fever" "Pollen Allergy"]))
=> #'multi-labeling.test/result
```
The return value is a map this the keys ``:correlation``, ``:label-order`` and ``:data``.
``:correlation`` contains the correlation values of the labels in form of a 
```clojure
(:correlation result)
=>
|                 Cold |                 Flu |              Asthma |       Pollen Allergy |           Hay Fever |
|----------------------+---------------------+---------------------+----------------------+---------------------|
|    1.000000000000124 | 0.06746869090503343 | 0.06248325486428463 | 0.059820852006081685 | 0.06674748894644558 |
|  0.06746869090503346 |  0.9999999999998889 |  0.0634672888843792 |  0.06966581494550396 | 0.07505147414197517 |
| 0.062483254864284604 |  0.0634672888843792 |  1.0000000000000973 |  0.06548332967125124 | 0.07293562890793891 |
| 0.059820852006081664 | 0.06966581494550397 | 0.06548332967125124 |    1.000000000000135 | 0.07376733713512022 |
|   0.0667474889464456 | 0.07505147414197519 | 0.07293562890793887 |  0.07376733713512024 |  0.9999999999999868 |
```
> ☝️ Please note that the diagonal does not always have 1.0 as a number. This is due to floating point errors when calculating floating point numbers. For more information on this topic, please see [[2]](#2).

The namespace ``multi-labeling.analyze`` also contains a convenient function ``dataset->map`` that converts a dataset into a map.
```clojure
(dataset->map (:correlation result))
=>
{"Cold"           {"Cold"           1.000000000000124,
                   "Flu"            0.06746869090503346,
                   "Asthma"         0.062483254864284604,
                   "Pollen Allergy" 0.059820852006081664,
                   "Hay Fever"      0.0667474889464456},
 "Flu"            {"Cold"           0.06746869090503343,
                   "Flu"            0.9999999999998889,
                   "Asthma"         0.0634672888843792,
                   "Pollen Allergy" 0.06966581494550397,
                   "Hay Fever"      0.07505147414197519},
 "Asthma"         {"Cold"           0.06248325486428463,
                   "Flu"            0.0634672888843792,
                   "Asthma"         1.0000000000000973,
                   "Pollen Allergy" 0.06548332967125124,
                   "Hay Fever"      0.07293562890793887},
 "Pollen Allergy" {"Cold"           0.059820852006081685,
                   "Flu"            0.06966581494550396,
                   "Asthma"         0.06548332967125124,
                   "Pollen Allergy" 1.000000000000135,
                   "Hay Fever"      0.07376733713512024},
 "Hay Fever"      {"Cold"           0.06674748894644558,
                   "Flu"            0.07505147414197517,
                   "Asthma"         0.07293562890793891,
                   "Pollen Allergy" 0.07376733713512022,
                   "Hay Fever"      0.9999999999999868}}
```

``:label-order`` is the information which label has which position in the correlation dataset
```clojure
(:label-order result)
=> {"Cold" 0, "Flu" 1, "Asthma" 2, "Pollen Allergy" 3, "Hay Fever" 4}
```
And ``:data`` the label datasets which are used for the calculation.
```clojure
(:data result)
=>
| Pollen Allergy | Hay Fever | Flu | Asthma | Cold |
|----------------+-----------+-----+--------+------|
|            1.0 |       1.0 |   0 |    1.0 |  1.0 |
|              0 |       1.0 | 1.0 |    1.0 |  1.0 |
|              0 |         0 |   0 |      0 |  1.0 |
|            1.0 |       1.0 |   0 |    1.0 |    0 |
|            1.0 |         0 | 1.0 |    1.0 |  1.0 |
|            1.0 |       1.0 | 1.0 |      0 |  1.0 |
|            ... |       ... | ... |    ... |  ... |
|            ... |       ... | ... |    ... |  ... |
|            ... |       ... | ... |    ... |  ... |
|            ... |       ... | ... |    ... |  ... |
|            1.0 |         0 | 1.0 |    1.0 |    0 |
|            1.0 |       1.0 |   0 |    1.0 |  1.0 |
|              0 |       1.0 | 1.0 |      0 |  1.0 |
```

## N-Fold set creation
The name space ``multi-labeling.n-fold-stratification`` is used to select test (and training) data for imbalanced multilabeled examples. The function ``iterative-stratification-n-fold`` is an implementation of the iterative stratification algorithm defined in [[2]](#2). The function expects for the input parameters ``input-examples`` a collection of examples which are maps or map-like data objects which have the keys ``:id``, which contains a unique ID for the example, and the key ``:labels``, which has the labels of the example as value and for ``folds`` the number of folds. Boolean values can be passed as additional parameters for the keys ``:complete?``, ``include-testing?`` and ``statistics?``. By default, it is assumed that all these additional parameters are ``false``.

By default, the function returns a map that has the fold number (starting with 0 to ``folds``-1) as a key and a map as a value that contains a collection of vectors for the ``:training`` key, where the first value is the ID of the example and the second are the labels for that example.

```clojure
(iterative-stratification-n-fold (generate-x-multiple-random-data 10 ["Flu" "Cold" "Asthma" "Hay Fever" "Pollen Allergy"] [1 3]) 2)

=>
{0 {:training #{["97ff749a6cdbc24053e64cf966b82d208ad6055d94dc4b98962c556f0f76f8b76fad341784f0e9870185c5832cf54da6ab1e0936ba8d29182fb481ba01c69dce"
                 #{"Hay Fever" "Asthma" "Cold"}]
                ["1d072ce4119a39e541724f8e1b061f1ff33113b62c4a4b8580184ece05a31c1f4840b36f29044aa6ee8bf9d974f75ab45d792f6d17e42a3259859f843f0df5e5"
                 #{"Pollen Allergy" "Hay Fever" "Flu"}]
                ["8417644a8437171dcab065bb6e60071ec14b29842934cacaf38a410ddc56480b276dfe9374c883b38fd2bb735d7715c7f07f8e49b5f0d8cbbd2c50d8c2586501"
                 #{"Hay Fever" "Flu" "Asthma"}]
                ["172289c281f69ab7a9b95de7c75cb558eb5a6e809081388610278deb1ba0d0fbda3bb7435ec62c080223311d302313a3a1dac75417469f69fc5b440619b50b4f"
                 #{"Hay Fever" "Asthma" "Cold"}]
                ["ec43f0e66df9230595c747ac93243064b924493d29cd56af111a1f800478e95547ca7ecdac61a42c2d69e0a4952f44366717da21624a53a1b33b8268d63bcc41"
                 #{"Pollen Allergy" "Hay Fever" "Asthma"}]}},
 1 {:training #{["5a0f92d72f2f1c69ff96c8dad3e390a64024d542e1d3b1e0d42b037fcc6fca6f6a5fb92a1aa76ffdddc0048534e6393800a52e65b18942306714a368c5d7fc7c"
                 #{"Hay Fever"}]
                ["8ad88671dd6aa5df8eeb5a3a2bd4ee6d6856321c33df11a5cf7ad81704df5ded2a8c943441d4631676677d47cc51129989b399234194792a3b809cea38067192"
                 #{"Pollen Allergy" "Flu" "Cold"}]
                ["4193de7e0e840a8d75e36a30e5e88f9eeea3fc3a67a68cbe68aab32816ac40d98f83ffaeb264b0b89af3c4bdad67489eb0d13d044ff3ccc784ba14cefa209f42"
                 #{"Flu"}]
                ["c4e2f180e57c81dfd291f3f9fafdbdc226b4cb395ee7dd91805f245c716bc38b590465b2a1256ebb7dc6a2c69f4ae026d294291461f4947296b0e2b578f2c2e8"
                 #{"Flu" "Asthma" "Cold"}]
                ["ef7ed01c4333761e5163ed3937854d44cbc87de87ac22c9a3264d24b0f5413832b1a8b5cb72df32158c25b36247b6cfbe77c8925c3ae9748ca5204810b89181f"
                 #{"Hay Fever" "Asthma" "Cold"}]}}}
```

If ``:complete?`` is ``true`` then the map will also have all the examples used to create it added under the key ``:complete``.

```clojure 
(iterative-stratification-n-fold (generate-x-multiple-random-data 10 ["Flu" "Cold" "Asthma" "Hay Fever" "Pollen Allergy"] [1 3]) 2 :complete? true)
=>
{0 {:training #{["d1ea17c064cb4e4aa4e4d7d97f8b0f961d612cca97f830731aded31cf5806fcf9baf4e0edcf103db0ec5f47db825401d750a4bd6977118852fcdcc544cccd8a0"
                 #{"Asthma"}]
                ["b49dd2da288268c629ca4cdfc8330c770c4c7075e60236b28ad4f252b175698aa4c62654affa7e9727a88ce4bc690bca99989e96bed5ac14ef43d1dfdc530944"
                 #{"Pollen Allergy" "Flu" "Cold"}]
                ["411352bcf55ba48b0606a023d3a2f9d8552965726d913db4b9aeb2169d13a9d4dbc60318273aae89d52ff53cdfc7cc44d8228d5e5d44ca070039dcdf7907beea"
                 #{"Hay Fever" "Flu" "Cold"}]
                ["057450b48618f56443bc1e98f97f8373e5de2b0a0afe4daa88d90be8305fdca2ae37a2cdb86c0c18e7a04f802b3e66641293c93bac29b764c39608f2ef7120e0"
                 #{"Cold"}]}},
 1 {:training #{["4a1f5763b2e8ff622089cf572b061d928e28cbada1688d572bc2341920f89a9d0c6e1a74588600e97beee793ea8998af5b8e99bf015b0c289c1cfb0f2ffebe11"
                 #{"Cold"}]
                ["5a63c9747caa17c4761e94076a5e22c574b514aca67851f67ce5901ee5d6b8b0f9cd4fb85f2e64b664a3c91d695bf73286b13ec4135dc9f151c345ec265942c5"
                 #{"Hay Fever"}]
                ["62b50111e00efc616db91a65057adbd4696d66d0003689670626be321e1183e86339376ae2c8df47b46cec6ae73af1d22b30452921719a0d7b78ab73a2e3a11f"
                 #{"Flu"}]
                ["e8fcddf76c68810cacc183adf8ddfec97d908974b6ecad1797f06d3c70f1d7814f6bd32ee8370945232a7635dba28202061f64147fbbd26fd48c3671c2446c99"
                 #{"Flu"}]
                ["d5e89b20c06c2431dca957cf40511b1c37373661a87528c82e72b6f38d9a2bad311c54739e9d0b57811995cde26884763c62c749f08f6eda1534aa12c7ef9346"
                 #{"Pollen Allergy" "Asthma" "Cold"}]
                ["06e1b9999c1ff667e5f441e0f5c11c4d0ea3847d4cc83a4e0a25c9d4d4c71c2d111950ad6aa2ca953e9e68b9d9b15a5e80c897a78166781cbe579a35361b3d01"
                 #{"Pollen Allergy"}]}},
 :complete (#multi_labeling.n_fold.Labeled-data{:id "411352bcf55ba48b0606a023d3a2f9d8552965726d913db4b9aeb2169d13a9d4dbc60318273aae89d52ff53cdfc7cc44d8228d5e5d44ca070039dcdf7907beea",
                                                :labels #{"Hay Fever" "Flu" "Cold"}}
            #multi_labeling.n_fold.Labeled-data{:id "b49dd2da288268c629ca4cdfc8330c770c4c7075e60236b28ad4f252b175698aa4c62654affa7e9727a88ce4bc690bca99989e96bed5ac14ef43d1dfdc530944",
                                                :labels #{"Pollen Allergy" "Flu" "Cold"}}
            #multi_labeling.n_fold.Labeled-data{:id "d5e89b20c06c2431dca957cf40511b1c37373661a87528c82e72b6f38d9a2bad311c54739e9d0b57811995cde26884763c62c749f08f6eda1534aa12c7ef9346",
                                                :labels #{"Pollen Allergy" "Asthma" "Cold"}}
            #multi_labeling.n_fold.Labeled-data{:id "62b50111e00efc616db91a65057adbd4696d66d0003689670626be321e1183e86339376ae2c8df47b46cec6ae73af1d22b30452921719a0d7b78ab73a2e3a11f",
                                                :labels #{"Flu"}}
            #multi_labeling.n_fold.Labeled-data{:id "4a1f5763b2e8ff622089cf572b061d928e28cbada1688d572bc2341920f89a9d0c6e1a74588600e97beee793ea8998af5b8e99bf015b0c289c1cfb0f2ffebe11",
                                                :labels #{"Cold"}}
            #multi_labeling.n_fold.Labeled-data{:id "5a63c9747caa17c4761e94076a5e22c574b514aca67851f67ce5901ee5d6b8b0f9cd4fb85f2e64b664a3c91d695bf73286b13ec4135dc9f151c345ec265942c5",
                                                :labels #{"Hay Fever"}}
            #multi_labeling.n_fold.Labeled-data{:id "e8fcddf76c68810cacc183adf8ddfec97d908974b6ecad1797f06d3c70f1d7814f6bd32ee8370945232a7635dba28202061f64147fbbd26fd48c3671c2446c99",
                                                :labels #{"Flu"}}
            #multi_labeling.n_fold.Labeled-data{:id "d1ea17c064cb4e4aa4e4d7d97f8b0f961d612cca97f830731aded31cf5806fcf9baf4e0edcf103db0ec5f47db825401d750a4bd6977118852fcdcc544cccd8a0",
                                                :labels #{"Asthma"}}
            #multi_labeling.n_fold.Labeled-data{:id "06e1b9999c1ff667e5f441e0f5c11c4d0ea3847d4cc83a4e0a25c9d4d4c71c2d111950ad6aa2ca953e9e68b9d9b15a5e80c897a78166781cbe579a35361b3d01",
                                                :labels #{"Pollen Allergy"}}
            #multi_labeling.n_fold.Labeled-data{:id "057450b48618f56443bc1e98f97f8373e5de2b0a0afe4daa88d90be8305fdca2ae37a2cdb86c0c18e7a04f802b3e66641293c93bac29b764c39608f2ef7120e0",
                                                :labels #{"Cold"}})}
```
If ``:statistics?`` equals ``true``, then statistical information regarding the distribution of training data ``training-label-imbalance`` and test data ``:test-label-imbalance`` is stored for each fold.  In addition, the metrics for all examples are stored under the key ``:complete-label-imbalance``.

```clojure
(iterative-stratification-n-fold (generate-x-multiple-random-data 10 ["Flu" "Cold" "Asthma" "Hay Fever" "Pollen Allergy"] [1 3]) 2 :statistics? true)

=>
{0 {:training #{["8e2b7cb1e27e25522c514680a094aa1784d71bf9bc5f8686b4b253d1f67730240d98274861c7562d064eb4050dc5e08bd2583d34a536d8af7d70f17f6677587e"
                 #{"Flu"}]
                ["0603ebac827a7aa9e0d6e1b455a9d65c7792ba5bed3c4ced3c910302455bf9aaedb03f9f42b91c271d3f54e060d075397a193b7af6c7ba5321639b7f4371439e"
                 #{"Flu"}]
                ["6751b88dc0fa5d0a2a4131e2ed5e181ebac8cd44f42b20f7f3a11851e8354d7b8c3025badafb9fdae422de8677fa8e4fc32943ddfaf7adf10e855bb34b0e6707"
                 #{"Asthma"}]
                ["603cc8d9516a5139c93c7bbfcee84cc01ff60f80b18d560e9b345e82060f5d7e68445c12a9767ceece297bb1ba1431e4fba05f340f680c1f935d2dfe3b311ff7"
                 #{"Pollen Allergy" "Asthma" "Cold"}]
                ["8c01a5f45b9e3e45bd902298847880ebbc681d251e7b8dee22511b5d3caf127a74e7d582e9eb7fb1adb12870529ad947e16090190d9d35fc10a653c3b74c42c6"
                 #{"Hay Fever"}]
                ["f5e0db09a2b2c28a5cc03f3d864e4f6cdfb1f249bb7fd8b1a6a2516ac3ce3144a57bec894c6a690140de19fb4afc09e5ca8a6be32741fa61cc8e9ddb4a8e060a"
                 #{"Cold"}]},
    :training-label-imbalance {:CARD 1.3333333333333333,
                               :CVIR 0.3912303982179758,
                               :DENS 0.26666666666666666,
                               :IRLb {"Hay Fever" 2.0, "Pollen Allergy" 2.0, "Flu" 1.0, "Cold" 1.0, "Asthma" 1.0},
                               :MEDIAN 1.0,
                               :MaxIR 2.0,
                               :MeanIR 1.4,
                               :MedianIR 1.0,
                               :P-min 0.8333333333333333,
                               :SDIR 0.5477225575051661,
                               :cooccurrence {#{"Pollen Allergy" "Asthma"} 1, #{"Pollen Allergy" "Cold"} 1, #{"Asthma" "Cold"} 1},
                               :instances 6,
                               :k 5,
                               :label-diversity 5,
                               :label-diversity-normalized 0.8333333333333333,
                               :label-set-frequency-kurtosis -0.919999999999999,
                               :label-set-frequency-mean 1.2,
                               :label-set-frequency-median 1.0,
                               :label-set-frequency-quantile (1.0 1.0 1.0 1.0 2.0),
                               :label-set-frequency-sd 0.4472135954999579,
                               :label-set-frequency-skewness 1.0733126291998993,
                               :label-sets {#{"Flu"} 2,
                                            #{"Asthma"} 1,
                                            #{"Pollen Allergy" "Asthma" "Cold"} 1,
                                            #{"Hay Fever"} 1,
                                            #{"Cold"} 1}},
    :testing-label-imbalance {:CARD 2.5,
                              :CVIR 0.5393131982084,
                              :DENS 0.5,
                              :IRLb {"Cold" 3.0, "Pollen Allergy" 3.0, "Asthma" 1.5, "Hay Fever" 1.0, "Flu" 1.0},
                              :MEDIAN 3.0,
                              :MaxIR 3.0,
                              :MeanIR 1.9,
                              :MedianIR 1.5,
                              :P-min 0.25,
                              :SDIR 1.02469507659596,
                              :cooccurrence {#{"Flu" "Hay Fever"} 3,
                                             #{"Asthma" "Hay Fever"} 2,
                                             #{"Asthma" "Flu"} 2,
                                             #{"Cold" "Hay Fever"} 1,
                                             #{"Cold" "Flu"} 1},
                              :instances 4,
                              :k 5,
                              :label-diversity 3,
                              :label-diversity-normalized 0.75,
                              :label-set-frequency-kurtosis -2.333333333333333,
                              :label-set-frequency-mean 1.3333333333333333,
                              :label-set-frequency-median 1.0,
                              :label-set-frequency-quantile (1.0 1.0 1.0 1.5 2.0),
                              :label-set-frequency-sd 0.5773502691896257,
                              :label-set-frequency-skewness 0.38490017945975075,
                              :label-sets {#{"Hay Fever" "Flu" "Asthma"} 2, #{"Hay Fever" "Flu" "Cold"} 1, #{"Pollen Allergy"} 1}}},
 1 {:training #{["60232abd1065d663efac4cad5311655bfc027faf6829366c22d607d9526495fdf5202c5a3f1d012877572f9f2553874463d70cc93d2b9f88e4afc52570c8f99a"
                 #{"Hay Fever" "Flu" "Asthma"}]
                ["e97ae7c309368a7b163d504af2342a96235aa844a63a566ecedd635770ccee46d1f49eea8b9b0d698b6599398155fc4341e29ed548c67fba26da214d69d4f2bb"
                 #{"Hay Fever" "Flu" "Cold"}]
                ["1e5cef0d2d91cdbd9e861913fee13c7b3fda64668aa0dc0a5c0dd000f8eda5cbde7e04f22c54f887f4928c7e56ac64af88d55fcaaafbc4d899e2b25d853cace0"
                 #{"Hay Fever" "Flu" "Asthma"}]
                ["d06db56835068f0d39c9aa737517e2ed70253f2ae944d39bbc781d61adba146aa7eaec1eb24c3ed9d4b67155f78c7f8708159fe49ac1f83418466b8e51d344cf"
                 #{"Pollen Allergy"}]},
    :training-label-imbalance {:CARD 2.5,
                               :CVIR 0.5393131982084,
                               :DENS 0.5,
                               :IRLb {"Cold" 3.0, "Pollen Allergy" 3.0, "Asthma" 1.5, "Hay Fever" 1.0, "Flu" 1.0},
                               :MEDIAN 3.0,
                               :MaxIR 3.0,
                               :MeanIR 1.9,
                               :MedianIR 1.5,
                               :P-min 0.25,
                               :SDIR 1.02469507659596,
                               :cooccurrence {#{"Flu" "Hay Fever"} 3,
                                              #{"Asthma" "Hay Fever"} 2,
                                              #{"Asthma" "Flu"} 2,
                                              #{"Cold" "Hay Fever"} 1,
                                              #{"Cold" "Flu"} 1},
                               :instances 4,
                               :k 5,
                               :label-diversity 3,
                               :label-diversity-normalized 0.75,
                               :label-set-frequency-kurtosis -2.333333333333333,
                               :label-set-frequency-mean 1.3333333333333333,
                               :label-set-frequency-median 1.0,
                               :label-set-frequency-quantile (1.0 1.0 1.0 1.5 2.0),
                               :label-set-frequency-sd 0.5773502691896257,
                               :label-set-frequency-skewness 0.38490017945975075,
                               :label-sets {#{"Hay Fever" "Flu" "Asthma"} 2, #{"Hay Fever" "Flu" "Cold"} 1, #{"Pollen Allergy"} 1}},
    :testing-label-imbalance {:CARD 1.3333333333333333,
                              :CVIR 0.3912303982179758,
                              :DENS 0.26666666666666666,
                              :IRLb {"Hay Fever" 2.0, "Pollen Allergy" 2.0, "Flu" 1.0, "Cold" 1.0, "Asthma" 1.0},
                              :MEDIAN 1.0,
                              :MaxIR 2.0,
                              :MeanIR 1.4,
                              :MedianIR 1.0,
                              :P-min 0.8333333333333333,
                              :SDIR 0.5477225575051661,
                              :cooccurrence {#{"Pollen Allergy" "Asthma"} 1, #{"Pollen Allergy" "Cold"} 1, #{"Asthma" "Cold"} 1},
                              :instances 6,
                              :k 5,
                              :label-diversity 5,
                              :label-diversity-normalized 0.8333333333333333,
                              :label-set-frequency-kurtosis -0.919999999999999,
                              :label-set-frequency-mean 1.2,
                              :label-set-frequency-median 1.0,
                              :label-set-frequency-quantile (1.0 1.0 1.0 1.0 2.0),
                              :label-set-frequency-sd 0.4472135954999579,
                              :label-set-frequency-skewness 1.0733126291998993,
                              :label-sets {#{"Cold"} 1,
                                           #{"Pollen Allergy" "Asthma" "Cold"} 1,
                                           #{"Flu"} 2,
                                           #{"Hay Fever"} 1,
                                           #{"Asthma"} 1}}},
 :n 10,
 :folds 2,
 :complete-label-imbalance {:CARD 1.8,
                            :CVIR 0.3854483310108075,
                            :DENS 0.36,
                            :IRLb {"Pollen Allergy" 2.5, "Cold" 1.666666666666667, "Hay Fever" 1.25, "Asthma" 1.25, "Flu" 1.0},
                            :MEDIAN 1.0,
                            :MaxIR 2.5,
                            :MeanIR 1.5333333333333334,
                            :MedianIR 1.25,
                            :P-min 0.6,
                            :SDIR 0.5910207742165715,
                            :cooccurrence {#{"Pollen Allergy" "Asthma"} 1,
                                           #{"Pollen Allergy" "Cold"} 1,
                                           #{"Asthma" "Cold"} 1,
                                           #{"Flu" "Hay Fever"} 3,
                                           #{"Asthma" "Hay Fever"} 2,
                                           #{"Asthma" "Flu"} 2,
                                           #{"Cold" "Hay Fever"} 1,
                                           #{"Cold" "Flu"} 1},
                            :instances 10,
                            :k 5,
                            :label-diversity 8,
                            :label-diversity-normalized 0.8,
                            :label-set-frequency-kurtosis -1.2135416666666665,
                            :label-set-frequency-mean 1.25,
                            :label-set-frequency-median 1.0,
                            :label-set-frequency-quantile (1.0 1.0 1.0 1.25 2.0),
                            :label-set-frequency-sd 0.4629100498862757,
                            :label-set-frequency-skewness 0.945108018517813,
                            :label-sets {#{"Cold"} 1,
                                         #{"Pollen Allergy" "Asthma" "Cold"} 1,
                                         #{"Flu"} 2,
                                         #{"Hay Fever" "Flu" "Asthma"} 2,
                                         #{"Hay Fever"} 1,
                                         #{"Hay Fever" "Flu" "Cold"} 1,
                                         #{"Asthma"} 1,
                                         #{"Pollen Allergy"} 1}}}
```
If ``:include-testing?`` equals ``true``, then the test data for each fold is also stored in the same form, analogous to the training examples.

```clojure 
(iterative-stratification-n-fold (generate-x-multiple-random-data 10 ["Flu" "Cold" "Asthma" "Hay Fever" "Pollen Allergy"] [1 3]) 2 :include-testing? true)
=>
{0 {:training #{["30b0c3141520787f0447bf5acab3bb6d6e382617e7f1fc6e9bf39916e164ff8c42d1195aa9062d8301b3cc98efa38d51980b9e74894125291f84454530238d56"
                 #{"Hay Fever" "Flu" "Cold"}]
                ["f37fec0b805b0a960b851ddcbb6a92df2665e70191e0f027dad5dd3df3716be35464803563c5243a0f72ada994ecb1716130742bb2effb62e931e75a0c71bea6"
                 #{"Cold"}]
                ["65e2c37df533addaa529e0f29f39d91bedf8a453defdb9f4f50c246c94e77e057ccb3c7566078a19af2914964b3aa35f8adfaff2cf1978207eee9e6fd4bc28cd"
                 #{"Pollen Allergy" "Hay Fever" "Asthma"}]
                ["e18ac74fa0ca3aae584bb22689bf3cb5437c243cd10d9114c4de91acaf767fa3923a9551541986a0d02ca9854adf08a027abaf595a3bdc5cd4529d69675295e1"
                 #{"Pollen Allergy" "Hay Fever" "Flu"}]
                ["7225a67b1add2d10b828c68a3fb6d88dd68796bc9a60a44d22e1fafb40f91f5a44d9200d5de2269ee47611bbe8d18f325e76d9e5bf0f9b6ef8dc2f390ed6411a"
                 #{"Pollen Allergy" "Hay Fever" "Asthma"}]},
    :testing (["6d4754f15a1d6980fea64952ecca34439276327dce7bcbf54ec373d6f659ecedd01b6791ed4b0488891d6f1c6b091f2d4e546b71bc03c4660a63454544b5cb85"
               #{"Hay Fever"}]
              ["929ca67c6aff2138a1b008409af053f95be60fff38381df133016ca5e5b5b21d6e7a634ff50ad11cc1b4a0f5e300c6a9ef03f33acd20d5311c094281a4881903"
               #{"Flu"}]
              ["6f601c95f5ebe39eb1858fb453404b300cfff2ebd9031ed9c32c10af8a7ea3489c41dd692cc38f8a6edda5340c64baf9d9fa58311738bd161a3b48723cbe14c9"
               #{"Pollen Allergy" "Flu" "Cold"}]
              ["dd58c997d2ba7a703b55dd88717cb285cc0e62b5de5bca775c9f37bbdf35d6f9921029427b2dbb449a2cc158a2dab3cbf6bf64dad56a0ea0681c15a76ac6f0e2"
               #{"Hay Fever" "Asthma" "Cold"}]
              ["ef6e334568d2fceab3f76998f175aacf7bc44c3a308ae0c2ceb18bf1861add3cadab153640534f10367621b23e64352c92567bf509e0ce0766b120f147576aec"
               #{"Pollen Allergy" "Asthma" "Cold"}])},
 1 {:training #{["6d4754f15a1d6980fea64952ecca34439276327dce7bcbf54ec373d6f659ecedd01b6791ed4b0488891d6f1c6b091f2d4e546b71bc03c4660a63454544b5cb85"
                 #{"Hay Fever"}]
                ["6f601c95f5ebe39eb1858fb453404b300cfff2ebd9031ed9c32c10af8a7ea3489c41dd692cc38f8a6edda5340c64baf9d9fa58311738bd161a3b48723cbe14c9"
                 #{"Pollen Allergy" "Flu" "Cold"}]
                ["929ca67c6aff2138a1b008409af053f95be60fff38381df133016ca5e5b5b21d6e7a634ff50ad11cc1b4a0f5e300c6a9ef03f33acd20d5311c094281a4881903"
                 #{"Flu"}]
                ["ef6e334568d2fceab3f76998f175aacf7bc44c3a308ae0c2ceb18bf1861add3cadab153640534f10367621b23e64352c92567bf509e0ce0766b120f147576aec"
                 #{"Pollen Allergy" "Asthma" "Cold"}]
                ["dd58c997d2ba7a703b55dd88717cb285cc0e62b5de5bca775c9f37bbdf35d6f9921029427b2dbb449a2cc158a2dab3cbf6bf64dad56a0ea0681c15a76ac6f0e2"
                 #{"Hay Fever" "Asthma" "Cold"}]},
    :testing (["f37fec0b805b0a960b851ddcbb6a92df2665e70191e0f027dad5dd3df3716be35464803563c5243a0f72ada994ecb1716130742bb2effb62e931e75a0c71bea6"
               #{"Cold"}]
              ["65e2c37df533addaa529e0f29f39d91bedf8a453defdb9f4f50c246c94e77e057ccb3c7566078a19af2914964b3aa35f8adfaff2cf1978207eee9e6fd4bc28cd"
               #{"Pollen Allergy" "Hay Fever" "Asthma"}]
              ["e18ac74fa0ca3aae584bb22689bf3cb5437c243cd10d9114c4de91acaf767fa3923a9551541986a0d02ca9854adf08a027abaf595a3bdc5cd4529d69675295e1"
               #{"Pollen Allergy" "Hay Fever" "Flu"}]
              ["7225a67b1add2d10b828c68a3fb6d88dd68796bc9a60a44d22e1fafb40f91f5a44d9200d5de2269ee47611bbe8d18f325e76d9e5bf0f9b6ef8dc2f390ed6411a"
               #{"Pollen Allergy" "Hay Fever" "Asthma"}]
              ["30b0c3141520787f0447bf5acab3bb6d6e382617e7f1fc6e9bf39916e164ff8c42d1195aa9062d8301b3cc98efa38d51980b9e74894125291f84454530238d56"
               #{"Hay Fever" "Flu" "Cold"}])}}
``` 

## References
<a id="1">[1]</a>
Herrera, Francisco, Francisco Charte Ojeda, Antonio J. Rivera, and María J. Del Jesus. 2016. Multilabel classification: problem analysis, metrics and techniques.

<a id="1">[2]</a>
David Goldberg. 1991. [What Every Computer Scientist Should Know About Floating-Point Arithmetic](https://docs.oracle.com/cd/E19957-01/806-3568/ncg_goldberg.html). issue of Computing Surveys. Copyright 1991, Association for Computing Machinery, Inc.


<a id="1">[3]</a>
Sechidis, Konstantinos and Tsoumakas, Grigorios and Vlahavas, Ioannis. 2011. On the stratification of multi-label data. In "Joint European Conference on Machine Learning and Knowledge Discovery in Databases". Pages 145-158, Springer.  


## License
Copyright (C) 2021 Institut für medizinische und pharmazeutische Prüfungsfragen.
Licensed under the [GNU General Public License 3](https://www.gnu.org/licenses/gpl-3.0.de.html).

Please quote as: Núñez, A.; Lindner, M. (2022): Multi-Labeling Tool, https://github.com/IMPP-Mainz/impp-melt, DOI: [![DOI](https://zenodo.org/badge/414500661.svg)](https://zenodo.org/badge/latestdoi/414500661)

