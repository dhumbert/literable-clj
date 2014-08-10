(ns literable-clj.couchdb
  (:require [clj-http.client :as client]
  			[cheshire.core :as cheshire]
  			[clojure.tools.logging :as log]
  			[clojure.set :refer [rename-keys]]
  			[literable-clj.env :refer [env]]))


(defn- rename-id-field [doc]
	(rename-keys doc {:_id :id}))


(defn- replace-urls [doc]
	(assoc doc :cover (str (env :cover-url) "/" (:cover doc)) 
			   :filename (str (env :file-url) "/" (:filename doc))))


(defn- clean-docs [docs]
	(map #(-> % :doc rename-id-field replace-urls) docs))


(defn- get-response 
	([endpoint] (get-response endpoint {}))
	([endpoint query-params]
		(cheshire/parse-string 
			(:body (client/get (str (env :database-url) "/" endpoint) {:query-params query-params}))
			true)))


(defn- get-view 
	([design-doc view] (get-view design-doc view {}))
	([design-doc view query-params] (:rows (get-response (str "_design/" design-doc "/_view/" view) query-params))))


; this is not the way you're supposed to do view-based pagination, but we have pretty small
; result sets, and the other way is way too complicated to be worth it for this application.
; see: http://guide.couchdb.org/draft/recipes.html#pagination
(defn get-paginated-view [design-doc view page limit query-params]
	(let [page (if (nil? page) 1 (read-string page))
		  docs (get-view design-doc view (assoc query-params :limit (inc limit) :skip (- (* limit page) limit)))
		  end-of-results (<= (count docs) limit)
		  next-page (if end-of-results nil (inc page))
		  prev-page (if	(= page 1) nil (dec page))
		  final-docs (if end-of-results docs (butlast docs))]

		  {:books (clean-docs final-docs)
		   :next-page next-page
		   :prev-page prev-page}))


(defn get-doc [id]
	(get-response id))