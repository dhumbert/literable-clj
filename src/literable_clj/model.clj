(ns literable-clj.model
  (:use cheshire.core)
  (:require [clj-http.client :as client]
  			[environ.core :refer [env]]))


(defn- extract-docs [response]
	(map :doc response))


(defn- clean-docs [docs]
	(map #(dissoc %1 :_id :_rev) docs))


(defn- get-couchdb-response [endpoint query-params]
	(cheshire.core/parse-string 
		(:body 
			(client/get (str (env :database-url) endpoint) {:query-params query-params}))
		true))


(defn- get-couchdb-view [design-doc view query-params]
	(:rows (get-couchdb-response (str "/_design/" design-doc "/_view/" view) query-params)))


(defn get-recent-books [limit]
	(clean-docs 
		(extract-docs 
			(get-couchdb-view "lists" "by_date" {:descending true :limit limit :include_docs true}))))