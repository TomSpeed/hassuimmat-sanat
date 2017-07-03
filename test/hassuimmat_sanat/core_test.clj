(ns hassuimmat-sanat.core-test
  (:require [midje.sweet :refer :all]
            [hassuimmat-sanat.core :refer :all]))

(def filename "test.txt")

(facts "about hassummat-sanat"
       (fact "read-file reads a file correctly"
             (read-file filename)
             => "”Olisit\n\nottanut Åken mukäan, niin olisi ollut parkkitöpit slüupissa!\n")

       (fact "text-into-words returns a collection of words correctly"
             (text-into-words (read-file filename))
             => '("Olisit" "ottanut" "Åken" "mukäan" "niin" "olisi" "ollut" "parkkitöpit" "slüupissa"))

       (fact "read-word-coll returns a collection of words in correct format"
             (read-word-coll filename)
             => '("Olisit" "ottanut" "Åken" "mukäan" "niin" "olisi" "ollut" "parkkitöpit" "slüupissa"))

       (fact "words-into-vowel-colls returns a collection of vectors with strings only consisting of vowels and empty strings"
             (words-into-vowel-colls (read-word-coll filename))
             => '(["O" "i" "i"] ["o" "a" "u"] ["Å" "e"] ["" "u" "äa"] ["" "ii"] ["o" "i" "i"] ["o" "u"] ["" "a" "i" "ö" "i"] ["" "üu" "i" "a"]))

       (fact "not-blank? returns false when blank"
             (not-blank? "") => false)

       (fact "not-blank? returns true when not blank"
             (not-blank? "a") => true)

       (fact "filter-out-empty-strings returns a collcetion with no empty strings"
             (filter-out-empty-strings ["o" "" "a" "u"])
             => '("o" "a" "u"))

       (fact "filter-out-empty-strings-from-string-colls returns a collection with all empty strings gone"
             (filter-out-empty-strings-from-string-colls (words-into-vowel-colls (read-word-coll filename)))
             => '(("O" "i" "i") ("o" "a" "u") ("Å" "e") ("u" "äa") ("ii") ("o" "i" "i") ("o" "u") ("a" "i" "ö" "i") ("üu" "i" "a")))

       (fact "read-vowels-coll returns a clean collections of vowels"
             (read-vowels-coll filename)
             => '(("O" "i" "i") ("o" "a" "u") ("Å" "e") ("u" "äa") ("ii") ("o" "i" "i") ("o" "u") ("a" "i" "ö" "i") ("üu" "i" "a")))

       (fact "count-chars returns a collection of char counts"
             (count-chars '("aaa" "ee" "i" "ooooo" "uuuu" "yy" "ääääää" "ööö"))
             => '(3 2 1 5 4 2 6 3))

       (fact "count-chars-in-strings-coll returns a collection with collections of all the char counts"
             (count-chars-in-strings-coll (read-vowels-coll filename))
             => '((1 1 1) (1 1 1) (1 1) (1 2) (2) (1 1 1) (1 1) (1 1 1 1) (2 1 1)))

       (fact "empty-colls-to-coll-containing-zero returns a coll with empty colls replaced with a coll containing 0"
             (empty-colls-to-coll-containing-zero '((1 2 3) () () (4 5 6) () (1)))
             => '((1 2 3) (0) (0) (4 5 6) (0) (1)))

       (fact "read-vowel-counts returns a collection of vowel count collections"
             (read-vowel-counts filename)
             => '((1 1 1) (1 1 1) (1 1) (1 2) (2) (1 1 1) (1 1) (1 1 1 1) (2 1 1)))

       (fact "calculate-funny-points returns corrently calculated points"
             (calculate-funny-points 5) => 160)

       (fact "sum-coll returns the sum of items in collection"
             (sum-coll '(1 5 9)) => 15)

       (fact "calculate-vowel-counts-funny-points returns a collection with correctly calculated points"
             (calculate-vowel-counts-funny-points (read-vowel-counts filename))
             => '(6 6 4 10 8 6 4 8 12))

       (fact "indexify-coll returns a collection of vectors with index and item"
             (indexify-coll '(0 1 2)) => '([0 0] [1 1] [2 2]))

       (fact "read-indexed-funny-points returns a collection of vectors with index and correctly calculated points"
             (read-indexed-funny-points filename)
             => '([0 6] [1 6] [2 4] [3 10] [4 8] [5 6] [6 4] [7 8] [8 12]))

       (fact "max-from-indexed-coll returns the maximum value item from indexified collection"
             (max-from-indexed-coll '([0 45] [1 312] [2 1])) => 312)

       (fact "It all works"
             (let [word-coll (read-word-coll filename)
                   indexed-funny-point-coll (read-indexed-funny-points filename)
                   max-funny-points (max-from-indexed-coll indexed-funny-point-coll)]
               (map #(get (into [] word-coll) %)
                    (map (fn[[x _]] x)
                         (filter (fn[[_ x]] (== x max-funny-points))
                                 indexed-funny-point-coll))))
             => '("slüupissa")))
