(defproject outpace-exercise "0.1.0-SNAPSHOT"
  :description "Daniel's implementation of the Outpace exercise."
  :url "https://github.com/sattvik/outpace-exercise"
  :license {:name "Proprietary"
            :distribution :manual}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot outpace-exercise.main
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.6.2"]]}
             :uberjar {:aot :all}})
