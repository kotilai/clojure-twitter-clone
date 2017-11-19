(ns clojure-twitter-clone.utils.url)

(defn change-to-url [url]
  (->
    (.-hash js/window.location)
    (set! url)))

(defn get-url-param []
  (->
    (.-hash js/window.location)
    (clojure.string/split #"/")
    last))
