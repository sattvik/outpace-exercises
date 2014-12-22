(ns outpace-exercise.ocr-test
  (:require [clojure.set :as set]
            [clojure.test :refer [deftest is testing]]
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
  (testing "story 1 lines"
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
                          ""]))))
  (testing "story 3 lines"
   (is (= [0 0 0 0 0 0 0 5 1]
           (line->digits [" _  _  _  _  _  _  _  _    "
                          "| || || || || || || ||_   |"
                          "|_||_||_||_||_||_||_| _|  |"
                          ""])))
   (is (= [4 9 0 0 6 7 7 1 \?]
           (line->digits ["    _  _  _  _  _  _     _ "
                          "|_||_|| || ||_   |  |  | _ "
                          "  | _||_||_||_|  |  |  | _|"
                          ""])))
   (is (= [1 2 3 4 \? 6 7 8 \?]
          (line->digits ["    _  _     _  _  _  _  _ "
                         "  | _| _||_| _ |_   ||_||_|"
                         "  ||_  _|  | _||_|  ||_| _ "
                         ""])))))

(deftest test-legible?
  (is (legible? [0 0 0 0 0 0 0 5 1]))
  (is (not (legible? [4 9 0 0 6 7 7 1 \?])))
  (is (not (legible? [1 2 3 4 \? 6 7 8 \?]))))

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
  sequence of digits.  Also verifies that the line was legible."
  (prop/for-all [digits digits-generator]
    (and (= digits (line->digits (digits->line digits)))
         (legible? digits))))

(defspec generated-test-line->digits digits->line->digits)

(def smudged-digits-chooser
  "Chooses a random number of digits to smudge"
  (gen/fmap (fn [[n digits]]
              (take n digits))
            (gen/tuple (gen/choose 1 9)
                       (gen/shuffle [0 1 2 3 4 5 6 7 8]))))

(defn smudge-digit
  "Smudges the given digit in the line by mutating pixel.  Ensures that the
  smudged digit isn't a different digit."
  [line digit]
  (let [row (int (rand 3))
        col (+ (* 3 digit) (int (rand 3)))
        new-line (update-in line [row]
                            (fn [pixel-row]
                              (str (.substring pixel-row 0 col)
                                   (rand-nth (vec (set/difference #{\space \_ \|}
                                                                  (hash-set (.charAt pixel-row col)))))
                                   (.substring pixel-row (inc col)))))
        smudge-digit (->> (take 3 new-line)
                          (map (fn [s] (.substring s (* 3 digit) (* 3 (inc digit)))))
                          (apply str))]
    (if (not= \? (pixels->digit smudge-digit))
      (recur line digit)
      new-line)))

(defn smudge-line
  "Smudges the given digits in a OCR line."
  [line digits-to-smudge]
  (reduce smudge-digit line digits-to-smudge))

(def smudged-line-generator
  "Creates a smudged OCR line and the result that is expected to be result from
  reading the line."
  (gen/fmap (fn [[some-digits digits-to-smudge]]
              [(smudge-line (digits->line some-digits) digits-to-smudge)
               (reduce (fn [digits n]
                         (assoc digits n \?))
                       some-digits
                       digits-to-smudge)])
            (gen/tuple (gen/vector (gen/choose 0 9) 9)
                       smudged-digits-chooser)))

(def smudged-digits-properties
  "Verifies that a smudged line is read appropriately.  In particular, that the
  correct digits are read as smudged and that the result is considered
  illegible."
  (prop/for-all [[smudged-line expected] smudged-line-generator]
    (let [actual (line->digits smudged-line)]
      (and (= expected actual)
           (not (legible? actual))))))

(defspec generated-smudged-line->digits smudged-digits-properties)
