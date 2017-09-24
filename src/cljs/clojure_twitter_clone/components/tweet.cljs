(ns clojure-twitter-clone.components.tweet
  (:require [reagent.core :as format]))

(defn format-date [date]
  (format/date-format date "dd.MM.yyyy HH.mm.ss"))

(defn tweet [content]
  (let [username (:username content)
        date (format-date (:posted_date content))]
  [:div.card
    [:div.card-body
      [:h5.card-title (str username " " date)]
      [:p.card-text (:text content)]]]))
