(ns user
  (:require [mount.core :as mount]
            [clojure-twitter-clone.figwheel :refer [start-fw stop-fw cljs]]
            clojure-twitter-clone.core))

(defn start []
  (mount/start-without #'clojure-twitter-clone.core/repl-server))

(defn stop []
  (mount/stop-except #'clojure-twitter-clone.core/repl-server))

(defn restart []
  (stop)
  (start))


