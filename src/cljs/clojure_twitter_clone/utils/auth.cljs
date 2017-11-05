(ns clojure-twitter-clone.utils.auth
  (:require [reagent.core :as r]
            [ajax.core :refer [POST]]))

(def authenticated? (r/atom false))

(defn change-to-url [url]
  (set! (.-hash js/window.location) url))

(defn state-handler [new-state]
  (reset! authenticated? new-state)
  (change-to-url "/"))

(defn do-auth [url params state-after]
  (POST url
    {:params params
     :handler #(state-handler state-after)}))

(defn is-authenticated []
  @authenticated?)

(defn logout []
  (do-auth "/logout" nil false))

(defn login [credentials]
  (do-auth "/login" credentials true))
