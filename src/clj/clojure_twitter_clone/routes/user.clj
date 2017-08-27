(ns clojure-twitter-clone.routes.user
  (:require [clojure-twitter-clone.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [compojure.core :refer [defroutes POST]]
            [compojure.core :refer [defroutes DELETE]]
            [ring.util.http-response :as response]
            [clojure-twitter-clone.db.core :as db]
            [ring.util.response :refer [redirect]]
            [struct.core :as st]
            [cemerick.friend :as friend]))

(def tweet-schema
  [[:text st/string
    {:message "Tweet must be 1 - 140 characters"
      :validate #(<= 1 (count %) 140)}]])

(defn validate-tweet [params]
  (first (st/validate params tweet-schema)))

(defn save-tweet! [{:keys [params] :as request}]
  (if-let [errors (validate-tweet params)]
    (-> (response/found (str "/" (:username params)))
      (assoc :flash (assoc params :errors errors)))
    (do
      (db/create-tweet!
        (-> params
          (assoc :posted_date (java.util.Date.))
          (assoc :username ((friend/current-authentication request) :username))))
      (response/found (str "/" (:username params))))))

(def delete-schema
  [[:id st/string]])

(defn validate-delete [params]
  (first (st/validate params delete-schema)))

(defn delete-tweet! [{:keys [params]}]
  (if-let [errors (validate-delete params)]
    (-> (response/found (str "/" (:username params)))
      (assoc :flash (assoc params :errors errors)))
    (do
      (db/delete-tweet! params)
      (response/ok))))

(defn user-page [{:keys [flash params] :as request}]
  (layout/render
    "user.html"
    (merge {:tweets (db/get-user-tweets params)}
      (select-keys flash [:name :message :errors])
      {:auth (friend/current-authentication request)})))

(defroutes user-routes
  (GET "/:username" request (user-page request))
  (POST "/:username" request (friend/authorize #{:user} (save-tweet! request)))
  (DELETE "/:username/:id" request (friend/authorize #{:user} (delete-tweet! request))))
