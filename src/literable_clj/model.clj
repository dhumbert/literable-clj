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


; todo: finish this. it's not quite right and it's not quite done.
(defn- get-paginated-couchdb-view [design-doc view page limit query-params]
	(let [docs (get-couchdb-view design-doc view (assoc query-params :limit (inc limit)))] ; get one more than the limit
		{:books (clean-docs (extract-docs (butlast docs)))
		 :next-key (:key (last docs))}))


(defn get-recent-books [limit]
	(get-paginated-couchdb-view "lists" "by_date" 1 limit {:descending true :include_docs true}))