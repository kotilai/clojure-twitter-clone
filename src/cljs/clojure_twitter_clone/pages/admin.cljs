(ns clojure-twitter-clone.pages.admin
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [clojure-twitter-clone.components.input :as input]))

(defonce users (r/atom nil))

(defn fetch-users []
 (GET "/admin/users"
   {:headers {"Accept" "application/transit+json"}
    :handler #(reset! users %)}))

(defn add-user [users]
  (conj users {:first_name nil :last_name nil :username nil :is_active false}))

(defn strength-badge [strength]
  (let [[text class] (case strength
                        "STRONG" ["Strong" "tag-success"]
                        "AVERAGE" ["Average" "tag-warning"]
                        "WEAK" ["Weak" "tag-danger"])]
    [:span.tag {:class class} text]))

(defn to-atom-map [m]
  (into {} (map (fn [[k v]] [k (r/atom v)]) m)))

(defn user-row-edit [user]
  (let [form (to-atom-map user)]
    ))

(defn user-row-view [{:keys [username
                             first_name
                             last_name
                             email
                             is_admin
                             is_active
                             password_strength]}]
  [:td username]
  [:td first_name]
  [:td last_name]
  [:td email]
  [:td (if is_admin [:i.fa.fa-check])]
  [:td (if is_active [:i.fa.fa-check])]
  [:td (strength-badge password_strength)]
  [:td [:i.fa.fa-pencil {:on-click #(reset! editing true)}]])

(defn user-row [user]
  (let [editing? (r/atom false)]
    [:tr
      (if @editing?
        [user-row-edit user]
        [user-row-view user])]))

(defn user-table []
  [:table.table
    [:thead
      [:tr
        [:td "Username"]
        [:td "First name"]
        [:td "Last name"]
        [:td "Email"]
        [:td "Is admin"]
        [:td "Is active"]
        [:td "Password"]
        [:td]]]
    [:tbody
      (for [u @users]
        ^{:key (:id u)}
        [user-row u])]])

(defn admin-page []
  (fetch-users)
  (fn []
    [:div.container
      [:button.btn.btn-success {:on-click #(.log js/console "Clicked add")}
        [:i.fa.fa-plus]]
      [user-table]))
