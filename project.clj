(defproject mrhappy "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [org.clojars.gnarmis/sentimental "0.1.1-SNAPSHOT"]]
  :plugins [[lein-ring "0.8.2"]
            [lein-swank "1.4.4"]]
  :ring {:handler mrhappy.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
