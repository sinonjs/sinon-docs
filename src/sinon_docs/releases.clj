(ns sinon-docs.releases)

(def re-version #"-(\d\.\d+\.\d+)")

(defn get-releases [directory]
  (filter #(re-find re-version (.getName %))
          (file-seq (clojure.java.io/file ))))

(defn versions [files]
  (map #(->> % .getName (re-find re-version) second) files))
