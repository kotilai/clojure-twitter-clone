(ns clojure-twitter-clone.utils.url)

(defn change-to-url [url]
  (->
    (.-hash js/window.location)
    (set! url)))
  ;(set! (.-hash js/window.location) url))

(defn get-url-param []
  (->
    (.-hash js/window.location)
    (clojure.string/split #"/")
    last))
