(ns user
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint pp print-table]]
            [clojure.repl :refer :all]))

(defmacro dump-locals []
  `(clojure.pprint/pprint
    ~(into {} (map (fn [l] [`'~l l]) (reverse (keys &env))))))
