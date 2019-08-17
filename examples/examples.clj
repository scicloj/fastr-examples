(ns examples
  (:require [fastr-examples.core :as r])
  (:import (org.graalvm.polyglot Context Context$Builder Source Value)
           (org.graalvm.polyglot.proxy ProxyArray ProxyObject)))

(-> "1+2"
    r/eval!
    r/->clj)
;; => 3.0

(->> "list(a=3, b=9)"
     r/eval!
     r/->clj)
;; => {:a 3.0, :b 9.0}

(->> [10 20 30 40]
     r/proxy-array
     ((r/function "function(x) mean(as.numeric(x))")))
;; => 25.0

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

