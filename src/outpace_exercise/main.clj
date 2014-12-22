(ns outpace-exercise.main
  (:require [clojure.java.io :as io]
            [outpace-exercise.ocr :as ocr])
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

(defn process
  "Process the input from `in` and places the output in `out`.  In the case of
  an invalid input, the processing will abort an error message will be print to
  standard error."
  [in out]
  (binding [*out* out]
    (loop [lines (partition 4 (line-seq (BufferedReader. in)))]
      (when-let [line (first lines)]
        (if (valid-input? line)
          (do
            (println (apply str (ocr/line->digits line)))
            (recur (next lines)))
          (binding [*out* *err*]
            (println "Invalid input:")
            (dorun (map println line))))))))

(defn -main
  "Process an input file of scanned account numbers and outputs to an output
  file of processed account numbers.  If no arguments are given, input is
  standard input and output is standard output.  If one argument is given, it
  is considered the path to an input file and output is sent to standard
  output.  If two arguments are given, the first is the path to an input file
  and the second is the path to an output file."
  ([]
   (process *in* *out*))
  ([in]
   (with-open [in (io/reader in)]
     (process in *out* )))
  ([in out]
   (with-open [in (io/reader in)
               out (io/writer out)]
     (process in out))))
