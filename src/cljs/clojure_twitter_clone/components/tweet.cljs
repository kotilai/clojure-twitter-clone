(ns clojure-twitter-clone.components.tweet
  (:require [clojure-twitter-clone.utils.date :as date-util]))

(defn date-padge [date]
  (let [formatted (date-util/time-ago date)]
    [:span.date-padge formatted]))

(defn username-padge [username]
  (let [uri (str "#/" username)]
    [:a.username-padge {:href uri}
      [:strong username]]))

(defn footer-link [class uri]
  [:div.footer-link
    [:a {:class class
         :href uri}]
    [:span "1"]])

(defn tweet [content]
  (let [username (:username content)
        date (:posted_date content)]
  [:div.tweet
    [:div.body-left
      [:div.avatar [:i {:class "fa fa-user-circle"}]]]
    [:div.body-right
      [:div.header
        [username-padge username]
        [date-padge date]]
      [:div.content
        [:p (:text content)]]
      [:div.footer
        [footer-link "fa fa-reply" "#"]
        [footer-link "fa fa-refresh" "#"]
        [footer-link "fa fa-heart-o" "#"]]]]))

(defn tweet-list [tweets]
  [:div.tweet-list
    (for [t (reverse (sort-by :posted_date tweets))]
      ^{:key (:id t)}
      [tweet t])])
