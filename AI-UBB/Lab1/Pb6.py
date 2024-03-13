
"""
finds the element which appears more than half of the times in a list
"""
def major_element(l: list):
    d: dict = dict()
    for x in l:
        if d.get(x) is None:
            d[x] = 1
        else:
            aux = d[x] + 1
            d.update({x: aux})
            if d[x] >= l.__len__() / 2:
                print(x)
                break


print("Easy case:")
major_element([2, 8, 7, 2, 2, 5, 2, 3, 1, 2, 2])

print("Medium case:")
major_element([3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,
               3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,
               3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,
               3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,
               3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6,3,6,6,2,5,6,2,5,6,7,6,6,1,6,6])

x: list = list()
for i in range(0, 100000):
    x.append(i)

for i in range(0, 100001):
    x.append(3)

print("Hard case:")
major_element(x)