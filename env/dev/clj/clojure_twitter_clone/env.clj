(ns clojure-twitter-clone.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [clojure-twitter-clone.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[clojure-twitter-clone started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[clojure-twitter-clone has shut down successfully]=-"))
   :middleware wrap-dev})
