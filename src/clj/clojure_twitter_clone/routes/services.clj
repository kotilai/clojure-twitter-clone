(ns clojure-twitter-clone.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]
            [buddy.hashers :refer [check encrypt]]
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

(s/defschema User {(s/optional-key :id) Long
                   :username String
                   :first_name String
                   :last_name String
                   :email String
                   :admin Boolean
                   :is_active Boolean
                   (s/optional-key :last_login) (s/maybe java.sql.Timestamp)
                   (s/optional-key :password) (s/maybe String)
                   (s/optional-key :password_confirm) (s/maybe String)})

(s/defschema Tweet {:id Long
                    :posted_date java.sql.Timestamp
                    :text String
                    :username String})

(defn sanitize-user [user]
  (dissoc user :password))

(defn get-all-users []
  (map sanitize-user (db/get-all-users)))

(defn get-user [user]
  (sanitize-user (db/get-username user)))

(defn do-user [user f]
  (f user)
  (if (contains? user :password)
    (db/update-user-password! (update user :password encrypt)))
  (get-user user))

(defn create-user [user]
  (do-user user db/create-user!))

(defn update-user [user]
  (do-user user db/update-user!))

(defn delete-user [id]
  (db/delete-user! {:id id}))

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}

  (POST "/login" []
    :body-params [username :- String, password :- String]
    :summary "Logs a user in and start session"
    (let [user (db/get-user-password {:username username})]
      (if (and user (check password (:password user)))
        (assoc-in (ok {}) [:session :identity] {:username (:username user)})
        (assoc (forbidden) :session nil))))

  (POST "/logout" []
    :auth-rules authenticated?
    :summary "End users session"
    (assoc (ok) :session nil))

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
      (ok (db/get-recent-tweets)))

    (GET "/:username" [username]
      :return       [Tweet]
      :summary      "Returns users tweets."
      (ok (db/get-user-tweets {:username username}))))

    (context "/admin" []
      (GET "/users" []
        :return       [User]
        :summary      "Returns all users"
        (ok (get-all-users)))

      (GET "/user/:username" [username]
        :return       User
        :summary      "Returns a user"
        (ok (get-user {:username username})))

      (POST "/user" []
        :body [user User]
        :return       User
        :summary      "Create a user"
        (ok (create-user user)))

      (PUT "/user" []
        :body [user User]
        :return       User
        :summary      "Update a user"
        (ok (update-user user)))

      (DELETE "/user" []
        :body [id Long]
        :summary      "Delete a user"
        (ok (delete-user id)))
      ))
