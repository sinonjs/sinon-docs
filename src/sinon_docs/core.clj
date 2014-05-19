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

(defn api-version [file]
  (mapv read-string (str/split (:version file) #"\.")))

(defn releases []
  (->> "resources/public/releases"
       clojure.java.io/file
       file-seq
       (filter #(re-find #"sinon-\d\.\d+\.\d+" (.getPath %)))
       (map #(let [matches (re-find #"(\d\.\d+\.\d+), (\d\d\d\d.\d\d.\d\d)" (slurp %))]
               {:version (nth matches 1) :date (nth matches 2) :file (.getPath %)}))
       (sort-by api-version)
       reverse))

(def get-releases (memoize releases))

(defn historic-releases []
  (rest (get-releases)))

(defn current-release []
  (first (get-releases)))

(def global-page-vars {:current-version (:version (current-release))
                       :current-date (:date (current-release))})

(defn interpolate [markup vars]
  (sniptest markup [any-node] (replace-vars vars)))

(defn highlight-code-blocks [markup]
  (sniptest markup [:pre.code-snippet] (fn [node] (highlight node {:class "codehilite"}))))
