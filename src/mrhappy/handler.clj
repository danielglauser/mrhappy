(ns mrhappy.handler
  (:use [compojure.core]
        [ring.middleware.resource]
        [ring.middleware.file])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-file "public")))
