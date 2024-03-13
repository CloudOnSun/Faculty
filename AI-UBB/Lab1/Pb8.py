"""
creates all binary numbers from 1 to n
"""
def binaryNumbers(n: int):
    q = []
    q.append("1")
    for i in range(0, n):
        nr = q[0]
        print(nr)
        q.pop(0)
        q.append(nr + "0")
        q.append(nr + "1")

print("Easy case:")
binaryNumbers(4)

print("Medium case:")
binaryNumbers(100)

print("Hard case:")
binaryNumbers(10000)


