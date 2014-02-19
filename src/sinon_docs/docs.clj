(ns sinon-docs.docs
  (:require [sinon-docs.page :refer [page]]
            [sinon-docs.core :as sinon]
            [clojure.string :as str]))

(defn- main-nav [apis]
  [:ul.nav.main-nav
   [:li [:strong "Documentation"]]
   (map #(vector :li [:a {:href (str "#" (:id %))} (:menu-text %)]) apis)])

(defn- path [file]
  (str "docs/" file))

(defn- render-example [example]
  (if example
    [:pre.code-snippet {:data-lang "javascript"} [:code example]]))

(defn- render-description [description example]
  (list (if (not (str/blank? description))
          (sinon/to-html description))
        (render-example example)))

(defn- render-api-description [api]
  (if (string? (:description api))
    (render-description (:description api) (:example api))
    (map #(render-description (:text %) (:example %)) (:description api))))

(defn- api-properties [properties]
  [:dl.dafuq
   (map #(list [:dt {:id (:id %)} [:code (:name %)]]
               [:dd (render-api-description %)]) properties)])

(defn- render-api [api]
  (list [:h3 (:title api)]
        [:a {:name (name (:id api))}]
        [:p (:introduction api)]
        (api-properties (:properties api))))

(defn- render-api-section [api-section]
  (let [section (sinon/load-edn-file (path (str (:id api-section) ".edn")))]
    [:div.section {:id (name (:id api-section))}
     (list [:h2 (list (:title section) " "
                      [:a.api {:href (str "#" (:id api-section) "-api")} "API reference"])]
           (sinon/to-html (or (:introduction section)
                              (sinon/slurp-resource (path (:introduction-file section)))))
           (map render-api (:apis section)))]))

(defn- docs-content [apis]
  [:div.docs
   [:p "This page contains the entire Sinon.JS API documentation along with brief
    introductions to the concepts Sinon implements. Please ask questions on
    <a href=\"http://groups.google.com/group/sinonjs\">the mailing list</a> if
    you're stuck. I also really appreciate suggestions to improve the
    documentation so Sinon.JS is easier to work with. Get in touch!"]
   (map render-api-section apis)])

(defn docs-page [context]
  (let [apis (sinon/load-edn-file (path "index.edn"))]
    (page
     context
     "Documentation"
     (list (main-nav apis)
           (docs-content apis)))))
