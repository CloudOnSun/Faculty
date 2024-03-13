
from heapq import heappush, heappop

"""
finds the k-highest element from a list
"""
def k_highest(l: list, k: int):
    h = []
    for i in range(0, k):
        heappush(h, l[i])

    for i in range(k, l.__len__()):
        if h[0] < l[i]:
            heappop(h)
            heappush(h, l[i])

    print(h[0])


print("Easy case:")
k_highest([7, 4, 6, 3, 9, 1], 2)

print("Medium case:")
x = []
for i in range(1, 300):
    x.append(i)
k_highest(x, 12)

print("Hard case")
y = []
for i in range(1, 1000000):
    y.append(i)
k_highest(y, 120)

