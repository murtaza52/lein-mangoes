(ns leiningen.watch
  (:require [nio2.watch :refer [watch-seq]]
            [nio2.io :as nio2]
            [clojure.string :refer [split join]])
  (:import [java.nio.file Path]))

;; others - :create :delete

(def events-to-watch [:modify])

(def empty-string "")

(defn path-seq
  [p]
  (let [s (split p #"\\")]
    (if (=  (first s) empty-string)
      (cons "/" (rest s))
      s)))

(defn get-path
  [path-object relative-folder-path]
  (let [abs-path (.toString (.toAbsolutePath path-object))
        path-seq (split abs-path #"/")
        path-seq-with-folder (concat (butlast path-seq) [relative-folder-path (last path-seq)])]
    (if (some (into #{} path-seq) (split relative-folder-path #"/"))
      abs-path
      (join "/" path-seq-with-folder))))

(defn get-file-name
  [p]
  (let [name (-> p .getFileName .toString)
        name-split (split name #"\.")]
    (if (> (count name-split) 1) ;; accounting for the case where there is no extension.
      (join "." (butlast name-split))
      (first name-split))))

;; (defn file-path-with-name
;;   [ev p]
;;   (str (get-path (ev :path) p) (-> ev :path .getFileName .toString)))

(defn watch-folder
  [p f]
  (let [path-object (apply nio2/path (path-seq p))]
    (doseq [ev (apply watch-seq path-object events-to-watch)]
      (f (get-path (ev :path) p) (get-file-name (ev :path))))))
