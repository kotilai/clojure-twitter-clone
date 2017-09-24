(ns clojure-twitter-clone.pages.admin
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]))

(defn fetch-users [result]
 (GET "/admin/users"
   {:headers {"Accept" "application/transit+json"}
    :handler #(reset! result %)}))

(defn button [text on-click]
  [:input {
    :type "button"
    :value text
    :on-click on-click}])

(defn user [{:keys [first_name last_name username]}]
  [:tr
    [:td first_name]
    [:td last_name]
    [:td username]
    [:td
      [button "Edit" #(.log js/console "Clicked")]]])

(defn admin-page []
  (let [users (r/atom nil)]
    (fetch-users users)
    (fn []
        [:table.table
          [:thead
            [:tr
              [:td "First name"]
              [:td "Last name"]
              [:td "Username"]
              [:td]]]
          [:tbody
            (for [u @users]
              ^{:key (:id u)}
              [user u])]])))
