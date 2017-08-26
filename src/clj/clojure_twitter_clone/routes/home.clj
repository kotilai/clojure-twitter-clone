(ns clojure-twitter-clone.routes.home
  (:require [clojure-twitter-clone.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [clojure-twitter-clone.db.core :as db]
            [cemerick.friend :as friend]))

(defn home-page [{:keys [flash] :as request}]
  (layout/render
    "home.html"
    (merge {:tweets (db/get-recent-tweets)}
      (select-keys flash [:name :message :errors])
      {:auth (friend/current-authentication request)})))

(defroutes home-routes
  (GET "/" request (home-page request)))
