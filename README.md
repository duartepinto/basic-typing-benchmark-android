# Basic Keyboard Benchmarking Game


![Menu Activity](docs/screenshot2.png "Menu Activity")
![Game Activity](docs/screenshot1.png "App Activity")
![Game with Chi phrases](docs/screensho3.png "Game with Chi phrases")

__Basic Keyboard Benchmarking Game__ is a simple typing bencharmk game application for android that can be used to measure the keyboard's or the user's performance when typing.

## Game Modes
* Limited Time
* Number of words
* Number of errors
* Number of correct words
* No end

## Word sources
* 12 dicts English words list - one word + preview next word
* Chi phrases (Compilation of clauses/sentences) - a clause + continuous feedback while writing (which words are correct or incorrect)

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
* Input stream: Keeps track of all the changes made to input box. Separates each change with a paragraph. Example (when typing the word "_and_"):

    ```
        <- Blank                 
    a
    an
    ans <- The 's' is a mistake and is going to be deleted
    an
    and <- Correct input!
    ```
* Input string: Contains all the input that was counted as submited by the user. Includes errors. 
    ```
    ans <- Error! 
    and <- Correct input!
    ```
* Transcibed String: Constains the correct input for each input submitted by the user plus the next word to be analyzed. With skip on fail deactivated it will repeat the correct input if the user did not enter the input correctly. After the user submited input it will automatically add the next word to the string, even if the user has not submited anything.
    * Without skip on fail
    ```
    and <- Because the user made a mistake this is going to be repeated.  
    and 
    banana <- Next word to be analyzed.
    ```
    * With skip on fail
    ```
    and 
    banana <- Next word to be analyzed.
    ```

__Entry Rates__
* Words per minute (WPM)
* Keystrokes per second (KSPS)

__Error Rates__
* Keystrokes per Char (KSPC)
* Minimum String Distance Error Rate (MSD Error Rate). _Only being applied to words that are failed_
* Corrected error rate
* Uncorrected error rate
* Total error rate

__Custom Entry Rates__
* Chars per second
* Words per second

## Sources

* _Soukoreff, R. W., & MacKenzie, I. S. (2003). 140422_CHI ’03_Metrics for text entry research- an evaluation of MSD and KSPC, and a new unified error metric. Proceedings of the Conference on Human Factors in Computing Systems - CHI ’03. https://doi.org/10.1145/642611.642632_
* _Wobbrock, J. O. (2007). Measures of Text Entry Performance. In Text Entry Systems. (pp. 47-74). Elsevier Inc.. DOI: 10.1016/B978-012373591-1/50003-6_
