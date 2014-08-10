(ns literable-clj.model
  (:require [cheshire.core :as cheshire]
  			[clojure.tools.logging :as log]
  			[clojure.string :refer [lower-case]]
  			[literable-clj.couchdb :as couchdb]))


(defn get-recent-books [page limit]
	(couchdb/get-paginated-view "date" "books" page limit {:descending true :include_docs true}))


(defn get-book-by-slug [slug]
	(couchdb/get-doc slug))


(defn get-books-by-genre [genre limit]
	(couchdb/get-paginated-view "list" "genre" 1 limit {:include_docs true :key (cheshire/generate-string (lower-case genre))}))


(defn get-books-by-tag [tag limit]
	(couchdb/get-paginated-view "list" "tag" 1 limit {:include_docs true :key (cheshire/generate-string (lower-case tag))}))