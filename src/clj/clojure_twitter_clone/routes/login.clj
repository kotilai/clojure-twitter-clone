(ns clojure-twitter-clone.routes.login
  (:require [clojure-twitter-clone.layout :as layout]
            [compojure.core :refer [defroutes ANY]]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [ring.util.response :refer [redirect]]
            [cemerick.friend :as friend]))

(defn login-page []
  (layout/render "login.html"))

(defroutes login-routes
  (GET "/login" [] (login-page))
  (friend/logout (ANY "/logout" request (redirect "/"))))
