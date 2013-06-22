(ns leiningen.jeff
  (:require [leiningen.hiccup]
            [leiningen.globals :refer [run-action]]))

(defn jeff
  [{:keys [jeff]} & args]
  (doseq [v jeff]
    (run-action v))
  (while true))

(comment
  (jeff {:jeff [[:hiccup->html "hiccup-template" "templates"] [:html->hiccup "tmp/abc" "hiccup-template/abc"]] }))
