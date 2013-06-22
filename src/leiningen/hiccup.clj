(ns leiningen.hiccup
  (:require [hickory.core :refer [parse-fragment as-hiccup]]
            [hiccup.core :refer [html]]
            [leiningen.transformer :refer [watch-and-transform]]
            [leiningen.globals :refer [create-future run-action]]))

(defn string->hiccup
  [v]
  (->> (parse-fragment v)
       (map as-hiccup)
       first))

(defn string->html
  [s]
  (-> s read-string html))

(def html->hiccup (watch-and-transform string->hiccup "clj"))

(def hiccup->html (watch-and-transform string->html "html"))

(defmethod run-action :html->hiccup
  [[_ read-dir write-dir]]
  (create-future html->hiccup read-dir write-dir))

(defmethod run-action :hiccup->html
  [[_ read-dir write-dir]]
  (create-future hiccup->html read-dir write-dir))
