(ns mrhappy.websocket-server
  (:require [sentimental.core :as sentimental]
            [laeggen.core :as laeggen]
            [laeggen.dispatch :as dispatch]
            [lamina.core :as lamina])
  (:import (java.util.concurrent Executors TimeUnit)))

(defonce broadcast-channel (lamina/channel* :grounded? true
                                            :permanent? true))

(defonce analyzed-emails (atom nil))

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
  (let [sentiment (sentimental/categorize text)
        subject (parse-subject text)]
    {:subj subject :sentiment (convert-to-percentage sentiment)}))

(defn ^:async subscribe [{:keys [channel] :as request}]
  (lamina/siphon broadcast-channel channel))

(defn serve-index [request]
  (slurp "src/assets/index.html"))

(def urls
  (dispatch/urls
   #"^/$" #'serve-index
   #"^/analyze-email/$" #'subscribe))

(defonce executor (Executors/newScheduledThreadPool 1))

(defn ballsify! []
  "Sends the first element of the seq down the broadcast-channel and fearlessly mutates
the analyzed emails moving the first element to the end of the seq."
  (let [first-chunk (first @analyzed-emails)
        rest-chunks (rest @analyzed-emails)]
    (lamina/enqueue broadcast-channel first-chunk)
    (swap! analyzed-emails #(conj rest-chunks first-chunk))))

(defn compute-sentiment! []
  (let [dir (clojure.java.io/file "data/email")
        files (file-seq dir)]
    (println (str "Found " (count files) " file(s)"))
    (reset! analyzed-emails
            (map #(-> %
                      slurp
                      analyze)
                 (rest files)))))

(defn -main []
  (compute-sentiment!)
  (laeggen/start {:port 8080
                  :append-slash? false
                  :urls urls
                  :websocket true})
  (.scheduleAtFixedRate executor #'ballsify! 0 1000 TimeUnit/MILLISECONDS))

;; (-main)

;; (swap! laeggen/routes assoc 8080 (dispatch/merge-urls laeggen.views/default-urls urls))
