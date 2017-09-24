(ns clojure-twitter-clone.pages.user
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [ajax.core :refer [GET POST]]
            [clojure-twitter-clone.components.tweet :as tweet]))

(defn fetch-user-tweets [username result]
 (GET (str "/api/" username)
   {:headers {"Accept" "application/transit+json"}
    :handler #(reset! result %)}))

(defn user-page []
  (let [username "user";(js/window -location -pathname)
        routing-data (session/get :route)
        tweets (r/atom nil)]
    (.log js/console username)
    (.log js/console routing-data)
    (fetch-user-tweets username tweets)
    (fn []
        [:div.container
        (for [t @tweets]
          ^{:key (:id t)}
          [tweet t])])))
