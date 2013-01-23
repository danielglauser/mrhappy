(ns mrhappy.websocket-server
  (:require [sentimental.core :as sentimental]
            [laeggen.core :as laeggen]
            [laeggen.dispatch :as dispatch]
            [lamina.core :as lamina]))

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

(defn ^:async analyze-email [{:keys [channel] :as request}]
  (let [dir (clojure.java.io/file "data/email")
        files (file-seq dir)]
    (println (str "Found " (count files) " file(s)"))
    (lamina/enqueue channel "{\"what\":true}")
    #_(map #(-> %
                slurp
                analyze)
           (rest files))))

(defn serve-index [request]
  (slurp "src/assets/index.html"))

(def urls
  (dispatch/urls
   #"^/$" #'serve-index
   #"^/analyze-email/$" #'analyze-email))

(defn -main []
  (laeggen/start {:port 8080
                  :append-slash? false
                  :urls urls
                  :websocket true}))

;; (swap! laeggen/routes assoc 8080 (dispatch/merge-urls laeggen.views/default-urls urls))
