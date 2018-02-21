# Basic Keyboard Benchmarking Game


![Menu Activity](docs/screenshot2.png "Menu Activity")
![Game Activity](docs/screenshot1.png "App Activity")

__Basic Keyboard Benchmarking Game__ is a simple typing bencharmk game application for android that can be used to measure the keyboard's or the user's performance when typing.

## Game Modes
* Limited Time
* Number of words
* Number of errors
* Number of correct words
* No end

## Word sources
* 12 dicts English words list - one word + preview next word
* Lengthy Text Sample __(not yet implemented)__

## Other options
* Auto-correct on/off
* Skip on fail (if the user fails the word, it just skips to the next word, the user cannot try again the same word)

## Benchmark metrics
__Metrics__
* Options (Game mode, auto-correct, skip on fail, and source of the text)
* Keyboard app
* Timestamp
* Time elapsed
* Correct chars
* Correct words
* Errors
* Total Words
* Number of times backspace keys was entered
* Number of keystrokes
* Number of characters

__Entry Rates__
* Words per minute (WPM)
* Keystrokes per second (KSPS)

__Error Rates__
* Keystrokes per Char (KSPC)
* Minimum String Distance Error Rate (MSD Error Rate). _Only being applied to words that are failed_

__Custom Entry Rates__
* Chars per second
* Words per second

## Sources

* _Soukoreff, R. W., & MacKenzie, I. S. (2003). 140422_CHI ’03_Metrics for text entry research- an evaluation of MSD and KSPC, and a new unified error metric. Proceedings of the Conference on Human Factors in Computing Systems - CHI ’03. https://doi.org/10.1145/642611.642632_
* _Wobbrock, J. O. (2007). Measures of Text Entry Performance. In Text Entry Systems. (pp. 47-74). Elsevier Inc.. DOI: 10.1016/B978-012373591-1/50003-6_
