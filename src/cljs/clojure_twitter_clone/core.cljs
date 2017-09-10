(ns clojure-twitter-clone.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
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
          [nav-link "#/login" "Login" :login collapsed?]
          [nav-link "#/logout" "Logout" :logout collapsed?]]]])))

(defn fetch-recent-tweets [result]
  (GET "/api/recent"
    {:headers {"Accept" "application/transit+json"}
     :handler #(reset! result %)}))

(defn tweet [content]
  [:div.card
    [:div.card-body
      [:h4.card-title (str (:username content) " " (:posted_date content))]
      [:p.card-text (:text content)]]])

(defn home-page []
  (let [tweets (r/atom nil)]
    (fetch-recent-tweets tweets)
    (fn []
        [:div.container
        (for [t @tweets]
          ^{:key (:id t)}
          [tweet t])])))

(defn input [name type value]
  "Simple input component"
  [:input {
    :name name
    :type type
    :value @value
    :on-change #(reset! value (-> % .-target .-value))}])

(defn login-page []
  (let [username (r/atom nil)
        password (r/atom nil)]
    [:div.container
      [input "username" "text" username]
      [input "password" "password" password]]))

(def pages
  {:home #'home-page
   :login #'login-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/login" []
  (session/put! :page :login))

(secretary/defroute "/logout" []
  (session/put! :page :logout))

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
