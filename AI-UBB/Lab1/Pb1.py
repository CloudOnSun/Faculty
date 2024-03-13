import string

"""
Receives a text and finds the last word in it in the alphabetical order;
It prints the word to the standard output
text: the text to be processed (only word separated by white spaces)
"""
def process_text(text: string) :
    list = text.split(" ")
    word = list[0]
    word.lower()
    for w in list:
        w.lower()
        if w > word:
            word = w
    print(word)

def process_file():
    file = open("pb1.txt", "r")
    text = file.read()
    process_text(text)

print("Easy case:")
process_text("Ana are mere si pere")

print("Medium case:")
process_text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
             "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
             "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
             "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in "
             "voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
             "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia "
             "deserunt mollit anim id est laborum.")

print("Hard case:")
process_file()

