(ns leiningen.hiccup
  (:require [hickory.core :refer [parse-fragment as-hiccup]]
            [hiccup.core :refer [html]]
            [leiningen.watch :refer [watch-folder]]))

(defn convert-to-hiccup
  [v]
  (->> (parse-fragment v)
       (map as-hiccup)
       first))

(defn convert-to-html
  [s]
  (html s))

(defn html->hiccup
  [dir-to-watch-path write-dir]
  (watch-folder dir-to-watch-path (fn [[file-path file-name]]
                                    (->> file-path slurp convert-to-hiccup (spit (str write-dir write-dir file-name ".clj"))))))

(defn hiccup->html
  [path]
  (watch-folder path convert-to-hiccup))

;(-> (convert-to-hiccup "<div><a>ha</a></div>") convert-to-html)
