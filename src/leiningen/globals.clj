(ns leiningen.globals)

(def all-futures (atom []))

(defn dispose-futures!
  []
  (doseq [fu @all-futures]
    (future-cancel fu))
  (reset! all-futures []))

(def output (atom []))

(def send-output (partial reset! output))

(add-watch output :print (fn [_ _ _ n] (println n)))

;; (def last-modified-file (atom nil))

;; (defn was-this-written-last?
;;   [f]
;;   (= f @last-modified-file))

;; (defn set-last-modified-file!
;;   [f]
;;   (reset! last-modified-file f))

(def error (atom nil))

(add-watch error :set-printer (fn [_ _ _ n] (reset! output n)))

(add-watch error :terminate (fn [_ _ _ _] (dispose-futures!)))

(defn create-future
  [f & args]
  (let [fu (future (try
                     (apply f args)
                     (catch Exception e (reset! error (.toString e)))))]
    (swap! all-futures #(conj % fu))))

(defmulti run-action first)

(comment
  (future-cancel (first @all-futures))
  (map future-cancel @all-futures)
  (dispose-futures!))
