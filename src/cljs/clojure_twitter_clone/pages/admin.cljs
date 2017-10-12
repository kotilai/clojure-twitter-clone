(ns clojure-twitter-clone.pages.admin
  (:require [reagent.core :as r]
            [ajax.core :refer [GET]]))

(defn fetch-users [users]
  (GET "/admin/users"
    {:headers {"Accept" "application/transit+json"}
     :handler #(reset! users %)}))

(defn user-row [{:keys [username
                        first_name
                        last_name
                        email
                        is_admin
                        is_active]}]
  [:tr
    [:td username]
    [:td first_name]
    [:td last_name]
    [:td email]
    [:td (if is_admin [:i.fa.fa-check])]
    [:td (if is_active [:i.fa.fa-check])]
    [:td [:a {:href (str "#/admin/" username)} [:i.fa.fa-pencil]]]])

(defn user-table [users]
  [:table.table
    [:thead
      [:tr
        [:td "Username"]
        [:td "First name"]
        [:td "Last name"]
        [:td "Email"]
        [:td "Is admin"]
        [:td "Is active"]
        [:td]]]
    [:tbody
      (for [u @users]
        ^{:key (:id u)}
        [user-row u])]])

(defn admin-page []
  (let [users (r/atom nil)]
    (fetch-users users)
    (fn []
      [:div.container
        [:a.btn.btn-success {:href "#/admin/new"} [:i.fa.fa-plus]]
        [user-table users]])))
