(ns outpace-exercise.main-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer [deftest is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [outpace-exercise.main :refer :all]
            [outpace-exercise.ocr-test :refer [digits->line smudged-line-generator]]
            [outpace-exercise.validator-test :refer [bad-digits-generator good-digits-generator]])
  (:import [java.io File]))

(deftest user-stories
  (let [input (slurp (io/resource "story-input.txt"))
        expected (slurp (io/resource "story-output.txt"))
        in (File/createTempFile "story-" ".in")
        out (File/createTempFile "story-" ".out")]
    (spit in input)
    (-main (.getPath in) (.getPath out))
    (let [result (slurp out)]
      (is (= expected result)))))

(def line-generator
  "Generates a line and its expected process result."
  (gen/frequency [[8 (gen/fmap (fn [d] [(digits->line d)
                                        (apply str d)])
                              good-digits-generator)]
                  [1 (gen/fmap (fn [d] [(digits->line d)
                                        (apply str (conj d " ERR"))])
                              bad-digits-generator)]
                  [1 (gen/fmap (fn [[line result]] [line (apply str (conj result " ILL"))])
                              smudged-line-generator)]]))

(defn make-input-file!
  "Writes a file with the lines from the given data and returns the name of the
  input file."
  [data]
  (let [file (File/createTempFile "generated-" ".in")]
    (with-open [out (io/writer file)]
      (binding [*out* out]
        (doseq [[input _] data]
          (dorun (map println input)))))
    (.getPath file)))

(defn make-expected-file!
  "Writes a file with the expected results from the given data and returns the
  name of the file."
  [data]
  (let [file (File/createTempFile "generated-" ".expected")]
    (with-open [out (io/writer file)]
      (binding [*out* out]
        (doseq [[_ result] data]
          (println result))))
    (.getPath file)))

(def file-generator
  "Generates files with input and expected output."
  (gen/fmap (fn [data]
              [(make-input-file! data)
               (make-expected-file! data)])
            (gen/vector line-generator)))

(defspec generated-file
  (prop/for-all [[input-file expected-file] file-generator]
    (let [output-file (File/createTempFile "generated-" ".out")
          expected (slurp expected-file)]
      (-main input-file  (.getPath output-file))
      (let [actual (slurp output-file)]
        (= expected actual)))))
