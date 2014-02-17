(ns sinon-docs.highlight
  (:require [net.cgrand.enlive-html :refer [sniptest html-resource select]]
            [clygments.core :as pygments]))

(defn- extract-code [highlighted]
  (-> highlighted
      (java.io.StringReader.)
      (html-resource)
      (select [:pre])
      first
      :content))

(defn highlight
  ([node] (highlight node {}))
  ([node options]
     (let [code (apply str (get-in node [:content 0 :content]))
           lang (get-in node [:attrs :data-lang])]
       {:tag :pre
        :attrs {:class (str (:class options) " " lang)}
        :content [{:tag :code
                   :attrs nil
                   :content (-> code
                                (pygments/highlight (keyword lang) :html)
                                (extract-code))}]})))
