(ns literable-clj.model
  (:require [clj-http.client :as client]
  			[cheshire.core :as cheshire]
  			[clojure.tools.logging :as log]
  			[clojure.string :refer [lower-case]]
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
(defn- get-paginated-couchdb-view [design-doc view page limit query-params]
	(let [docs (get-couchdb-view design-doc view query-params)] ; get one more than the limit
		{:books (map :doc docs)
		 :next-key (:key (last docs))}))


(defn get-recent-books [limit]
	(get-paginated-couchdb-view "date" "books" 1 limit {:descending true :include_docs true}))


(defn get-couchdb-doc [id]
	(get-couchdb-response id))


(defn get-book-by-slug [slug]
	(get-couchdb-doc slug))


(defn get-books-by-genre [genre limit]
	(get-paginated-couchdb-view "list" "genre" 1 limit {:include_docs true :key (cheshire/generate-string (lower-case genre))}))


(defn get-books-by-tag [tag limit]
	(get-paginated-couchdb-view "list" "tag" 1 limit {:include_docs true :key (cheshire/generate-string (lower-case tag))}))