# hassuimmat-sanat

A solution to the first wundernut by Wunderdog:

Hassuimmat sanat / The Funniest Words
http://wunder.dog/hassuimmat-sanat

The wundernut's explanation text (in Finnish):

"Koodauspähkinä: Hassuimmat sanat

Monet ulkomaalaiset pitävät suomen kieltä hassun kuuloisena vokaalien runsauden vuoksi. Erityisen hassulta kuulostaa, jos vokaaleja on monta peräkkäin, kuten kuuluisassa sanassa "hääyöaie", jossa on seitsemän peräkkäistä vokaalia.

Kehitämme tieteellisen tavan sanojen hassuuden pisteytykseen.

Jokainen vokaaliketju saa n×2^n pistettä, jossa n on vokaalien määrä ketjussa. Sanan vokaaliketjujen saamat pisteet lasketaan yhteen, jolloin saadaan sanan hassuuspisteet.

Esimerkiksi sana "koira" saa 10 pistettä, koska "koira" sisältää vokaaliketjut "oi" (2×22 = 8 pistettä) ja "a" (1×21 = 2 pistettä), ja 8 + 2 = 10.

Sana "hääyöaie" saa 896 pistettä, koska vokaaliketju "ääyöaie" saa (7×27 = 896 pistettä).

Jotta hauskuus ei loppuisi kesken, käytämme esimerkkiteoksena Volter Kilven romaania Alastalon salissa, jota pidetään suomalaisen kirjallisuuden mestarinäytteenä hassuuden saralla.

Mikä on Alastalon salissa -romaanin hassuin sana, tai hassuimmat sanat, jos useampi sana saa korkeimmat hassuuspisteet?"

English tl;dr: Words with long vowel sequences are funny. Each vowel sequence in a word gets funny points with formula n×2^n, n being the length of the sequence. Sum up the points to get the word's points. Find the word(s) with the highest funny points in a book.

The solution uses text file containing "Alastalon salissa" by Volter Kilpi. The book is public domain.

## Usage

Run:
lein run

Test:
lein midje

Can be run in AWS Lambda.
See the doc directory for a screenshot of a successful AWS Lambda test output. 

## License

Copyright © 2017 Tomi Piri

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
