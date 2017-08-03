(ns clojure-twitter-clone.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[clojure-twitter-clone started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[clojure-twitter-clone has shut down successfully]=-"))
   :middleware identity})
