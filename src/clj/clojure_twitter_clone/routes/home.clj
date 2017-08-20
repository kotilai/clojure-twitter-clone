(ns clojure-twitter-clone.routes.home
  (:require [clojure-twitter-clone.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [compojure.core :refer [defroutes POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure-twitter-clone.db.core :as db]
            [ring.util.response :refer [redirect]]
            [struct.core :as st]
            [cemerick.friend :as friend]))

; https://github.com/clj-time/clj-time#clj-timeformat
; https://funcool.github.io/struct/latest/#define-your-own-validator

(def tweet-schema
  [[:text st/string
     {:message "Tweet must be 1 - 140 characters"
      :validate #(<= 1 (count %) 140)}]])

(defn validate-tweet [params]
  (first (st/validate params tweet-schema)))

(defn save-tweet! [{:keys [params]}]
  (if-let [errors (validate-tweet params)]
    (-> (response/found "/")
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/create-tweet!
        (assoc params :posted_date (java.util.Date.)))
      (response/found "/"))))

(def delete-schema
  [[:id st/string]])

(defn validate-delete [params]
  (first (st/validate params delete-schema)))

(defn delete-tweet! [{:keys [params]}]
  (if-let [errors (validate-delete params)]
    (-> (response/found "/")
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/delete-tweet! params)
      (response/found "/"))))

(defn home-page [{:keys [flash]}]
  (layout/render
   "home.html"
   (merge {:tweets (db/get-tweets)}
          (select-keys flash [:name :message :errors]))))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" request (home-page request))
  (POST "/" request (friend/authorize #{:user} (save-tweet! request)))
  (POST "/delete" request (friend/authorize #{:user} (delete-tweet! request)))
  (GET "/about" [] (about-page)))
