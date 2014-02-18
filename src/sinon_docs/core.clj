(ns sinon-docs.core
  (:require [clojure.string :as str]
            [me.raynes.cegdown :as md]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough :quotes :smarts])

(defn to-html [s]
  (md/to-html s pegdown-options))

(defn load-edn-file [file]
  (let [content (slurp file)
        forms (try
                (read-string (str "[" (str/trim content) "]"))
                (catch Exception e
                  (throw (Exception. (str "Error in " file ": " (.getMessage e))))))]
    (when (> (count forms) 1)
      (throw (Exception. (str "File " file " should contain only a single map, but had " (count forms) " forms."))))
    (first forms)))

(defn releases []
  (load-edn-file "resources/releases.edn"))

(defn current-release []
  (:sinon (releases)))

(defn historic-releases []
  (:historic (releases)))
