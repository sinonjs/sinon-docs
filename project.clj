(defproject sinon-docs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://sinonjs.org"
  :license {:name "The BSD 2-Clause License"
            :url "http://opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [stasis "0.7.0"]
                 [ring "1.2.1"]
                 [optimus "0.14.2"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [clygments "0.1.1"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler sinon-docs.web/app}
  :aliases {"build-site" ["run" "-m" "sinon-docs.web/export"]}
  :profiles {:test {:dependencies [[midje "1.6.0"]]
                    :plugins [[lein-midje "3.1.3"]]}})
