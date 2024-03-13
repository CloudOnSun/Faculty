from queue import Queue

dx = [0, 0, -1, 1]
dy = [-1, 1, 0, 0]

"""
starts from a given position in the matrix (x1, y1) which should be equal to 0 in the matrix
marks the position with -1 and goes to every neighbour of it which also equals to 0 and also marks it with 0
it goes to every neighbour of neighbours which equals to 0 and so on until all reachable position which equals to 0 are
reached
"""
def fill(matrix: [], n: int, m: int, x1: int, y1: int):
    q = []
    q.append((x1,y1))
    matrix[x1][y1] = -1
    while q.__len__() > 0:
        loc = q[0]
        q.pop(0)
        x = loc[0]
        y = loc[1]
        for i in range(4):
            x2 = x + dx[i]
            y2 = y + dy[i]
            if n > x2 >= 0 and m > y2 >= 0:
                if matrix[x2][y2] == 0:
                    matrix[x2][y2] = -1
                    q.append((x2, y2))

"""
finds all the positions, which equals to 0, reachable from the borders and marks them with -1
all the remaining 0s are fully surrounded by 1s so they become 1s
all the initial positions marked with -1 become 0s back
"""
def outside_zero(matrix: [], n: int, m: int):
    for i in range(n):
        if matrix[i][0] == 0:
            fill(matrix, n, m, i, 0)
        if matrix[i][m-1] == 0:
            fill(matrix, n, m, i, m-1)
    for j in range(m):
        if matrix[0][j] == 0:
            fill(matrix, n, m, 0, j)
        if matrix[n-1][j] == 0:
            fill(matrix, n, m, n-1, j)
    for i in range(n):
        for j in range(m):
            if matrix[i][j] == -1:
                matrix[i][j] = 0
            elif matrix[i][j] == 0:
                matrix[i][j] = 1
    for l in matrix:
        print(l)

print("Case:")
outside_zero(  [[1,1,1,1,0,0,1,1,0,1],
                [1,0,0,1,1,0,1,1,1,1],
                [1,0,0,1,1,1,1,1,1,1],
                [1,1,1,1,0,0,1,1,0,1],
                [1,0,0,1,1,0,1,1,0,0],
                [1,1,0,1,1,0,0,1,0,1],
                [1,1,1,0,1,0,1,0,0,1],
                [1,1,1,0,1,1,1,1,1,1]], 8, 10)