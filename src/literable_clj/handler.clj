(ns literable-clj.handler
  (:use compojure.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]
            [literable-clj.model :as model]))


(defn get-recent-books []
  (response (model/get-recent-books 5)))


(defn create-new-book [body]
  nil)

(defn get-book [slug]
  (response (model/get-book-by-slug slug)))


(defn get-genre-books [genre]
  (response (model/get-books-by-genre genre 5)))


(defn get-tag-books [tag]
  (response (model/get-books-by-tag tag 5)))


(defroutes app-routes 
  (context "/library" [] 
    (defroutes library-routes
      (GET "/" [] (get-recent-books))
      (POST "/" {body :body} (create-new-book body))))
  (context "/book" []
    (defroutes book-routes
      (GET "/:slug" [slug] (get-book slug))))
  (context "/genre" []
    (defroutes genre-routes
      (GET "/:genre" [genre] (get-genre-books genre))))
  (context "/tag" []
    (defroutes tag-routes
      (GET "/:tag" [tag] (get-tag-books tag))))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))


