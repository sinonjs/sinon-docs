(ns sinon-docs.web
  (:require [ring.middleware.content-type :refer [wrap-content-type]]
            [stasis.core :as stasis]
            [optimus.prime :as optimus]
            [optimus.assets :as assets]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :refer [serve-live-assets]]
            [optimus.export]
            [sinon-docs.pages :as pages]))

(defn get-assets []
  (assets/load-assets "public" [#"/styles/.*\.css"
                                #"/images/.*"
                                #"/scripts/.*\.js"]))

(defn get-pages []
  (merge (pages/get-pages)
         (stasis/slurp-directory "resources/public" #"releases\/.*$")))

(def optimize optimizations/all)

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type))

(def export-dir "./dist")

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))
