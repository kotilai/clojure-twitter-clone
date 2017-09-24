(ns clojure-twitter-clone.pages.base
  (:require [reagent.core :as r]
            [reagent.session :as session]))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
    {:class (when (= page (session/get :page)) "active")}
    [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-dark.bg-primary
        [:button.navbar-toggler.hidden-sm-up
          {:on-click #(swap! collapsed? not)} "☰"]
        [:div.collapse.navbar-toggleable-xs
          (when-not @collapsed? {:class "in"})
          [:a.navbar-brand {:href "#/"} "clojure-twitter-clone"]
          [:ul.nav.navbar-nav
            [nav-link "#/" "Home" :home collapsed?]]
        [:ul.nav.navbar-nav.float-sm-right
          [nav-link "#/login" "Login" :login collapsed?]
          [nav-link "#/logout" "Logout" :logout collapsed?]]]])))
