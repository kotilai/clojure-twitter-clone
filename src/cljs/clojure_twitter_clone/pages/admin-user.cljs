(ns clojure-twitter-clone.pages.admin-user
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]
            [ajax.core :refer [GET POST PUT DELETE]]
            [clojure-twitter-clone.utils.url :as url]))

(defn return-from-edit []
 (url/change-to-url "/admin"))

(defn fetch-user [username user]
 (GET (str "/admin/user/" username)
   {:headers {"Accept" "application/transit+json"}
    :handler #(reset! user (select-keys % [:id
                                           :username
                                           :first_name
                                           :last_name
                                           :email
                                           :admin
                                           :is_active]))}))

(defn create-user [user]
 (POST "/admin/user"
   {:params user
    :handler return-from-edit}))

(defn update-user [user]
 (PUT "/admin/user"
   {:params user
    :handler return-from-edit}))

(defn delete-user [id]
 (DELETE (str "/admin/user/" id)
   {:handler return-from-edit}))

(defn build-form [save delete]
  [:form {:autoComplete "off"}
    [:div.form-group.row
      [:label.col-sm-2.col-form-label "Username"]
      [:div.col-sm-10 [:input {:field :text :id :username}]]]
    [:div.form-group.row
      [:label.col-sm-2.col-form-label "First name"]
      [:div.col-sm-10 [:input {:field :text :id :first_name}]]]
    [:div.form-group.row
      [:label.col-sm-2.col-form-label "Last name"]
      [:div.col-sm-10 [:input {:field :text :id :last_name}]]]
    [:div.form-group.row
      [:label.col-sm-2.col-form-label "Email"]
      [:div.col-sm-10 [:input {:field :email :id :email}]]]
    [:div.form-group.row
      [:label.col-sm-2 "Is admin"]
      [:div.col-sm-10 [:input {:field :checkbox :id :is_admin}]]]
    [:div.form-group.row
      [:label.col-sm-2 "Is active"]
      [:div.col-sm-10 [:input {:field :checkbox :id :is_active}]]]
    [:div.form-group.row
      [:label.col-sm-2.col-form-label "Password"]
      [:div.col-sm-10 [:input {:field :password :id :password}]]]
    [:div.form-group.row
      [:label.col-sm-2.col-form-label "Confirm password"]
      [:div.col-sm-10 [:input {:field :password :id :password_confirm}]]]
    [:div.form-group.row
      [:div.offset-sm-2.col-sm-10
        [:button.btn.btn-outline-success.mr-1
          {:on-click save}
          "Save"]
        [:button.btn.btn-outline-danger.mr-1
          {:on-click delete}
          "Remove"]
        [:button.btn.btn-outline-secondary
          {:on-click return-from-edit}
          "Cancel"]]]])

(defn user-form [user editing?]
  (let [[save delete title] (if editing?
          [#(update-user @user) #(delete-user (:id @user)) "Edit user"]
          [#(create-user @user) return-from-edit "Create user"])
        form-template (build-form save delete)]
    [:div.container
      [:h1 title]
      [bind-fields form-template user]]))

(defn edit-page []
  (let [username (url/get-url-param)
        user (r/atom nil)]
    (fetch-user username user)
    (fn []
      [user-form user true])))

(defn new-page []
  (let [user (r/atom {:username nil
                      :first_name nil
                      :last_name nil
                      :email nil
                      :admin false
                      :is_active true})]
    (fn []
      [user-form user false])))
