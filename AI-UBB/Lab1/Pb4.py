import string

"""
finds all the words which appears exactly one time in a text
"""
def single_time_words(text: string):
    words = text.split()
    single: dict = {}
    for w in words:
        if single.get(w) is None:
            single[w] = "u"
        else:
            single.update({w: "d"})
    for w in single:
        if single.get(w) == "u":
            print(w)


print("Easy case:")
single_time_words("ana are ana are mere rosii ana")

print("Medium case:")
single_time_words("am fost la piata si la magazin am cumparat un kilogram de mere si un kilogram de pere mere au fost"
                  " rele pentru ca magazin este vechi pere au fost bune daca mai merg la piata voi cumparat si niste"
                  " prune din acelea de toamna nu din acelea de vara")

print("Hard case:")
file = open("pb4.txt", "r")
single_time_words(file.read())