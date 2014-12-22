(ns outpace-exercise.validator-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [outpace-exercise.validator :refer :all]))

(deftest story-checksums
  (testing "story good account numbers"
    (is (valid-checksum? [7 1 1 1 1 1 1 1 1 ]))
    (is (valid-checksum? [1 2 3 4 5 6 7 8 9 ]))
    (is (valid-checksum? [4 9 0 8 6 7 7 1 5 ])))
  (testing "story bad account numbers"
    (is (not (valid-checksum? [8 8 8 8 8 8 8 8 8 ])))
    (is (not (valid-checksum? [4 9 0 0 6 7 7 1 5 ])))
    (is (not (valid-checksum? [0 1 2 3 4 5 6 7 8 ])))))

(def base-digits-generator
  "Generates vectors of eight digits."
  (gen/vector (gen/elements (range 10)) 8))

(defn partial-checksum
  "Generates a partial checksum based on a vector of eight digits."
  [digits]
  (rem (->> digits
            (map * (range 9 -1 -1))
            (reduce +))
       11))

(defn make-good-digits
  "Given a vector of eight digits, generate a vector of nine digits with a
  valid checksum."
  [digits]
  (let [new-digit (- 11 (partial-checksum digits))]
    (conj digits new-digit)))

(def good-digits-generator
  "Generates a vector of nine digits that should be valid"
  (gen/fmap make-good-digits base-digits-generator))

(def good-digits-validate-property
  "A property that ensures that valid checksums validate."
  (prop/for-all [d good-digits-generator]
    (valid-checksum? d)))

(defspec generated-good-validate-checksum good-digits-validate-property)

(defn make-bad-digits
  "Given a vector of eight digits, generate a vector of nine digits with an
  invalid checksum."
  [digits]
  (let [new-digit (- 8 (partial-checksum digits))]
    (conj digits new-digit)))

(def bad-digits-generator
  "Generates a vector of nine digits that should be invalid"
  (gen/fmap make-bad-digits base-digits-generator))

(def bad-digits-validate-property
  "A property that ensures that invalid checksums won't validate."
  (prop/for-all [d bad-digits-generator]
    (not (valid-checksum? d))))

(defspec generated-bad-validate-checksum bad-digits-validate-property)
