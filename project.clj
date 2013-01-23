(defproject mrhappy "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [cheshire "5.0.1"]
                 [org.clojars.dsabanin/webbit "0.4.15-SNAPSHOT"]
                 [org.clojars.gnarmis/sentimental "0.1.1-SNAPSHOT"]
                 [laeggen "0.5"]]
  :plugins [[lein-ring "0.8.2"]
            [lein-swank "1.4.4"]]
  :ring {:handler mrhappy.handler/app}
  :jvm-opts ["-Xmx1024M" "-XX:MaxPermSize=128M"  "-server"]
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}}
  :main mrhappy.websocket-server)
