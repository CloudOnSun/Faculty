import math
import pydoc

import numpy as np
from utils import generateNewValue
import matplotlib.pyplot as plt
from random import randint
import heapq
from heapq import heappush, heappop
from RealChromosome import Chromosome
from pygmlparser.Parser import Parser
from pygmlparser.Graph import Graph
import networkx as nx
import string


class GraphProcessor:
    def __init__(self, path: string):
        self.__path = path
        self.__graph = None

    def __readFile(self):
        parser: Parser = Parser()
        parser.loadGML(self.__path)
        parser.parse()
        self.__graph: Graph = parser.graph

    def create_network(self):
        self.__readFile()
        nodes: Graph.Nodes = self.__graph.graphNodes  # a map of id -> Node objects
        edges: Graph.Edges = self.__graph.graphEdges  # list of Edge objects
        nrNodes = nodes.__len__()
        matrix = np.array([0]*(nrNodes*nrNodes)).reshape(nrNodes, nrNodes)
        for edge in edges:
            matrix[edge.source][edge.target] = 1
            matrix[edge.target][edge.source] = 1
        net = {}
        net['noNodes'] = nodes.__len__()
        net["mat"] = matrix
        degrees = []
        noEdges = edges.__len__()
        degrees = [sum(x) for x in matrix]
        net["noEdges"] = noEdges
        net["degrees"] = degrees
        return net

    def create_network2(self):
        file = open(self.__path, "r")
        nrNodes = 0
        el = None
        for line in file:
            elems = line.split("\t")
            if nrNodes < int(elems[0]):
                nrNodes = int(elems[0])
                el = elems
            if nrNodes < int(elems[1]):
                nrNodes = int(elems[1])
                el = elems
        nrNodes += 1
        #matrix = np.array([0]*(nrNodes*nrNodes)).reshape(nrNodes, nrNodes)
        file.close()
        matrix = []
        file = open(self.__path, "r")
        for i in range(317080):
            matrix.append([])
            for j in range(317080):
                matrix[-1].append(0)
        noEdges = 0
        for line in file:
            elems = line.split("\t")
            matrix[int(elems[0])][int(elems[1])] = 1
            noEdges += 1
        net = {}
        net['noNodes'] = nrNodes
        net["mat"] = matrix
        degrees = []
        noEdges = 0
        degrees = [sum(x) for x in matrix]
        net["noEdges"] = noEdges
        net["degrees"] = degrees
        return net


class GA:
    def __init__(self, param=None, problParam=None):
        self.__param = param
        self.__problParam = problParam
        self.__population = []
        self.__probabilities = []
        self.__fitnesses = []

    @property
    def population(self):
        return self.__population

    def initialisation(self):
        for _ in range(0, self.__param['popSize']):
            c = Chromosome(self.__problParam)
            heappush(self.__population, c)
            self.__fitnesses.append(0)

    def evaluation(self):
        for c in self.__population:
            c.fitness = self.__problParam['function'](c.repres, self.__problParam['net'])

    def calculate_probabilities(self):
        # for i in range(0, self.__param['popSize']):
        #     self.__fitnesses[i] = self.__population[i].fitness + 0.5
        self.__fitnesses = [val.fitness + 0.5 for val in self.__population]
        total_fitnesses = sum(self.__fitnesses)
        self.__probabilities = np.array(self.__fitnesses) / total_fitnesses

    def bestChromosome(self):
        best = self.__population[self.__population.__len__() - 1]
        return best

    def worstChromosome(self):
        worst = self.__population[0]
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
            off = p1.crossover(p2)
            off.mutation()
            heappush(newPop, off)
        self.__population = newPop
        self.evaluation()

    def oneGenerationElitism(self):
        newPop = [self.bestChromosome()]
        for _ in range(self.__param['popSize'] - 1):
            p1 = self.__population[self.selection()]
            p2 = self.__population[self.selection()]
            off = p1.crossover(p2)
            heappush(newPop, off)
        self.__population = newPop
        self.evaluation()

    def oneGenerationSteadyState(self):
        for _ in range(self.__param['popSize']):
            p1 = self.__population[self.selection()]
            p2 = self.__population[self.selection()]
            off = p1.crossover(p2)
            off.mutation()
            off.fitness = self.__problParam['function'](off.repres, self.__problParam['net'])
            worst = self.worstChromosome()
            if off.fitness > worst.fitness:
                heappop(self.__population)
                heappush(self.__population, off)
                self.calculate_probabilities()


