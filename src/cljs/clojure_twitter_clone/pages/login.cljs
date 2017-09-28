(ns clojure-twitter-clone.pages.login
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [clojure-twitter-clone.components.input :as input]))

(defn send-login [credentials]
  (POST "/login"
    {:params credentials
     }))

(defn login-page []
 (let [username (r/atom nil)
       password (r/atom nil)]
   [:div.container
     [input/input "username" "text" username]
     [input/input "password" "password" password]
     [:input {:type "button" :value "Login"
      :on-click #(send-login {:username @username :password @password})}]]))
