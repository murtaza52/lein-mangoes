(ns leiningen.mangoes
  (:require [leiningen.hiccup]
            [leiningen.globals :refer [run-action]]))

(defn mangoes
  [{:keys [mangoes]} & args]
  (doseq [v mangoes]
    (run-action v))
  (while true))

(comment
  (mangoes {:mangoes [[:hiccup->html "hiccup-template" "templates"] [:html->hiccup "tmp/abc" "hiccup-template/abc"]] }))
