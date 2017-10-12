(ns clojure-twitter-clone.pages.admin-user
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]
            [ajax.core :refer [GET POST PUT DELETE]]))

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
   {:params user}))

(defn update-user [user]
 (PUT "/admin/user"
   {:params user}))

(defn delete-user [id]
 (DELETE "/admin/user"
   {:params id}))

(defn cancel-user-edit []
 (set! (.-hash js/window.location) "/admin"))

(defn get-url-param []
  (->
    (.-hash js/window.location)
    (clojure.string/split #"/")
    last))

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
          {:on-click cancel-user-edit}
          "Cancel"]]]])

(defn user-form [user]
  (let [creating? (contains? @user :id)
        [save delete title] (if creating?
                              [#(create-user @user) cancel-user-edit "Edit user"]
                              [#(update-user @user) #(delete-user (:id @user)) "Create user"])
        form-template (build-form save delete)]
    [:div.container
      [:h1 title]
      [bind-fields form-template user]]))

(defn edit-page []
  (let [username (get-url-param)
        user (r/atom nil)]
    (fetch-user username user)
    (fn []
      [user-form user])))

(defn new-page []
  (let [user (r/atom {:username nil
                      :first_name nil
                      :last_name nil
                      :email nil
                      :admin false
                      :is_active true})]
    (fn []
      [user-form user])))
