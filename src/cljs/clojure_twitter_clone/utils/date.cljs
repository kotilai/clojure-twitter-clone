(ns clojure-twitter-clone.utils.date
  (:require [reagent.format :as format]))

(defn format-date [date]
  (format/date-format date "dd.MM.yyyy HH:mm:ss"))

(defn date-to-millis [date]
  (.getTime date))

(defn round-div [divident divisor]
  (Math/round (/ divident divisor)))

(defn seconds-to [seconds to]
  (let [divisors {:seconds 1
                  :minutes 60
                  :hours 3600
                  :days 86400
                  :months 2629800
                  :years 31557600}
        divisor (get divisors to 1)]
    (round-div seconds divisor)))

(defn date-diff [from to]
  (let [from-millis (date-to-millis from)
        to-millis (date-to-millis to)]
    (->
      (- to-millis from-millis)
      (/ 1000))))

(defn formatter-plain [text]
  (str text " ago"))

(defn formatter-value [text value value-type]
  (->
    (seconds-to value value-type)
    (str text)
    (formatter-plain)))

(defn find-or-else [coll f else]
  (if-let [result (first (filter f coll))]
    result
    else))

(defn get-ago [seconds]
  (let [agos (list {:limit 30 :formatter #(formatter-plain "a few seconds")}
                   {:limit 45 :formatter #(formatter-value " seconds" seconds :seconds)}
                   {:limit 90 :formatter #(formatter-plain "a minute")}
                   {:limit 2700 :formatter #(formatter-value " minutes" seconds :minutes)}
                   {:limit 5400 :formatter #(formatter-plain "an hour")}
                   {:limit 79200 :formatter #(formatter-value " hours" seconds :hours)}
                   {:limit 129600 :formatter #(formatter-plain "a day")}
                   {:limit 2160000 :formatter #(formatter-value " days" seconds :days)}
                   {:limit 3888000 :formatter #(formatter-plain "a month")}
                   {:limit 27561600 :formatter #(formatter-value " months" seconds :months)}
                   {:limit 47347200 :formatter #(formatter-plain "a year")})
        else {:formatter #(formatter-value " years" seconds :years)}
        predicate #(< seconds (:limit %))]
    (find-or-else agos predicate else)))

(defn time-diff [from-date to-date]
  ((->
    (date-diff from-date to-date)
    (get-ago)
    (:formatter))))

(defn time-ago [date]
  (->>
    (js/Date.)
    (time-diff date)))
