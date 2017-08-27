(ns clojure-twitter-clone.routes.admin
  (:require [clojure-twitter-clone.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [compojure.core :refer [defroutes POST]]
            [compojure.core :refer [defroutes DELETE]]
            [ring.util.http-response :as response]
            [clojure-twitter-clone.db.core :as db]
            [ring.util.response :refer [redirect]]
            [struct.core :as st]
            [cemerick.friend :as friend]))

(def user-schema
  [[:username st/string st/required]
   [:first_name st/string st/required]
   [:last_name st/string st/required]])

(defn validate-user [params]
  (first (st/validate params user-schema)))

(defn save-user! [{:keys [params] :as request}]
  (if-let [errors (validate-user params)]
    (-> (response/found "/admin")
      (assoc :flash (assoc params :errors errors)))
    (do
      (if (contains? params :id) (db/update-user! params) (db/create-user! params))
      (response/found "/admin"))))

(def delete-schema
  [[:id st/string]])

(defn validate-delete [params]
  (first (st/validate params delete-schema)))

(defn delete-user! [{:keys [params]}]
  (if-let [errors (validate-delete params)]
    (-> (response/found "/admin")
      (assoc :flash (assoc params :errors errors)))
    (do
      (db/delete-user! params)
      (response/ok))))

(defn admin-page [{:keys [flash params] :as request}]
  (layout/render
    "admin.html"
    (merge {:users (db/get-all-users)}
      (select-keys flash [:name :message :errors])
      {:auth (friend/current-authentication request)})))

(defroutes admin-routes
  (GET "/admin" request (friend/authorize #{:admin} (admin-page request)))
  (POST "/admin" request (friend/authorize #{:admin} (save-user! request)))
  (DELETE "/admin/:id" request (friend/authorize #{:admin} (delete-user! request))))
