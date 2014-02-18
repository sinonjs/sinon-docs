(ns sinon-docs.pages
  (:require [clojure.string :as str]
            [net.cgrand.enlive-html :refer [sniptest any-node replace-vars html]]
            [sinon-docs.core :as sinon]
            [sinon-docs.docs :refer [docs-page]]
            [sinon-docs.page :refer [page]]
            [sinon-docs.highlight :refer [highlight]]))

(defn- insert-download-button [html current-release]
  (let [version (:version current-release)
        date (:date current-release)]
    (str/replace html "<div class=\"download-link-placeholder\"/>"
                 (str "<div class=\"button\"><a href=\"releases/sinon-" version
                      ".js\">Download Sinon.JS " version "</a></div><p>"
                      date " - <a href=\"Changelog.txt\">Changelog</a> - "
                      "<a href=\"/download/\">More builds/versions</a></p>"))))

(defn- highlight-code-blocks [markup]
  (sniptest markup [:pre.code-snippet] (fn [node] (highlight node {:class "codehilite"}))))

(defn- interpolate [markup vars]
  (sniptest markup [any-node] (replace-vars vars)))

(defn- historic-download-links [releases]
  (map #(vector :li (list [:a {:href (str "/releases/sinon-" (:version %) ".js")}
                           (str "Sinon.JS " (:version %))]
                          " (" (:date %) ")")) releases))

(defn- insert-historic-download-links [markup releases]
  (sniptest markup [:ul.old-versions]
            (fn [node] (assoc node :content (html (historic-download-links releases))))))

(def page-vars {:current-version (:version (sinon/current-release))})

(defn frontpage [context]
  (let [current-release (sinon/current-release)]
    (page
     context
     "Documentation"
     {:body-class "front"}
     (-> (slurp "resources/partials/index.html")
         (insert-download-button current-release)
         highlight-code-blocks))))

(defn download-page [context]
  (page
   context
   "Downloads"
   (-> (slurp "resources/partials/download.html")
       (interpolate page-vars)
       (insert-historic-download-links (sinon/historic-releases)))))

(defn get-pages []
  {"/" frontpage
   "/download/" download-page
   "/docs/" docs-page})
