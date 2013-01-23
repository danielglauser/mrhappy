(ns "Get all files in the data/email directory, replace all the subjects
by cycling through a seq of loaded subjects."
  mrhappy.gen-test-data)

(defonce bag-o-subjects (atom []))


(defn rotate
  "Given a seq rotates the first value to be the last value."
  [init-seq]
  (let [init-vec (vec init-seq)
        first-vec (first init-vec)
        rest-vec (vec (rest init-vec))]
    (conj rest-vec first-vec)))


(defn load-subjects
  "Load a seq of subjects from a file. Subjects are expected to be one per line"
  [path]
  (with-open [rdr (clojure.java.io/reader path)]
    (doseq [line (line-seq rdr)]
      (swap! bag-o-subjects (fn [state] (conj state line))))))

(defn change-subject [text]
  (let [new-subject (first (@bag-o-subjects))]
    (swap! )
    (replace text #"Subject: .+" (str "Subject: " new-subject))))

(defn change-subjects! []
  (let [dir (clojure.java.io/file "data/email")
        files (file-seq dir)]
    (println (str "Changing subject in " (count files) " email file(s)"))

    (map #(-> %
              slurp
              change-subject)
         (rest files))))
