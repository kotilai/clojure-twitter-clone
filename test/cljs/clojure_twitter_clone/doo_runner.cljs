(ns clojure-twitter-clone.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [clojure-twitter-clone.core-test]))

(doo-tests 'clojure-twitter-clone.core-test)

