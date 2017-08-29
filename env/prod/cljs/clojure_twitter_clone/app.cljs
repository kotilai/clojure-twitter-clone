(ns clojure-twitter-clone.app
  (:require [clojure-twitter-clone.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
