(ns hassuimmat-sanat.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:gen-class
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))

;; A pattern used to determine which characters are allowed in a word
(def word-characters-pattern #"[a-zA-ZåÅäÄöÖüÜ-]+")

;; A pattern used to determine which characters are not vowels in a word
(def non-vowel-pattern #"[BbCcDdFfGgHhJjKkLlMmNnPpQqRrSsTtVvWwXxZz-]+")

(def book-filename "alastalon_salissa.txt")

;; Reads a resource file
;; Parameters
;;     filename : String
;;         - The name of the file to be read
(defn read-file [filename]
  (slurp (io/resource filename)))

;; Creates a collection of separate words from text
;; Uses word-characters-pattern
;; Parameters
;;     text : String
;;         - The text to be split into words
(defn text-into-words [text]
  (re-seq word-characters-pattern text))

;; A thread-first macro that uses above functions
;; Result is a collections of words
;; Parameters
;;     filename : String
;;         - The name of the file to be read
(defn read-word-coll [filename]
  (-> filename
      read-file
      text-into-words))

;; Splits each word in a collection into collections of strings of vowel sequences
;; Uses non-vowel-pattern
;; Parameters
;;     word-col : Collection
;;         - Collection containing the words
(defn words-into-vowel-colls [word-coll]
  (map #(clojure.string/split % non-vowel-pattern) word-coll))

;; Returns true if given string is not empty, false if it is.
;; Parameters
;;     text : String
;;         - The string to be tested
(defn not-blank? [string]
  (false? (clojure.string/blank? string)))

;; Filters out strings that are empty from a col
;; Parameters
;;     string-col : Collection
;;         - Collection containing strings
(defn filter-out-empty-strings [string-coll]
  (filter (fn[x](not-blank? x)) string-coll))

;; Filters out all empty strings from a collection of string collections
;; Parameters
;;     string-cols : Collection
;;         - Collection containing string collections
(defn filter-out-empty-strings-from-string-colls [string-colls]
  (map #(filter-out-empty-strings %) string-colls))

;; A thread-first macro that uses above functions
;; Returns a collection of vowel sequence collections
;; Parameters
;;     filename
;;         - The name of the file to be read
(defn read-vowels-coll [filename]
  (-> filename
      read-word-coll
      words-into-vowel-colls
      filter-out-empty-strings-from-string-colls))

;; Takes a collection of strings and counts the characters
;; Returns a collection of integers
;; Parameters
;;     string-coll : Collection
;;         - A collection of strings
(defn count-chars [string-coll]
  (map count string-coll))

;; Takes a collection of string collections
;; Turns strings into integers representing the count of characters in each string
;; Parameters
;;     string-colls : Collection
;;         - A collection containing collections of strings
(defn count-chars-in-strings-coll [string-colls]
  (map count-chars string-colls))

;; Takes all empty collections and adds one item: 0
;; This is used because function (apply max) doesn't work with empty colls
;; Parameters
;;     colls : Collection
;;         - Collection containing empty and non-empty collections
(defn empty-colls-to-coll-containing-zero [colls]
  (map #(if (== 0 (count %)) (conj % 0) %) colls))

;; A thread-fist macro that uses above functions
;; Returns a collection of collections that contain sizes of vowel sequences
;; Parameters
;;     filename : String
;;         - The name of the file to be read
(defn read-vowel-counts [filename]
  (-> filename
      read-vowels-coll
      count-chars-in-strings-coll
      empty-colls-to-coll-containing-zero))

;; Calculates the funny points of a vowel count
;; Formula: n×2^n where n is the vowel count
;; Parameters
;;     vowel-count : integer
;;         - The size of vowel sequence
(defn calculate-funny-points [vowel-count]
  (* vowel-count (int (Math/pow 2 vowel-count))))

;; Sums up all integers in a collection
;; Parameters
;;     int-coll : Collection
;;         - Collection of integers
(defn sum-coll [int-coll]
  (reduce + int-coll))

;; Calculates the funny points for all vowel-counts
;; Returns a list of integers representing each word's funny points
;; Parameters
;;     vowel-counts-coll : Collection
;;         - A collection containing collections of integers
(defn calculate-vowel-counts-funny-points [vowel-counts-coll]
  (map (fn[x](sum-coll (map #(calculate-funny-points %) x))) vowel-counts-coll))

;; Takes a coll and returns a collection with each item paired with index number
;; Parameters
;;     coll : Collection
;;         - A collection
(defn indexify-coll [coll]
  (map-indexed (fn[index item] [index item]) coll))

;; A thread-first macro that uses above functions
;; Returns a collection of vectors containing index number and funny points
;; Parameters
;;     filename : String
;;         - The name of the file to be read
(defn read-indexed-funny-points [filename]
  (-> filename
      read-vowel-counts
      calculate-vowel-counts-funny-points
      indexify-coll))

;; Returns the max item from an indexed collection
;; Parameter
;;     indexed-coll : Collection
;;         - A collection containing vectors with index number and integer item
(defn max-from-indexed-coll [indexed-coll]
  (apply max (map (fn[[_ x]] x) indexed-coll)))

;; Uses all the above functions together to read the funniest words of a book
;; Parameter
;;     filename : String
;;         - The name of the file to be read
(defn read-funniest-words [filename]
  (let [word-coll (read-word-coll filename)
        indexed-funny-point-coll (read-indexed-funny-points filename)
        max-funny-points (max-from-indexed-coll indexed-funny-point-coll)]
    (map #(get (into [] word-coll) %)
         (map (fn[[x _]] x)
              (filter (fn[[_ x]] (== x max-funny-points))
                      indexed-funny-point-coll)))))

;; Handler function for handling AWS Lambda request
;; Returns the funniest word(s) in json format
(defn -handleRequest [this is os context]
  (let [w (io/writer os)]
    (-> (read-funniest-words book-filename)
        (json/write w))
    (.flush w)))

;; Main function
(defn -main [& args]
  (println (read-funniest-words book-filename)))
