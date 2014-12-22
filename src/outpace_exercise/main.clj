(ns outpace-exercise.main
  (:require [outpace-exercise.ocr :as ocr])
  (:import [java.io BufferedReader])
  (:gen-class))

(defn valid-input?
  "Returns true when the given input is valid.  An input is considered valid if
  it consists of four strings where the first three are 27 characters long and
  the last one is empty."
  [lines]
  (let [[should-be-data should-be-empty] (split-at 3 lines)]
    (and (every? string? lines)
         (every? (fn [^String s] (= 27 (.length s))) should-be-data)
         (.isEmpty (first should-be-empty)))))

(defn -main
  "Reads lines from *in* and writes results to *out*.  Aborts on an invalid
  input."
  [& _]
  (loop [lines (partition 4 (line-seq (BufferedReader. *in*)))]
    (when-let [line (first lines)]
      (if (valid-input? line)
        (do
          (println (apply str (ocr/line->digits line)))
          (recur (next lines)))
        (binding [*out* *err*]
          (println "Invalid input:")
          (dorun (map println line)))))))
