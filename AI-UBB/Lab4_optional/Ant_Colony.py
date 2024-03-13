import random
import numpy as np


class AntColony:

    def __init__(self, cost_matrix: list, feromon_matrix: list):
        self.__cost_matrix = cost_matrix
        self.__feromon_matrix = feromon_matrix
        self.__nrNodes = self.__cost_matrix[0].__len__()
        self.__max_weigth = max([max(i) for i in self.__cost_matrix])

    def change_matrix(self):
        limit = int(self.__nrNodes*2/10)
        nrEdgesToChange = random.randint(0, limit)
        for _ in range(nrEdgesToChange):
            i = random.randint(0, self.__nrNodes-1)
            j = random.randint(0, self.__nrNodes-1)
            while i == j:
                j = random.randint(0, self.__nrNodes - 1)
            self.__cost_matrix[i][j] = random.randint(1, self.__max_weigth)

    def calculate_probability(self, node, visited: list):

        probs: list = []
        sum = 0

        for i in range(self.__nrNodes):
            if i not in visited:
                sum += self.__feromon_matrix[node][i] * (1 / self.__cost_matrix[node][i])

        for i in range(self.__nrNodes):
            if i not in visited:
                probs.append((self.__feromon_matrix[node][i] * (1 / self.__cost_matrix[node][i])) / sum)
            else:
                probs.append(0)

        return probs

    def oneAnt(self):
        current = random.randint(0, self.__nrNodes - 1)
        start = current
        visited: list = [current]
        length = 0

        while visited.__len__() != self.__nrNodes:
            probs = self.calculate_probability(current, visited)
            nextN = np.random.choice(self.__nrNodes, size=1, replace=False, p=probs)
            next = nextN[0]
            length += self.__cost_matrix[current][next]
            visited.append(next)
            current = next

        length += self.__cost_matrix[current][start]

        for i in range(1, self.__nrNodes):
            self.__feromon_matrix[visited[i - 1]][visited[i]] += 10000 / length
            self.__feromon_matrix[visited[i]][visited[i-1]] += 10000 / length

        self.__feromon_matrix[start][current] += 1 / length
        self.__feromon_matrix[current][start] += 1 / length

        for i in range(self.__nrNodes):
            for j in range(self.__nrNodes):
                self.__feromon_matrix[i][j] = (1.0-0.05)*self.__feromon_matrix[i][j]

        chance = random.random()

        if chance < 0.3:
            self.change_matrix()

        return length