def fcEval_modularity(chromosom: [], net: {}):
    noNodes = net['noNodes']
    mat = net['mat']
    degrees = net['degrees']
    noEdges = net['noEdges']
    M = 2 * noEdges
    Q = 0.0

    node_pairs = [(i, j) for i in range(noNodes) for j in range(i + 1, noNodes) if chromosom[i] == chromosom[j]]
    Q += sum([mat[i][j] - degrees[i] * degrees[j] / M for i, j in node_pairs])

    return Q * 2 / M


def fcEval_conductance(chromosom: [], net: {}):
    noNodes = net['noNodes']
    mat = net['mat']
    degrees = net['degrees']
    noEdges = net['noEdges']
    M = 2 * noEdges
    # Calculate the conductance score for each community
    conductance_scores = []
    for community in set(chromosom):
        # Get the nodes in the community
        community_nodes = [i for i in range(noNodes) if chromosom[i] == community]

        # Calculate the cut size and volume for the community
        cut_size = 0
        volume = 0
        for i in community_nodes:
            volume += degrees[i]
            for j in range(noNodes):
                if i != j and chromosom[j] != community and mat[i][j] > 0:
                    cut_size += 1

        # Calculate the conductance score for the community
        conductance_scores.append(cut_size / min(volume, M - volume))

    # Calculate the average conductance score for all communities
    avg_conductance = sum(conductance_scores) / len(conductance_scores)

    # Return the fitness as the inverse of the average conductance score
    return 1 / avg_conductance


def fcEval_modularity_balance(chromosom: [], net: {}):
    noNodes = net['noNodes']
    mat = net['mat']
    degrees = net['degrees']
    noEdges = net['noEdges']
    M = 2 * noEdges

    # Compute the modularity of the partition
    node_pairs = [(i, j) for i in range(noNodes) for j in range(i + 1, noNodes) if chromosom[i] == chromosom[j]]
    Q = sum([mat[i][j] - degrees[i] * degrees[j] / M for i, j in node_pairs]) * 2 / M

    # Compute the balance of the partition
    community_sizes = [0] * (max(chromosom) + 1)
    for i in range(noNodes):
        community_sizes[chromosom[i]] += 1
    max_community_size = max(community_sizes)
    min_community_size = min(community_sizes)
    balance = (max_community_size - min_community_size) / noNodes

    # Combine the modularity and balance into a single fitness value
    alpha = 0.8
    fitness = alpha * Q + (1 - alpha) * (1 - balance)

    return fitness


def fcEval_modularity_conductance(communities, param):
    noNodes = param['noNodes']
    mat = param['mat']
    degrees = param['degrees']
    noEdges = param['noEdges']

    total_conductance = 0.0
    for i in range(0, noNodes):
        # Calculate the conductance of each community
        community_nodes = [j for j in range(noNodes) if communities[j] == communities[i]]
        boundary_edges = [mat[i][j] for j in range(noNodes) if communities[j] != communities[i]]
        boundary_nodes = [j for j in range(noNodes) if communities[j] != communities[i]]

        internal_degree = sum([degrees[j] for j in community_nodes])
        external_degree = sum(boundary_edges)
        community_conductance = external_degree / (internal_degree + external_degree)

        # Add the conductance to the total modularity score
        total_conductance += community_conductance

    # Return the average conductance over all communities
    return 1 - (total_conductance / noNodes)


def fcEval_modularity_conductance2(communities, graph_params):
    num_nodes = graph_params['noNodes']
    adjacency_matrix = graph_params['mat']
    node_degrees = graph_params['degrees']
    num_edges = graph_params['noEdges']

    total_conductance = 0.0
    for i in range(0, num_nodes):
        # Calculate the conductance of each community
        community_nodes = [j for j in range(num_nodes) if communities[j] == communities[i]]
        boundary_edges = sum([adjacency_matrix[i][j] for j in range(num_nodes) if communities[j] != communities[i]])
        internal_degree = sum([node_degrees[j] for j in community_nodes])
        community_conductance = boundary_edges / (internal_degree + boundary_edges)

        # Add the conductance to the total conductance score
        total_conductance += community_conductance

    # Return the average conductance over all communities
    average_conductance = total_conductance / num_nodes
    modularity_conductance_score = 1 - average_conductance
    return modularity_conductance_score


