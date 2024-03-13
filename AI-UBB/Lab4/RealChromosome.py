from dataclasses import dataclass, field
import random
import numpy as np


@dataclass(order=True)
class Chromosome:
    __problParam: object = field(compare=False)
    __repres: list = field(compare=False)
    __fitness: float = field(compare=True)

    MUTATION_RATE = 0.5

    def __init__(self, problParam=None):
        self.__problParam = problParam

        self.__repres = list(np.random.permutation(self.__problParam['noDim']))

        self.__fitness = 0.0

    @property
    def repres(self):
        return self.__repres

    @property
    def fitness(self):
        return self.__fitness

    @repres.setter
    def repres(self, l=[]):
        self.__repres = l

    @fitness.setter
    def fitness(self, fit=0.0):
        self.__fitness = fit

    def crossover(self, c):
        parent1 = c.__repres
        parent2 = self.__repres
        child = [None] * len(parent1)
        start, end = sorted(random.sample(range(len(parent1)), 2))
        child[start:end + 1] = parent1[start:end + 1]

        new_elements = [elem for elem in parent2 if elem not in child]
        child[0:start] = new_elements[0:start]
        child[end+1:] = new_elements[start:]
        offspring = Chromosome(c.__problParam)
        offspring.repres = child
        return offspring


    def mutation(self):
        if random.random() < self.MUTATION_RATE:
            for _ in range(15):
                index1, index2 = random.sample(range(len(self.__repres)), 2)
                self.__repres[index1], self.__repres[index2] = self.__repres[index2], self.__repres[index1]

    def __str__(self):
        return '\nChromo: ' + str(self.__repres) + ' has fit: ' + str(self.__fitness)

    def __repr__(self):
        return self.__str__()

    def __eq__(self, c):
        return self.__repres == c.__repres and self.__fitness == c.__fitness
