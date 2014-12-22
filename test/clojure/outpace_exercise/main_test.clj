(ns outpace-exercise.main-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer [deftest is]]
            [outpace-exercise.main :refer :all])
  (:import [java.io File]))

(deftest story1
  (let [input (slurp (io/resource "story-input.txt"))
        expected (slurp (io/resource "story-output.txt"))
        in (File/createTempFile "story-" ".in")
        out (File/createTempFile "story-" ".out")]
    (spit in input)
    (-main (.getPath in) (.getPath out))
    (let [result (slurp out)]
      (is (= expected result)))))
