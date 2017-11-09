(ns clojure-twitter-clone.pages.user
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [clojure-twitter-clone.components.tweet :as tweet]
            [clojure-twitter-clone.utils.url :as url]
            [clojure-twitter-clone.components.input :as input]))

(defn fetch-user-tweets [username result]
 (GET (str "/api/" username)
   {:headers {"Accept" "application/transit+json"}
    :handler #(reset! result %)}))

(defn create-tweet [username tweet]
 (POST (str "/api/" username)
   {:params tweet}))

(defn new-tweet [username]
  (let [text (r/atom nil)
        save #(create-tweet username {:text @text})]
    (fn []
      [:form
        [:div.form-group
          [:label "Post new tweet"]
          [:textarea.form-control {
            :rows 5
            :cols 80
            :on-change #(reset! text (-> % .-target .-value))}]]
          [:button.btn.btn-outline-success
            {:on-click save}
            "Post"]])))

(defn user-page []
  (let [username (url/get-url-param)
        tweets (r/atom nil)]
    (fetch-user-tweets username tweets)
    (fn []
        [:div.container
          [new-tweet username]
          (for [t @tweets]
            ^{:key (:id t)}
            [tweet/tweet t])])))
