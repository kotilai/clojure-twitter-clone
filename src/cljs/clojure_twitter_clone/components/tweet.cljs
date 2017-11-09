(ns clojure-twitter-clone.components.tweet
  (:require [reagent.format :as format]))

(defn format-date [date]
  (format/date-format date "dd.MM.yyyy HH:mm:ss"))

(defn date-padge [date]
  (let [formatted (format-date date)]
    [:span.date-padge formatted]))

(defn username-padge [username]
  (let [uri (str "#/" username)]
    [:a.username-padge {:href uri}
      [:strong username]]))

(defn footer-link [class uri]
  [:div.footer-link
    [:a {:class class
         :href uri}]])

(defn tweet [content]
  (let [username (:username content)
        date (:posted_date content)]
  [:div.tweet.card
    [:div.card-body
      [:div.card-title
        [username-padge username]
        [date-padge date]]
      [:p.card-text (:text content)]
      [footer-link "fa fa-reply" "#"]
      [footer-link "fa fa-refresh" "#"]
      [footer-link "fa fa-heart-o" "#"]]]))
