(ns clojure-twitter-clone.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [clojure-twitter-clone.ajax :refer [load-interceptors!]]
            [clojure-twitter-clone.pages.admin :as admin]
            [clojure-twitter-clone.pages.admin-user :as admin-user]
            [clojure-twitter-clone.pages.base :as base]
            [clojure-twitter-clone.pages.home :as home]
            [clojure-twitter-clone.pages.login :as login]
            [clojure-twitter-clone.pages.user :as user]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(def pages
  {:home #'home/home-page
   :login #'login/login-page
   :admin #'admin/admin-page
   :admin-new-user #'admin-user/new-page
   :admin-edit-user #'admin-user/edit-page
   :user #'user/user-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/login" []
  (session/put! :page :login))

(secretary/defroute "/admin" []
  (session/put! :page :admin))

(secretary/defroute "/admin/new" []
  (session/put! :page :admin-new-user))

(secretary/defroute "/admin/:username" []
  (session/put! :page :admin-edit-user))

(secretary/defroute "/:username" [username]
  (session/put! :page :user))

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
  (r/render [#'base/navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
