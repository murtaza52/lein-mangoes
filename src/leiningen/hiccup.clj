(ns leiningen.hiccup
  (:require [hickory.core :refer [parse-fragment as-hiccup]]
            [hiccup.core :refer [html]]
            [leiningen.watch :refer [watch-folder]]
            [clojure.java.io :as io]))

(def watchers (agent nil))

(defn convert-to-hiccup
  [v]
  (->> (parse-fragment v)
       (map as-hiccup)
       first))

(defn convert-to-html
  [s]
  (html s))

(defn file-converter
  [f write-dir ext]
  (fn [[file-path file-name]]
    (->> file-path slurp f (spit (io/file write-dir (str file-name "." ext))))))

(def start-thread (fn [f] (send-off watchers (fn []))))

(defn html->hiccup
  [read-dir write-dir]
  (send-off watchers (fn [_]
                       (watch-folder read-dir (file-converter convert-to-hiccup write-dir "clj")))))

(defn hiccup->html
  [read-dir write-dir]
  (watch-folder read-dir (file-converter convert-to-html write-dir "html")))

(def shut-threads #(shutdown-agents))
