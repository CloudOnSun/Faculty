import string
import tsplib95
from networkx import Graph



class GraphProcessor:
    def __init__(self, path: string):
        self.__path = path
        self.__graph = None

    def __readFile(self):
        problem = tsplib95.load(self.__path)
        return problem

    def create_network(self):
        problem: tsplib95.models.StandardProblem = self.__readFile()
        cost_matrix: list = []
        for i in range(1, problem.dimension+1):
            row = []
            for j in range(1, problem.dimension+1):
                row.append(problem.get_weight(i,j))
            cost_matrix.append(row)

        feromon_matrix: list = []
        for i in range(1, problem.dimension+1):
            row = []
            for j in range(1, problem.dimension+1):
                row.append(1)
            feromon_matrix.append(row)
        return cost_matrix, feromon_matrix

