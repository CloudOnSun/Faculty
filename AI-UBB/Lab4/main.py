import string

import tsplib95.models

from GraphProcessor import GraphProcessor
from GeneticAlgorithm import GA

import matplotlib.pyplot as plt

from RealChromosome import Chromosome


class ShortestPathDetector:
    def __init__(self, path: string, noGen: int, fitness, popSize: int):
        graph_processor = GraphProcessor(path)
        self.net = graph_processor.create_network2()

        self.gaParam = {'popSize': popSize, 'noGen': noGen}
        self.problParam = {'function': fitness, 'noDim': self.net.dimension, 'net': self.net}
        self.ga = GA(self.gaParam, self.problParam)

    def print_best_solution(self, best):
        print("Fitness ", best.fitness)
        repre: list = best.repres
        for i in range(repre.__len__()):
            print(repre)
            prim = repre[0]
            repre.remove(repre[0])
            repre.append(prim)

    def run(self):
        self.ga.initialisation()
        self.ga.evaluation()
        self.ga.calculate_probabilities()

        best_chromosom: Chromosome = self.ga.bestChromosome()
        bests = []
        all_star = []
        all_star.append(best_chromosom)

        for i in range(self.gaParam['noGen']):
            print(i)
            best = self.ga.bestChromosome()
            # print(best.fitness)
            bests.append(best.fitness)
            if best.fitness < best_chromosom.fitness:
                best_chromosom = best
                all_star.clear()
                all_star.append(best)
            elif best.fitness == best_chromosom.fitness:
                if not best_chromosom.repres.__eq__(best.repres):
                    all_star.append(best)
            if all_star.__len__() > 1:
                break;
            # self.ga.oneGeneration()
            # self.ga.oneGenerationElitism()
            self.ga.oneGenerationSteadyState()

        plt.plot(bests)
        plt.show()
        print(best_chromosom)
        print(all_star)


def fitness_funct2(repres: list, problem):
    i = 0
    fit = 0
    for j in range(1, repres.__len__()):
        edge = repres[i] + 1, repres[j] + 1
        fit += problem.get_weight(*edge)
        i = j
    return fit


def fitness_funct(repres: [], net: {}):
    i = repres[0]
    list_adj = net['listAdc']
    fit = 0
    for j in repres:
        if j != repres[0]:
            edges = list_adj[i]
            edge = [edge for edge in edges if (edge.source == i and edge.target == j)
                    or (edge.source == j and edge.target == i)]
            if edge.__len__() == 0:
                fit += 100
            else:
                fit += edge[0].value
        i = j
    return fit


if __name__ == '__main__':

    detector = ShortestPathDetector("wi29.tsp", 250, fitness_funct2, 1000)
    detector.run()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
