(ns outpace-exercise.validator)

(defn valid-checksum?
  "Validates if a sequence of nine digits validate according to their
  checksum."
  [digits]
  (let [sum (->> (map * (range 9 -1 -1) digits)
                 (reduce +))]
    (zero? (rem sum 11))))
