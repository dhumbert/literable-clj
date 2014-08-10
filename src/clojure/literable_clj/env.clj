(ns literable-clj.env
  (:require [environ.core]))


(defn env [k]
	(k (environ.core/env :literable)))