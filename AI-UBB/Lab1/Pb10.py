
"""
goes to every line and finds the one with most 1s
in each line it searches from left to right the first 1 and
            the number of 1 is the subtraction of the position from the length of the length of the line
"""
def max_line(matrix: list, n: int, m: int):
    max1 = 0
    line = -1
    for i in range(n):
        for j in range(m):
            if matrix[i][j] == 1:
                nr = m - j
                if nr > max1:
                    max1 = nr
                    line = i
    print("Line: ", line + 1, "  Nr of 1: ", max1)

print("Easy case:")
max_line([[0,0,0,1,1],
          [0,1,1,1,1],
          [0,0,1,1,1]], 3, 5)

print("Medium case:")
max_line([[0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1],
          [0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
          [0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1],
          [0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
          [0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1],
          [0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
          [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1],
          [0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
          [0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1],
          [0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1]], 10, 21)

print("Hard case:")
m = []
for i in range(2000):
    x = []
    for j in range(2000-i):
        x.append(0)
    for j in range(i):
        x.append(1)
    m.append(x)
max_line(m, 2000, 2000)
