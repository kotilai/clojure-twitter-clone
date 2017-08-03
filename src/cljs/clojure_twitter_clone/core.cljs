(ns clojure-twitter-clone.core)

(defn mount-components []
  (let [content (js/document.getElementById "app")]
    (while (.hasChildNodes content)
      (.removeChild content (.-lastChild content)))
    (.appendChild content (js/document.createTextNode "Welcome to clojure-twitter-clone"))))

(defn init! []
  (mount-components))
