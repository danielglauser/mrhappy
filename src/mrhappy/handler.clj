(ns mrhappy.handler
  (:use [compojure.core]
        [ring.middleware.resource]
        [ring.middleware.file]
        [sentimental.core])
  (:import [org.webbitserver WebServer WebServers WebSocketHandler]
            [org.webbitserver.handler StaticFileHandler])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cheshire.core :refer :all]))

(defn parse-subject
  [text] (re-find #"Subject: " text))

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

(defn analyze [text]
  (let [sentiment (categorize text)
        subject (parse-subject text)]
    {:subj subject :sentiment (convert-to-percentage sentiment)}))

(defn to-json
  "Takes a seq of percentages and returns json of the form: {:emails [{:subj foo :sentiment 80}, {:subj bar :sentiment 20}, ...]}"
  [per-seq] )

(defn analyze-email []
  (let [dir (clojure.java.io/file "data/email")
        files (file-seq dir)]
    (println (str "Found " (count files) " file(s)"))
    (map #(-> %
              slurp
              analyze)
         (rest files))))

(defroutes app-routes
  (GET "/analyzed-email" [] (analyze-email))
  (route/not-found "Not Found"))

(def webserver (atom nil))
(def chanels (atom '()))


(defn stop-server []
  (.stop @webserver)
  (reset! webserver nil))

(defn websocket-server [passthrough]
  (reset! webserver
          (doto (WebServers/createWebServer 8080)
            (.add "/websocket"
                  (proxy [WebSocketHandler] []
                    (onOpen [c] (do
                                  (prn "opened" c)
                                  (swap! chanels conj c)))
                    (onClose [c] (do
                                   (prn "closed" c)
                                   (swap! chanels (fn [v] (remove #(= %1 c) v)))))
                    (onMessage [channel msg] (println msg))))

            (.add (StaticFileHandler. "./public/"))
            (.start)))
  passthrough)

(defn ping [passthrough]
  (doseq [channel @chanels]
    (.send channel "Woop, there it is!"))
  passthrough)

(def app
  (-> app-routes
      (wrap-file "public")
      websocket-server
      ping))
