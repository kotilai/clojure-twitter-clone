(ns clojure-twitter-clone.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [clojure-twitter-clone.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

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
          [nav-link "#/login" "Home" :home collapsed?]
          [nav-link "#/logout" "About" :about collapsed?]]]])))

(defn home-page []
  [:div.container
   (when-let [docs (session/get :docs)]
     [:div.row>div.col-sm-12
       [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(def pages
  {:home #'home-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
