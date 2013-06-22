(ns leiningen.transformer
  (:require [leiningen.watch :refer [watch-folder]]
            [leiningen.file :refer [write-file read-file]]
            [leiningen.globals :refer [send-output]]))

(defn file-transformer
  "Reads a file, applies a fn f and writes it in another dir."
  [f write-dir ext]
  (fn [file-path file-name]
    (->> (read-file file-path) f (write-file [write-dir (str file-name "." ext)]))
    (send-output (str "Processed File : " file-path))))

(defn watch-and-transform
  [f ext]
  (fn [read-dir write-dir]
    (let [transformer-fn (file-transformer f write-dir ext)]
      (watch-folder read-dir transformer-fn))))
