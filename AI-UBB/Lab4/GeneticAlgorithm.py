import random

from RealChromosome import Chromosome
import numpy as np


class GA:
    def __init__(self, param=None, problParam=None):
        self.__param = param
        self.__problParam = problParam
        self.__population = []
        self.__probabilities = []
        self.__fitnesses = [0] * self.__param['popSize']
        self.CROSSOVER_RATE = 0.8
        self.__distances = []
        # self.calc_dist()

    def calc_dist(self):
        problem = self.__problParam['net']
        size = self.__problParam['noDim'] + 1
        self.__distances = [[problem.get_weight(i, j) for j in range(1, size)]
                            for i in range(1, size)]

    @property
    def population(self):
        return self.__population

    def initialisation(self):
        for i in range(0, self.__param['popSize']):
            c = Chromosome(self.__problParam)
            self.__population.append(c)

    def evaluation(self):
        for c in self.__population:
            c.fitness = self.__problParam['function'](c.repres, self.__problParam['net'])

    def calculate_probabilities(self):
        # for i in range(0, self.__param['popSize']):
        #     self.__fitnesses[i] = self.__population[i].fitness + 0.5
        self.__fitnesses = [1 / val.fitness for val in self.__population]
        total_fitnesses = sum(self.__fitnesses)
        self.__probabilities = np.array(self.__fitnesses) / total_fitnesses

    def bestChromosome(self):
        best = min(self.__population)
        return best

    def worstChromosome(self):
        worst = max(self.__population)
        return worst

    def selection(self):
        parent = np.random.choice(len(self.__fitnesses), size=1, replace=False, p=self.__probabilities)
        return parent[0]

    def oneGeneration(self):
        newPop = []
        self.calculate_probabilities()
        for _ in range(self.__param['popSize']):
            p1 = self.__population[self.selection()]
            p2 = self.__population[self.selection()]
            if random.random() < self.CROSSOVER_RATE:
                off = p1.crossover(p2)
                off.mutation()
                newPop.append(off)
            elif p1.fitness < p2.fitness:
                newPop.append(p1)
            else:
                newPop.append(p2)
        self.__population = newPop
        self.evaluation()

    def oneGenerationElitism(self):
        newPop = [self.bestChromosome()]
        self.calculate_probabilities()
        for _ in range(self.__param['popSize'] - 1):
            p1 = self.__population[self.selection()]
            p2 = self.__population[self.selection()]
            if random.random() < self.CROSSOVER_RATE:
                off = p1.crossover(p2)
                off.mutation()
                newPop.append(off)
            elif p1.fitness < p2.fitness:
                newPop.append(p1)
            else:
                newPop.append(p2)
        self.__population = newPop
        self.evaluation()

    def oneGenerationSteadyState(self):
        for _ in range(self.__param['popSize']):
            if random.random() < self.CROSSOVER_RATE:
                p1 = self.__population[self.selection()]
                p2 = self.__population[self.selection()]
                off = p1.crossover(p2)
                off.mutation()
                off.fitness = self.__problParam['function'](off.repres, self.__problParam['net'])
                #worst = self.worstChromosome()
                index = random.randint(0, len(self.__population) - 1)
                indiv = self.__population[index]
                if off.fitness < indiv.fitness:
                    self.__population[index] = off
                    self.calculate_probabilities()
