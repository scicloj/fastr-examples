(ns fastr-examples.core
  (:require [com.rpl.specter :as specter])
  (:import (org.graalvm.polyglot Context Context$Builder Source Value)
           (org.graalvm.polyglot.proxy ProxyArray ProxyObject)))

(set! *warn-on-reflection* true)

(def context-builder
  (doto ^Context$Builder (Context/newBuilder (into-array String ["R"]))
    (.allowAllAccess true)
    (.allowNativeAccess true)))

(def context
  (delay (.build ^Context$Builder context-builder)))

(defn eval! [r-code]
  (.eval ^Context @context "R" r-code))

(defn ->clj [^Value v]
  (cond (.isNumber v) (.asDouble v)
        (.hasMembers v) (->> v
                             (.getMemberKeys)
                             (map (fn [k]
                                    [(keyword k)
                                     (->> k
                                          (.getMember v)
                                          ->clj)]))
                             (into {}))
        :else         v))

(defn function [r-code]
  (let [f ^Value (eval! r-code)]
    (assert (.canExecute f))
    (fn [& proxy-args]
      (->>  (into-array proxy-args)
            (.execute f)
            ->clj))))

(defn proxy-array [xs]
  (-> xs
      into-array
      (ProxyArray/fromArray)))

(defn proxy-object [m]
  (-> m
      (ProxyObject/fromMap)))

(defn proxy-object-of-arrays [map-of-vectors]
  (->> map-of-vectors
       (specter/transform [specter/MAP-VALS]
                          proxy-array)
       (specter/transform [specter/MAP-KEYS]
                          name) ; keys to Strings
       proxy-object))

