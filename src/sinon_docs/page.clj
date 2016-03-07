(ns sinon-docs.page
  (:require [hiccup.page :refer [html5]]
            [optimus.link :as link]
            [sinon-docs.core :as sinon]))

(defn- current-year []
  (+ 1900 (.getYear (java.util.Date.))))

(defn page
  ([request title body] (page request title {} body))
  ([request title options body]
     (-> (html5
          [:head
           [:meta {:charset "utf-8"}]
           [:meta {:name "google-site-verification" :content "mkRakJ_AGQ2BuUur1LqqqHh-x3wFVnE_UmPxLFxGHro"}]
           [:title (str "Sinon.JS - " title)]
           [:link {:rel "stylesheet" :type "text/css" :href (link/file-path request "/styles/sinon.css")}]]
          [:body {:class (:body-class options)}
           [:div.payoff
            [:h1 [:a {:href "/"} "Sinon.JS"]]
            [:p "Standalone test spies, stubs and mocks for JavaScript.<br>
          No dependencies, works with any unit testing framework."]]
           body
           [:div.aside
            [:p (list "Sinon uses "
                      [:a {:href "http://semver.org/"} "Semantic versioning"]
                      ".")]
            [:p (list "Copyright 2010 - "
                      (current-year)
                      ", "
                      [:a {:href "http://cjohansen.no/"} "Christian Johansen"]
                      ". Released under the "
                      [:a {:href "http://www.opensource.org/licenses/bsd-license.php"} "BSD license"]
                      ".")]]
           [:script {:src (link/file-path request "/scripts/sinon-web.js")}]
           [:script {:src "/releases/sinon.js"}]
           [:script {:src "/releases/sinon-ie.js"}]])
         (sinon/interpolate sinon/global-page-vars)
         sinon/highlight-code-blocks)))
