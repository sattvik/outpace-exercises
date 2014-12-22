(ns outpace-exercise.ocr)

(defn pixels->digit
  "Converts a sequence of nine 'pixels' into a digit."
  [pixels]
  (condp = pixels
    " _ | ||_|" 0
    "     |  |" 1
    " _  _||_ " 2
    " _  _| _|" 3
    "   |_|  |" 4
    " _ |_  _|" 5
    " _ |_ |_|" 6
    " _   |  |" 7
    " _ |_||_|" 8
    " _ |_| _|" 9))

(defn line->digits
  "Converts a line of 'pixelated' OCR digits into a vector of digits."
  [lines]
  (->> (take 3 lines)
       ; partition each line into groups of three pixels (one character)
       (map (fn [line] (partition 3 line)))
       ; transpose the lines/characters
       (apply map list)
       ; concatenate each row of pixels into one 9-pixel array
       (map (fn [pixel-line] (apply concat pixel-line)))
       ; convert the pixels array into a string
       (map (fn [pixels] (apply str pixels)))
       ; convert the string into a digit
       (map pixels->digit)))
