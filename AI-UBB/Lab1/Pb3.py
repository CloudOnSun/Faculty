
"""
calculates the scalar product of 2 vectors
"""
def scalar_product(x: list, y: list):
    length = x.__len__()
    sp = 0
    for i in range(0, length):
        sp = sp + x[i] * y[i]

    print(sp)


print("Easy case:")
scalar_product([1, 0, 2, 0, 3], [1, 2, 0, 3, 1])

print("Medium case:")
scalar_product([4, 0, 0, 6, 2, 0, 8, 0, 0, 0, 7, 0, 6, 2, 0, 1, 3, 0, 0, 6, 0, 4, 0, 5, 6, 0, 0, 8, 9, 0, 9, 0, 0],
               [4, 0, 0, 6, 2, 0, 8, 0, 0, 0, 7, 0, 6, 2, 0, 1, 3, 0, 0, 6, 0, 4, 0, 5, 6, 0, 0, 8, 9, 0, 9, 0, 0])

