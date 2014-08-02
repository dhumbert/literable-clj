(defproject literable-clj "0.1.0-SNAPSHOT"
  :description "Web-based ebook management"
  :url "http://literable.co"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [ring/ring-json "0.3.1"]
                 [cheshire "5.3.1"]
                 [clj-http "0.9.2"]
                 [environ "0.5.0"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler literable-clj.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
