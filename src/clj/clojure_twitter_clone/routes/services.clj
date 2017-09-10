(ns clojure-twitter-clone.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]
            [buddy.hashers :refer [check]]
            [clojure-twitter-clone.db.core :as db]
            [clojure-twitter-clone.middleware :as middleware]))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))

(s/defschema Tweet {:id String
                    :posted_date s/Any
                    :text String
                    :username String})

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}

  (POST "/login" []
    :body-params [username :- String, password :- String]
    :summary "Logs a user in and start session"
    (let [user (db/get-username username)]
      (if (check password (:pass user))
        (assoc-in (ok {}) [:session :identity] {:username (:username user)})
        (assoc-in (forbidden) [:session :identity] nil))))

  (POST "/logout" []
    :auth-rules authenticated?
    :summary "End users session"
    (assoc (ok) [:session :identity] nil))

  (GET "/authenticated" []
    :auth-rules authenticated?
    :current-user user
    (ok {:user user}))

  (context "/api" []
    :tags ["thingie"]
    ;:middleware [middleware/wrap-formats]

    (GET "/recent" []
      :return       [Tweet]
      :summary      "Returns 10 most recent tweets."
      ;(ok (convert-posted-date (db/get-recent-tweets))))
      (ok (db/get-recent-tweets)))

    (POST "/minus" []
      :return      Long
      :body-params [x :- Long, y :- Long]
      :summary     "x-y with body-parameters."
      (ok (- x y)))))
