(ns clojure-twitter-clone.utils.date
  (:require [reagent.format :as format]))

(defn format-date [date]
  (format/date-format date "dd.MM.yyyy HH:mm:ss"))

(defn date-to-millis [date]
  (.getTime date))

(defn round-div [divident divisor]
  (Math/round (/ divident divisor)))

(defn seconds-to [seconds type]
  (let [divisors {:seconds 1
                  :minutes 60
                  :hours 3600
                  :days 86400
                  :months 2629800
                  :years 31557600}
        key (keyword type)
        divisor (get divisors key 1)]
    (round-div seconds divisor)))

(defn date-diff [from to]
  (let [from-millis (date-to-millis from)
        to-millis (date-to-millis to)
        diff (- to-millis from-millis)]
    (/ diff 1000)))

(defn formatter-plain [text]
  (str text " ago"))

(defn formatter-value [text value value-type]
  (formatter-plain (str (seconds-to value value-type) text)))

(defn find-or-else [coll f else]
  (if-let [result (first (filter f coll))]
    result
    else))

(defn get-ago [diff]
  (let [agos (list {:limit 30 :formatter #(formatter-plain "a few seconds")}
                   {:limit 45 :formatter #(formatter-value " seconds" diff :seconds)}
                   {:limit 90 :formatter #(formatter-plain "a minute")}
                   {:limit 2700 :formatter #(formatter-value " minutes" diff :minutes)}
                   {:limit 5400 :formatter #(formatter-plain "an hour")}
                   {:limit 79200 :formatter #(formatter-value " hours" diff :hours)}
                   {:limit 129600 :formatter #(formatter-plain "a day")}
                   {:limit 2160000 :formatter #(formatter-value " days" diff :days)}
                   {:limit 3888000 :formatter #(formatter-plain "a month")}
                   {:limit 27561600 :formatter #(formatter-value " months" diff :months)}
                   {:limit 47347200 :formatter #(formatter-plain "a year")})
        predicate #(< diff (:limit %))
        else {:formatter #(formatter-value " years" diff :years)}]
    (find-or-else agos predicate else)))

(defn time-diff [from-date to-date]
  (let [diff (date-diff from-date to-date)
        ago (get-ago diff)
        formatter (:formatter ago)]
    (formatter)))

;(.log js/console (:text ago))

; (defn time-diff3 [from-date to-date]
;  (let [seconds (date-diff from-date to-date)]
;    (cond
;      (< seconds 30) (printer-ago "a few seconds ago")
;      (< seconds 45) (printer-ago " seconds ago" seconds :seconds)
;      (< seconds 90) (printer-ago "a minute ago")
;      (< seconds 2700) (printer-ago " minutes ago" seconds :minutes)
;      (< seconds 5400) (printer-ago "an hour ago")
;      (< seconds 79200) (printer-ago " hours ago" seconds :hours)
;      (< seconds 129600) (printer-ago "a day ago")
;      (< seconds 2160000) (printer-ago " days ago" seconds :days)
;      (< seconds 3888000) (printer-ago "a month ago")
;      (< seconds 27561600) (printer-ago " months ago" seconds :months)
;      (< seconds 47347200) (printer-ago "a year ago")
;      :else (printer-ago " years ago" seconds :years))))

(defn time-diff_ [from-date to-date]
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
