(ns clojure-twitter-clone.utils.date
  (:require [reagent.format :as format]))

(defn format-date [date]
  (format/date-format date "dd.MM.yyyy HH:mm:ss"))

(defn date-to-millis [date]
  (.getTime date))

(defn round-div [divident divisor]
  (Math/round (/ divident divisor)))

(defn time-diff [from-date to-date]
  (let [from (date-to-millis from-date)
        to (date-to-millis to-date)
        millis (- to from)
        seconds (/ millis 1000)]
    (cond
      (< seconds 30) "a few seconds ago"
      (< seconds 45) (str (round-div seconds 1) " seconds ago")
      (< seconds 90) "a minute ago"
      (< seconds 2700) (str (round-div seconds 60) " minutes ago")
      (< seconds 5400) "an hour ago"
      (< seconds 79200) (str (round-div seconds 3600) " hours ago")
      (< seconds 129600) "a day ago"
      (< seconds 2160000) (str (round-div seconds 86400) " days ago")
      (< seconds 3888000) "a month ago"
      (< seconds 27561600) (str (round-div seconds 2629800) " months ago")
      (< seconds 47347200) "a year ago"
      :else (str (round-div seconds 31557600) " years ago"))))

(defn time-ago [date]
  (->>
    (js/Date.)
    (time-diff date)))