def num_connected_components(communities, param):
    noNodes = param['noNodes']
    mat = param['mat']
    visited = [False] * noNodes
    num_components = 0
    for i in range(0, noNodes):
        if not visited[i]:
            dfs(i, visited, mat, communities)
            num_components += 1
    return num_components


def dfs(node, visited, mat, communities):
    visited[node] = True
    for neighbor in range(0, len(mat)):
        if mat[node][neighbor] == 1 and not visited[neighbor] and communities[neighbor] == communities[node]:
            dfs(neighbor, visited, mat, communities)


def coverage(communities, param):
    noNodes = param['noNodes']
    mat = param['mat']
    coveredNodes = set()
    for i in range(noNodes):
        if communities[i] not in coveredNodes:
            for j in range(noNodes):
                if communities[j] == communities[i]:
                    coveredNodes.add(j)
    return len(coveredNodes) / noNodes


def coverage_balance_fitness(communities, param):
    matrix = param['mat']
    num_nodes = len(matrix)
    num_communities = len(set(communities))
    fitness = 0

    for c in set(communities):
        # count the number of nodes in the community
        community_size = sum(1 for i in range(num_nodes) if communities[i] == c)

        # calculate the proportion of nodes in the community
        community_coverage = community_size / num_nodes

        # calculate the balance of the community size
        community_balance = 1 - abs(community_size - (num_nodes / num_communities)) / (num_nodes / num_communities)

        # add the balance to the fitness
        fitness += community_coverage * community_balance

    # normalize the fitness by the number of communities
    fitness /= num_communities

    return fitness


def closeness_betweenness_fitness(communities, param):
    matrix = param['mat']
    num_nodes = len(matrix)
    num_communities = len(set(communities))
    G = nx.Graph(matrix)
    fitness = 0

    for c in set(communities):
        # get the nodes in the community
        community_nodes = [i for i in range(num_nodes) if communities[i] == c]

        # calculate the average closeness centrality of the nodes in the community
        closeness_centrality = sum(nx.closeness_centrality(G, u) for u in community_nodes) / len(community_nodes)

        # add the closeness centrality to the fitness
        fitness += closeness_centrality

    # normalize the fitness by the number of communities
    fitness /= num_communities

    # calculate the betweenness centrality of the edges connecting different communities
    edges_between_communities = [(u, v) for u, v in G.edges() if communities[u] != communities[v]]
    betweenness_centrality = sum(
        nx.edge_betweenness_centrality(G, normalized=True, weight=None)[(u, v)] for u, v in
        edges_between_communities)

    # add the betweenness centrality to the fitness
    fitness += betweenness_centrality

    return fitness


class CommunityDetector:
    def __init__(self, path: string, noGen: int, noCommunities: int, fcEval, popSize: int):
        graph_processor = GraphProcessor(path)
        self.net = graph_processor.create_network()

        self.gaParam = {'popSize': popSize, 'noGen': noGen}
        self.problParam = {'min': 1, 'max': noCommunities, 'function': fcEval,
                           'noDim': self.net['noNodes'], 'net': self.net}
        self.ga = GA(self.gaParam, self.problParam)

    def plot_network(self, network, communities=[1, 1, 1, 1, 1, 1]):
        np.random.seed(123)  # to freeze the graph's view (networks uses a random view)
        A = np.matrix(network)
        G = nx.from_numpy_matrix(A)
        pos = nx.spring_layout(G)  # compute graph layout
        plt.figure(figsize=(15, 15))  # image is 8 x 8 inches
        nx.draw_networkx_nodes(G, pos, node_size=600, cmap=plt.cm.RdYlBu, node_color=communities)
        nx.draw_networkx_edges(G, pos, alpha=0.3)
        plt.show()

    def run(self):
        self.ga.initialisation()
        self.ga.evaluation()
        self.ga.calculate_probabilities()

        best_chromosom = self.ga.bestChromosome()
        bests = []

        for i in range(self.gaParam['noGen']):
            print(i)
            best = self.ga.bestChromosome()
            # print(best.fitness)
            bests.append(best.fitness)
            if best.fitness > best_chromosom.fitness:
                best_chromosom = best
            self.ga.oneGenerationSteadyState()

        plt.plot(bests)
        self.plot_network(self.net['mat'], best_chromosom.repres)


com = CommunityDetector('data/real/dolphins/dolphins.gml', 100, 2, num_connected_components, 100)
com.run()
