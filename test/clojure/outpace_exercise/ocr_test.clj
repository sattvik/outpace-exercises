(ns outpace-exercise.ocr-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.test.check :as tc]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [outpace-exercise.ocr :refer :all]))

(def zero (str " _ "
               "| |"
               "|_|"))
(def one (str "   "
              "  |"
              "  |"))
(def two (str " _ "
              " _|"
              "|_ "))
(def three (str " _ "
                " _|"
                " _|"))
(def four (str "   "
               "|_|"
               "  |"))
(def five (str " _ "
               "|_ "
               " _|"))
(def six (str " _ "
              "|_ "
              "|_|"))
(def seven (str " _ "
                "  |"
                "  |"))
(def eight (str " _ "
                "|_|"
                "|_|"))
(def nine (str " _ "
               "|_|"
               " _|"))

(deftest test-pixels->digit
  (testing "read simple digits"
    (is (= 0 (pixels->digit zero)))
    (is (= 1 (pixels->digit one)))
    (is (= 2 (pixels->digit two)))
    (is (= 3 (pixels->digit three)))
    (is (= 4 (pixels->digit four)))
    (is (= 5 (pixels->digit five)))
    (is (= 6 (pixels->digit six)))
    (is (= 7 (pixels->digit seven)))
    (is (= 8 (pixels->digit eight)))
    (is (= 9 (pixels->digit nine)))))

(deftest test-line->digits
  (testing "story lines"
    (is (= [0 0 0 0 0 0 0 0 0]
           (line->digits [" _  _  _  _  _  _  _  _  _ "
                          "| || || || || || || || || |"
                          "|_||_||_||_||_||_||_||_||_|"
                          ""])))
    (is (= [1 1 1 1 1 1 1 1 1]
           (line->digits ["                           "
                          "  |  |  |  |  |  |  |  |  |"
                          "  |  |  |  |  |  |  |  |  |"
                          ""])))
    (is (= [2 2 2 2 2 2 2 2 2]
           (line->digits [" _  _  _  _  _  _  _  _  _ "
                          " _| _| _| _| _| _| _| _| _|"
                          "|_ |_ |_ |_ |_ |_ |_ |_ |_ "
                          ""])))
    (is (= [3 3 3 3 3 3 3 3 3]
           (line->digits [" _  _  _  _  _  _  _  _  _ "
                          " _| _| _| _| _| _| _| _| _|"
                          " _| _| _| _| _| _| _| _| _|"
                          ""])))
    (is (= [4 4 4 4 4 4 4 4 4]
           (line->digits ["                           "
                          "|_||_||_||_||_||_||_||_||_|"
                          "  |  |  |  |  |  |  |  |  |"
                          ""])))
    (is (= [5 5 5 5 5 5 5 5 5]
           (line->digits [" _  _  _  _  _  _  _  _  _ "
                          "|_ |_ |_ |_ |_ |_ |_ |_ |_ "
                          " _| _| _| _| _| _| _| _| _|"
                          ""])))
    (is (= [6 6 6 6 6 6 6 6 6]
           (line->digits [" _  _  _  _  _  _  _  _  _ "
                          "|_ |_ |_ |_ |_ |_ |_ |_ |_ "
                          "|_||_||_||_||_||_||_||_||_|"
                          ""])))
    (is (= [7 7 7 7 7 7 7 7 7]
           (line->digits [" _  _  _  _  _  _  _  _  _ "
                          "  |  |  |  |  |  |  |  |  |"
                          "  |  |  |  |  |  |  |  |  |"
                          ""])))
    (is (= [8 8 8 8 8 8 8 8 8]
           (line->digits [" _  _  _  _  _  _  _  _  _ "
                          "|_||_||_||_||_||_||_||_||_|"
                          "|_||_||_||_||_||_||_||_||_|"
                          ""])))
    (is (= [9 9 9 9 9 9 9 9 9]
           (line->digits [" _  _  _  _  _  _  _  _  _ "
                          "|_||_||_||_||_||_||_||_||_|"
                          " _| _| _| _| _| _| _| _| _|"
                          ""])))
    (is (= [1 2 3 4 5 6 7 8 9]
           (line->digits ["    _  _     _  _  _  _  _ "
                          "  | _| _||_||_ |_   ||_||_|"
                          "  ||_  _|  | _||_|  ||_| _|"
                          ""])))))

(def line0 [" _ " "   " " _ " " _ " "   " " _ " " _ " " _ " " _ " " _ "])
(def line1 ["| |" "  |" " _|" " _|" "|_|" "|_ " "|_ " "  |" "|_|" "|_|"])
(def line2 ["|_|" "  |" "|_ " " _|" "  |" " _|" "|_|" "  |" "|_|" " _|"])

(defn digits->line
  "Converts a sequence of digits into a pixelated line."
  [digits]
  [(apply str (map line0 digits))
   (apply str (map line1 digits))
   (apply str (map line2 digits))
   ""])

(def digits-generator
  "Generates vectors of digits."
  (gen/list (gen/elements (range 10))))

(def digits->line->digits
  "A property for round-tripping a sequence of digits to a line and back to a
  sequence of digits."
  (prop/for-all [digits digits-generator]
    (= digits (line->digits (digits->line digits)))))

(defspec generated-test-line->digits digits->line->digits)
