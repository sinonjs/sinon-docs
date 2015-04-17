(ns sinon-docs.pages
  (:require [clojure.string :as str]
            [net.cgrand.enlive-html :refer [sniptest html]]
            [sinon-docs.core :as sinon]
            [sinon-docs.docs :refer [docs-page]]
            [sinon-docs.page :refer [page]]))

(defn- insert-download-button [html current-release]
  (let [version (:version current-release)
        date (:date current-release)]
    (str/replace html "<div class=\"download-link-placeholder\"/>"
                 (str "<div class=\"button\"><a href=\"releases/sinon-" version
                      ".js\">Download Sinon.JS " version "</a></div><p>"
                      date " - <a href=\"Changelog.txt\">Changelog</a> - "
                      "<a href=\"/download/\">More builds/versions</a></p>"))))

(defn- historic-download-links [releases]
  (map #(vector :li (list [:a {:href (str "/releases/sinon-" (:version %) ".js")}
                           (str "Sinon.JS " (:version %))]
                          " (" (:date %) ")")) releases))

(defn- insert-historic-download-links [markup releases]
  (sniptest markup [:ul.old-versions]
            (fn [node] (assoc node :content (html (historic-download-links releases))))))

(defn frontpage [context]
  (let [current-release (sinon/current-release)]
    (page
     context
     "Documentation"
     {:body-class "front"}
     (-> (slurp "resources/partials/index.html")
         (insert-download-button current-release)))))

(defn download-page [context]
  (page
   context
   "Downloads"
   (-> (slurp "resources/partials/download.html")
       (insert-download-button (sinon/current-release))
       (insert-historic-download-links (sinon/historic-releases)))))

(defn qunit-page [context]
  (page
   context
   "Sinon.JS for QUnit"
   (slurp "resources/partials/qunit/index.html")))

(defn get-pages []
  {"/" frontpage
   "/download/" download-page
   "/docs/" docs-page
   "/qunit/" qunit-page})
