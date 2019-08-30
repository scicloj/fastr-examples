(ns examples
  (:require [fastr-examples.core :as r]
            [gg4clj.core :refer [to-r r+]]
            [clojure.string :as string]
            [clojure.java.shell :refer [sh]])
  (:import (org.graalvm.polyglot Context Context$Builder Source Value)
           (org.graalvm.polyglot.proxy ProxyArray ProxyObject)))

;; Let us run basic R code, converting results to Clojure:

(-> "1+2"
    r/eval!
    r/->clj)
;; => 3.0

(->> "list(a=3, b=9)"
     r/eval!
     r/->clj)
;; => {:a 3.0, :b 9.0}

;; We can pass Clojure data to an R function:

(->> [10 20 30 40]
     r/proxy-array
     ((r/function "function(x) mean(as.numeric(x))")))
;; => 25.0

;; For example, we can use R for linear regression:

(let [n 30
      slope 3
      intercept -9
      xs (repeatedly n #(rand))
      ys (->> xs
              (map (fn [x]
                (-> x
                    (* slope)
                    (+ intercept)
                    (+ (rand))))))]
  (->> {:x xs
        :y ys}
       r/proxy-object-of-arrays
       ((r/function "
(function(columns)
  with(as.list(columns),
       as.list(lm(y~x)$coefficients)))"))))
;; => {:(Intercept) -8.507125181146558, :x 2.995631080884335}


;; Let us now reproduce the example from gg4clj's README
;; https://github.com/JonyEpsilon/gg4clj
;; -- demonstrating the generation of R code from Clojure data.

(defn b-m
  []
  (let [a  (rand)
        b  (rand)
        r  (Math/sqrt (* -2 (Math/log a)))
        th (* 2 Math/PI b)]
    (* r (Math/cos th))))

(def data {:g1 (repeatedly 50 b-m) :g2 (repeatedly 50 b-m)})

(set! *print-length* 5)

data
;; =>
;; {:g1
;;  (0.8240104431255278
;;   -0.7907026611222235
;;   0.2524066713817609
;;   -1.4162640389673988
;;   1.7361980659129101
;;   ...),
;;  :g2
;;  (0.8516603879057449
;;   0.040041891092059856
;;   -0.7703755934846348
;;   0.2773111323082047
;;   -1.1967455527610371
;;   ...)}


(defn r-function-code [args & r-codes-as-edn]
  (format "function(%s) {\n%s\n}"
          (->> args
               (map name)
               (string/join ","))
          (->> r-codes-as-edn
               (map to-r)
               (map #(str "\t" %))
               (string/join ";\n "))))

(println
 (r-function-code [:x]
                  [:print "hello"]
                  [:tan :x]))
;; => Printed:
;; function(x) {
;;              print ("hello") ;
;;              tan   (x)
;;              }

(->> data
     r/proxy-object-of-arrays
     ((r/function
       (r-function-code [:data]
                        [:library :ggplot2]
                        [:png "/tmp/plot1.png"]
                        [:print
                         (r+
                          [:ggplot [:data.frame :data] [:aes :g1 :g2]]
                          [:xlim -2 2]
                          [:ylim -2 2]
                          [:geom_point {:colour "steelblue" :size 4}]
                          [:stat_density2d {:colour "#FF29D2"}]
                          [:theme_bw])]
                        [:dev.off]))))

;; Open the created image:
(sh "xdg-open" "/tmp/plot1.png")



