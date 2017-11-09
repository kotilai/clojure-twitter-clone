(ns clojure-twitter-clone.components.input)

(defn- input [name type value]
  "Simple input component"
  [:input {
    :name name
    :type type
    :value @value
    :on-change #(reset! value (-> % .-target .-value))}])

(defn text [name value]
  [input name "text" value])

(defn pass [name value]
  [input name "password" value])
