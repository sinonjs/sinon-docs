(ns sinon-docs.core
  (:require [clojure.string :as str]))

(defn- load-edn-file [file]
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
