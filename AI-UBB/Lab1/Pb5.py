

"""
finds the only element which appears 2 times
"""
def double_element(x: list):
    all: set = set()
    for i in x:
        if i in all:
            print(i)
            break
        else:
            all.add(i)


print("Easy case:")
double_element([1, 2, 3, 4, 2])

print("Medium case:")
double_element([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 8, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50])

y = []
for i in range(0, 10000):
    y.append(i)

y.append(10005)

for i in range(10000, 2000000):
    y.append(i)

print("Hard case:")
double_element(y)
