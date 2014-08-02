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


(defroutes app-routes 
  (context "/library" [] 
    (defroutes library-routes
      (GET "/" [] (get-recent-books))
      (POST "/" {body :body} (create-new-book body))))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))


