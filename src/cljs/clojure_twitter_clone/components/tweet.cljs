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
    [:div.card-block
      [:div.card-title
        [username-padge username]
        [date-padge date]]
      [:p.card-text (:text content)]
      [footer-link "fa fa-reply" "#"]
      [footer-link "fa fa-refresh" "#"]
      [footer-link "fa fa-heart-o" "#"]]]))

(defn tweet-list [tweets]
  [:div.tweet-list
    (for [t (reverse (sort-by :posted_date tweets))]
      ^{:key (:id t)}
      [tweet t])])

      ; <div class="t">
      ;   <div class="t-body">
      ;     <div class="left">
      ;       <div class="t-avatar">fff</div>
      ;     </div>
      ;     <div class="right">
      ;       <div class="t-heading">John</div>
      ;       <div class="t-content">abc</div>
      ;       <div class="t-footer">1 2 3</div>
      ;     </div>
      ;   </div>
      ; </div>
      ;
      ; .t-body {
      ;   display: flex;
      ; }
      ;
      ; .t-body .left {
      ;   flex: 0 0 58px;
      ; }
      ;
      ; .t-body .right {
      ;   flex: 1 100%;
      ; }
      ;
      ;
      ; .t-avatar {
      ;
      ; }
