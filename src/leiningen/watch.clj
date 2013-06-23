(ns leiningen.watch
  (:require [nio2.watch :refer [watch-seq]]
            [nio2.io :as nio2]
            [clojure.string :refer [split join]])
  (:import [java.nio.file Path]))

;; others - :create :delete

(def events-to-watch [:modify])

(def empty-string "")

(defn get-path-seq
  [p]
  (split p #"/"))

;; (defn get-path-seq
;;   "Splits the path on /, and accounts for the case where its an absolute path."

;; (defn get-path-seq
;;   "Splits the path on /, and accounts for the case where its an absolute path."
;;   [p]
;;   (let [s (split p #"/")]
;;     (if (=  (first s) emp;;   [p]
;;   (let [s (split p #"/")]
;;     (if (=  (first s) empty-string)
;;       (cons "/" (rest s))
;;       s)))

(defn contains-folder
  [folder path]
  (let [path-seq (get-path-seq path)
        path-seq-without-file (butlast path-seq)
        folder-seq (get-path-seq folder)
        possible-path-with-only-folder (take (count folder-seq) (reverse path-seq-without-file))]
    (every? (into #{} possible-path-with-only-folder) folder-seq)))


;; .toAbsolutePath should return the path to the file. However in some instances it returns only the project path + file name. It doesnt include the folder name that is being observed. Thus it either returns the path if it already includes the folder name, or adds it to the returned path.
(defn get-path
  "Returns the path of the file which was modified."
  [path-object relative-folder-path]
  (let [abs-path (.toString (.toAbsolutePath path-object))
        path-seq (get-path-seq abs-path)
        path-seq-with-folder (concat (butlast path-seq) [relative-folder-path (last path-seq)])]
    (if (contains-folder relative-folder-path abs-path)
      abs-path
      (join "/" path-seq-with-folder))))

(defn get-file-name
  "Returns the name of the file which was modified, without the extension."
  [p]
  (let [name (-> p .getFileName .toString)
        name-split (split name #"\.")]
    (if (> (count name-split) 1) ;; accounting for the case where there is no extension.
      (join "." (butlast name-split))
      (first name-split))))

(defn watch-folder
  "Watches a folder on path p for any changes, and applies fn f."
  [p f]
  (let [path-object (apply nio2/path (get-path-seq p))]
    (doseq [ev (apply watch-seq path-object events-to-watch)]
      (f (get-path (ev :path) p) (get-file-name (ev :path))))))

(comment
  (watch-folder "hiccup-template" println))
