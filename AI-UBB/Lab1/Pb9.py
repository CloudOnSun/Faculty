
"""
calculates the sum of every sub-matrixes from the list
"""
def square_sum(pairs: list, matrix: list, n: int, m: int):
    buffered = []
    for i in range(n):
        buffered.append([])
    buffered[0].append(matrix[0][0])
    for j in range(1, m):
        buffered[0].append(buffered[0][j-1] + matrix[0][j])

    for i in range(1, n):
        buffered[i].append(buffered[i-1][0] + matrix[i][0])

    for i in range(1, n):
        for j in range(1, m):
            buffered[i].append(buffered[i][j-1] + buffered[i-1][j] - buffered[i-1][j-1] + matrix[i][j])

    for pair in pairs:
        x1: int = pair[0]
        y1: int = pair[1]
        x2: int = pair[2]
        y2: int = pair[3]
        sum = buffered[x2][y2]
        if x1 > 0:
            sum = sum - buffered[x1-1][y2]
        if y1 > 0:
            sum = sum - buffered[x2][y1-1]
        if x1 > 0 and y1 > 0:
            sum = sum + buffered[x1-1][y1-1]
        print("(", x1, y1, ") , (", x2, y2, ") --> sum =", sum)

print("Case:")
square_sum([(1,1,3,3), (2,2,4,4)],
           [[0, 2, 5, 4, 1],
            [4, 8, 2, 3, 7],
            [6, 3, 4, 6, 2],
            [7, 3, 1, 8, 3],
            [1, 5, 7, 9, 4]], 5, 5
           )