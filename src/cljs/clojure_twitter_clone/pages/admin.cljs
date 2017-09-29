(ns clojure-twitter-clone.pages.admin
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]))

(defn fetch-users [result]
 (GET "/admin/users"
   {:headers {"Accept" "application/transit+json"}
    :handler #(reset! result %)}))

(defn add-user [users]
  (conj users {:first_name nil :last_name nil :username nil :is_active false}))

(defn user [{:keys [username
                    first_name
                    last_name
                    email
                    is_admin
                    is_active
                    password_strength]}]
  [:tr
    [:td username]
    [:td first_name]
    [:td last_name]
    [:td email]
    [:td password_strength]]) ; label
    [:td (if is_admin [:i.fa.fa-check])]
    [:td (if is_active [:i.fa.fa-check])]

(defn admin-page []
  (let [users (r/atom nil)]
    (fetch-users users)
    (fn []
      [:div.container
        [:button.btn.btn-success {:on-click #(.log js/console "Clicked add")}
          [:i.fa.fa-plus]]
        [:table.table
          [:thead
            [:tr
              [:td "Username"]
              [:td "First name"]
              [:td "Last name"]
              [:td "Email"]
              [:td "Is admin"]
              [:td "Is active"]
              [:td "Password"]]]
          [:tbody
            (for [u @users]
              ^{:key (:id u)}
              [user u])]]])))
