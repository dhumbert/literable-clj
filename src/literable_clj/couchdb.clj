(ns literable-clj.couchdb
  (:require [clj-http.client :as client]
  			[cheshire.core :as cheshire]
  			[clojure.tools.logging :as log]
  			[environ.core :refer [env]]))


(defn- get-couchdb-response 
	([endpoint] (get-couchdb-response endpoint {}))
	([endpoint query-params]
		(log/warn (str (env :database-url) "/" endpoint))
		(log/warn query-params)
		(cheshire/parse-string 
			(:body (client/get (str (env :database-url) "/" endpoint) {:query-params query-params}))
			true)))


(defn- get-couchdb-view [design-doc view query-params]
	(:rows (get-couchdb-response (str "_design/" design-doc "/_view/" view) query-params)))


; todo: actually implement this.
(defn get-paginated-couchdb-view [design-doc view page limit query-params]
	(let [docs (get-couchdb-view design-doc view query-params)] ; get one more than the limit
		{:books (map :doc docs)
		 :next-key (:key (last docs))}))


(defn get-couchdb-doc [id]
	(get-couchdb-response id))