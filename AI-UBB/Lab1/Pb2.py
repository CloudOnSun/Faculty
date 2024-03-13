from math import sqrt

"""
Calculates the euclidian distance between 2 points and prints it out at the standard output
x1: horizontal coordinate of the first point
y1: vertical coordinate of the first point
x2: horizontal coordinate of the second point
y2: vertical coordinate of the second point
"""
def euclidian_distance(x1: int, y1: int, x2: int, y2: int):
    print(sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)))

print("Easy case:")
euclidian_distance(1,5,4,1)

print("Medium case:")
euclidian_distance(20, 55, 345, 679)

print("Hard case:")
euclidian_distance(1560, 6479, 32454564, 64544223)