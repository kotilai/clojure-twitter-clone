(ns ^:figwheel-no-load clojure-twitter-clone.app
  (:require [clojure-twitter-clone.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
