(defproject outpace-exercise "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot outpace-exercise.main
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.6.2"]]}
             :uberjar {:aot :all}})
