(ns sinon-docs.core
  (:require [clojure.string :as str]
            [me.raynes.cegdown :as md]
            [net.cgrand.enlive-html :refer [sniptest any-node replace-vars html]]
            [sinon-docs.highlight :refer [highlight]]))

(defn slurp-resource [file]
  (slurp (clojure.java.io/resource file)))

(defn load-edn-file [file]
  (let [content (slurp-resource file)
        forms (try
                (read-string (str "[" (str/trim content) "]"))
                (catch Exception e
                  (throw (Exception. (str "Error in " file ": " (.getMessage e))))))]
    (when (> (count forms) 1)
      (throw (Exception. (str "File " file " should contain only a single map, but had " (count forms) " forms."))))
    (first forms)))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough :quotes :smarts])

(defn to-html [s]
  (md/to-html s pegdown-options))

(defn releases []
  (load-edn-file "releases.edn"))

(defn historic-releases []
  (:historic (releases)))

(defn current-release []
  (:sinon (releases)))

(def global-page-vars {:current-version (:version (current-release))})

(defn interpolate [markup vars]
  (sniptest markup [any-node] (replace-vars vars)))

(defn highlight-code-blocks [markup]
  (sniptest markup [:pre.code-snippet] (fn [node] (highlight node {:class "codehilite"}))))
