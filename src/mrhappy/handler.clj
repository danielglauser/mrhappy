(ns mrhappy.handler
  (:use [compojure.core]
        [ring.middleware.resource]
        [ring.middleware.file]
        [sentimental.core])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cheshire.core :refer :all]))

(defn parse-subject
  [text] (re-find #"Subject: " text))

(defn analyze [text]
  (let [sentiment (categorize text)
        subject (parse-subject text)]
    {:subj subject :sentiment sentiment}))

(defn convert-to-percentage
  "Takes in one of the following forms and maps it to a percentage of happiness:
strongsubj-positive 100%
weaksubj-positive    80%
strongsubj-neutral   60%
weaksubj-neutral     40%
weaksubj-negative    20%
strongsubj-negative   0%
"
  [str]
  (cond
    (= "strongsubj-positive" str) 100
    (= "weaksubj-positive") 80
    (= "strongsubj-neutral") 60
    (= "weaksubj-neutral") 40
    (= "weaksubj-negative") 20
    (= "strongsubj-negative") 0))

(defn to-json
  "Takes a seq of percentages and returns json of the form: {:emails [{:subj foo :sentiment 80}, {:subj bar :sentiment 20}, ...]}"
  [per-seq] )

(defn analyze-email []
  (let [dir (clojure.java.io/file "data/email")
        files (file-seq dir)]
    (println (str "Found " (count files) " file(s)"))
    (map #(-> %
              slurp
              analyze
              convert-to-percentage
              )
         (rest files))))

(defroutes app-routes
  (GET "/analyzed-email" [] (analyze-email))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-file "public")))
