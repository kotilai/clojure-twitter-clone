(ns clojure-twitter-clone.pages.login
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [clojure-twitter-clone.components.input :as input]
            [clojure-twitter-clone.utils.auth :as auth]))

(defn login-page []
 (let [username (r/atom nil)
       password (r/atom nil)]
   [:div.container
     [input/text "username" username]
     [input/pass "password" password]
     [:input {:type "button" :value "Login"
      :on-click #(auth/login {:username @username :password @password})}]]))
