(ns sinon-docs.highlight-test
  (:require [sinon-docs.highlight :refer :all]
            [midje.sweet :refer :all]
            [net.cgrand.enlive-html :refer [select]]))

(fact "Highlights code samples"
      (-> (highlight {:tag :pre,
                      :attrs {:class "syntax-highlight"
                              :data-lang "javascript"},
                      :content
                      [{:tag :code,
                        :attrs nil,
                        :content
                        ["var a = 42;"]}]} {:class "codehilite"})
          (select [:pre.codehilite.javascript :code :.nx])
          first
          :content) => '("a"))
