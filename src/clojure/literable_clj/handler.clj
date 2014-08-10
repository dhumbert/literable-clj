(ns literable-clj.handler
  (:use compojure.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]
            [literable-clj.model :as model]
            [ring.middleware.cors :refer [wrap-cors]]))


(defn get-recent-books [page]
  (response (model/get-recent-books page 8)))


(defn create-new-book [body]
  nil)

(defn get-book [slug]
  (response (model/get-book-by-slug slug)))


(defn get-genres []
  (response (model/get-genres)))

(defn get-genre-books [genre]
  (response (model/get-books-by-genre genre 5)))


(defn get-tag-books [tag]
  (response (model/get-books-by-tag tag 5)))


(defroutes app-routes 
  (context "/recent" [] 
    (defroutes library-routes
      (GET "/" {params :query-params} (get-recent-books (get params "page")))))
  (context "/book" []
    (defroutes book-routes
      (GET "/:slug" [slug] (get-book slug))))
  (context "/genre" []
    (defroutes genre-routes
      (GET "/" [] (get-genres))
      (GET "/:genre" [genre] (get-genre-books genre))))
  (context "/tag" []
    (defroutes tag-routes
      (GET "/:tag" [tag] (get-tag-books tag))))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api (wrap-cors app-routes :access-control-allow-origin #"http://localhost:9000"
                       :access-control-allow-methods [:get :put :post :delete]))
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))


