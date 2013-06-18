(ns leiningen.file)

(defn read-file
  [path]
  (slurp path))

(defn write-file
  [path]
  (spit path))
