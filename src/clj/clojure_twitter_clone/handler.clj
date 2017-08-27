(ns clojure-twitter-clone.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [clojure-twitter-clone.layout :refer [error-page]]
            [clojure-twitter-clone.routes.home :refer [home-routes]]
            [clojure-twitter-clone.routes.login :refer [login-routes]]
            [clojure-twitter-clone.routes.user :refer [user-routes]]
            [compojure.route :as route]
            [clojure-twitter-clone.env :refer [defaults]]
            [mount.core :as mount]
            [clojure-twitter-clone.middleware :as middleware]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def users {
  "user" {
    :username "user"
    :password (creds/hash-bcrypt "pass")
    :roles #{:user}}})

(defn authenticate-routes [route-data]
  (friend/authenticate route-data
  			{:credential-fn (partial creds/bcrypt-credential-fn users)
         :workflows [(workflows/interactive-form)]}))

(def app-routes
  (authenticate-routes
    (routes
      (-> #'login-routes
          (wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats))
      (-> #'home-routes
          (wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats))
      (-> #'user-routes
          (wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats))
      (route/not-found
        (:body
          (error-page {:status 404
                       :title "page not found"}))))))

(defn app [] (middleware/wrap-base #'app-routes))
