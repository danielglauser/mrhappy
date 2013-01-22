(ns mrhappy.handler
  (:use [compojure.core]
        [ring.middleware.resource]
        [ring.middleware.file])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn analyze-email []
  "bam!")

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/analyzed-email" [] (analyze-email))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-file "public")))
