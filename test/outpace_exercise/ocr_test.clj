(ns outpace-exercise.ocr-test
  (require [clojure.test :refer [deftest is testing]]
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
