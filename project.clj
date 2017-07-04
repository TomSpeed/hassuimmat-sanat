(defproject hassuimmat-sanat "0.1.0-SNAPSHOT"
  :description "A solution to wunderdog's first wundernut"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main hassuimmat-sanat.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [uswitch/lambada "0.1.2"]]
  :resource-paths ["resources/prod"]
  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                    :plugins [[lein-midje "3.2"]]
                    :resource-paths ["resources/test"]}
             :midje {}
             :uberjar {:aot [hassuimmat-sanat.core]}})
