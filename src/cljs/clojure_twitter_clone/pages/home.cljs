(ns clojure-twitter-clone.pages.home
  (:require [reagent.core :as r]
            [reagent.format :as format]
            [ajax.core :refer [GET POST]]
            [clojure-twitter-clone.components.tweet :as tweet]))

(defn fetch-recent-tweets [result]
  (GET "/api/recent"
    {:headers {"Accept" "application/transit+json"}
     :handler #(reset! result %)}))

(defn home-page []
  (let [tweets (r/atom nil)]
    (fetch-recent-tweets tweets)
    (fn []
        [:div.container
        (for [t @tweets]
          ^{:key (:id t)}
          [tweet/tweet t])])))
