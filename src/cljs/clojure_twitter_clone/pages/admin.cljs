(ns clojure-twitter-clone.pages.admin
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]
            [ajax.core :refer [GET POST PUT DELETE]]))

(defonce users (r/atom (sorted-map)))

(defn add-user [id user]
  (swap! users assoc id user))

(defn delete-user [id]
  (swap! users dissoc id))

(defn fetch-users []
 (GET "/admin/users"
   {:headers {"Accept" "application/transit+json"}
    :handler #(doseq [u %] (add-user (:id u) u))}))

(defn create-user [user]
 (POST "/admin/user"
   {:params user}))

; (defn update-user [user]
;  (PUT "/admin/user"
;    {:params user}))
;
; (defn delete-user [id]
;  (DELETE "/admin/user"
;    {:params id}))

(defn strength-badge [strength]
  (let [[text class] (case strength
                        "STRONG" ["Strong" "tag-success"]
                        "AVERAGE" ["Average" "tag-warning"]
                        "WEAK" ["Weak" "tag-danger"])]
    [:span.tag {:class class} text]))

(defn build-form [toggle save delete]
  [:tr.user-row-edit
    [:td [:input {:field :text :id :username}]]
    [:td [:input {:field :text :id :first_name}]]
    [:td [:input {:field :text :id :last_name}]]
    [:td [:input {:field :email :id :email}]]
    [:td [:input {:field :checkbox :id :is_admin}]]
    [:td [:input {:field :checkbox :id :is_active}]]
    [:td
      [:input {:field :password :id :password1}]
      [:input {:field :password :id :password2}]]
    [:td
      [:i.fa.fa-save {:on-click save}]
      [:i.fa.fa-remove {:on-click delete}]
      [:i.fa.fa-undo {:on-click toggle}]]])

(defn user-row-edit [user toggle]
  (let [doc (r/atom user)
        save #(create-user doc)
        delete #(delete-user (:id user))
        form-template (build-form toggle save delete)]
    (fn []
      [bind-fields form-template doc])))

(defn user-row-view [{:keys [username
                             first_name
                             last_name
                             email
                             is_admin
                             is_active
                             password_strength]} toggle]
  [:tr
    [:td username]
    [:td first_name]
    [:td last_name]
    [:td email]
    [:td (if is_admin [:i.fa.fa-check])]
    [:td (if is_active [:i.fa.fa-check])]
    [:td (strength-badge password_strength)]
    [:td [:i.fa.fa-pencil {:on-click toggle}]]])

(defn user-row [user]
  (let [editing (r/atom (:editing user))
        toggle #(reset! editing (not @editing))]
    (fn []
      (if @editing
        [user-row-edit user toggle]
        [user-row-view user toggle]))))

(defn user-table []
  (fn []
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
        (for [u (vals @users)]
          ^{:key (:id u)}
          [user-row u])]]))

(defn admin-page []
  (fetch-users)
  (fn []
    [:div.container
      [:button.btn.btn-success {:on-click #(add-user 0 {:id 0 :editing true})}
        [:i.fa.fa-plus]]
      [user-table]]))
