(ns leiningen.mangoes
  (:require [leiningen.hiccup]
            [leiningen.globals :refer [run-action]]))

(defn mangoes
  [{:keys [jeff]} & args]
  (doseq [v jeff]
    (run-action v))
  (while true))

(comment
  (jeff {:jeff [[:hiccup->html "hiccup-template" "templates"] [:html->hiccup "tmp/abc" "hiccup-template/abc"]] }))
