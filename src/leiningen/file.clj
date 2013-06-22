(ns leiningen.file
  (:require [clojure.java.io :as io]))

(defn read-file
  [path]
  (slurp (io/file path)))

(defn write-file
  [path file]
  (spit (apply io/file path) file))
