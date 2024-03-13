import string

import numpy as np
import networkx as nx
import matplotlib.pyplot as plt
import warnings
from pygmlparser.Parser import Parser
from pygmlparser.Graph import Graph
from pygmlparser.Edge import Edge
from pygmlparser.Node import Node

warnings.simplefilter('ignore')


class Community:

    def __init__(self, node: int, matrix: list) -> None:
        self.edges = []
        self.vertices = [node]
        self.matrix = matrix

    def get_edges(self):
        return self.edges

    def get_verticies(self):
        return self.vertices

    def add_vertices(self, ver: list):
        for v in ver:
            for node in self.vertices:
                if self.matrix[v][node] == 1:
                    self.edges.append((v, node))
            self.vertices.append(v)

    def get_nr_edges(self):
        return self.edges.__len__()


def read_file(file_path: string):
    parser: Parser = Parser()
    parser.loadGML(file_path)
    parser.parse()
    graph: Graph = parser.graph
    return graph


def process_file(file_id: int):
    if file_id == 1:
        return read_file('data/real/dolphins/dolphins.gml')
    elif file_id == 2:
        return read_file('data/real/football/football.gml')
    elif file_id == 3:
        return read_file('data/real/karate/karate.gml')
    elif file_id == 4:
        return read_file('data/real/krebs/krebs.gml')
    elif file_id == 5:
        return read_file('data/real/graph10.gml')
    elif file_id == 6:
        return read_file('data/real/graph20.gml')
    elif file_id == 7:
        return read_file('data/real/graph30.gml')
    elif file_id == 8:
        return read_file('data/real/graph40.gml')
    elif file_id == 9:
        return read_file('data/real/graph50.gml')
    elif file_id == 10:
        return read_file('data/real/graph60.gml')
    else:
        print("incorrect id")
        exit(0)


def plot_network(network, communities=[1, 1, 1, 1, 1, 1]):
    np.random.seed(123)  # to freeze the graph's view (networks uses a random view)
    A = np.matrix(network)
    G = nx.from_numpy_matrix(A)
    pos = nx.spring_layout(G)  # compute graph layout
    plt.figure(figsize=(15, 15))  # image is 8 x 8 inches
    nx.draw_networkx_nodes(G, pos, node_size=600, cmap=plt.cm.RdYlBu, node_color=communities)
    nx.draw_networkx_edges(G, pos, alpha=0.3)
    plt.show()


def are_connected(com1: Community, com2: Community, edges: []) -> int:
    v1 = com1.get_verticies()
    v2 = com2.get_verticies()
    nr = 0
    for edge in edges:
        if edge.source in v1 and edge.target in v2 or edge.source in v2 and edge.target in v1:
            nr += 1
    return nr


def greedy_communities_detection(graph: Graph, matrix: list, nr_com: int):
    comunities: list = list()
    # Retrieve the graph nodes
    nodes: Graph.Nodes = graph.graphNodes  # a map of id -> Node objects
    # Retrieve the graph edges
    edges: Graph.Edges = graph.graphEdges  # list of Edge objects

    for node in nodes:
        com = Community(node, matrix)
        comunities.append(com)

    const = edges.__len__() * edges.__len__()

    while comunities.__len__() != nr_com:
        max_q = -10000000
        c1 = -1
        c2 = -1
        for i in range(comunities.__len__()):
            for j in range(i + 1, comunities.__len__()):
                nr_inter = are_connected(comunities[i], comunities[j], edges)
                if nr_inter > 0:
                    v1 = comunities[i].get_verticies()
                    v2 = comunities[j].get_verticies()
                    nr_com1 = 0
                    nr_com2 = 0
                    for edge in edges:
                        if edge.source in v1 or edge.target in v1:
                            nr_com1 += 1
                        if edge.source in v2 or edge.target in v2:
                            nr_com2 += 1
                    q = 2 * (nr_inter / edges.__len__() - nr_com1 * nr_com2 / const)
                    if q > max_q:
                        max_q = q
                        c1 = i
                        c2 = j
        com2 = comunities[c2]
        com1 = comunities[c1]
        comunities.remove(com2)
        com1.add_vertices(com2.get_verticies())

    divergent = []
    for node in nodes:
        for i in range(comunities.__len__()):
            if node in comunities[i].get_verticies():
                divergent.append(i)
                break

    return divergent


def run(command: int, nr_com: int):
    graph = process_file(command)
    # Retrieve the graph nodes
    nodes: Graph.Nodes = graph.graphNodes  # a map of id -> Node objects
    # Retrieve the graph edges
    edges: Graph.Edges = graph.graphEdges  # list of Edge objects
    matrix = []
    for i in range(nodes.__len__()):
        matrix.append([])
        for j in range(nodes.__len__()):
            matrix[-1].append(0)
    for edge in edges:
        matrix[edge.source][edge.target] = 1
        matrix[edge.target][edge.source] = 1
    divergent = greedy_communities_detection(graph, matrix, nr_com)
    for i in range(divergent.__len__()):
        print(i+1, "", divergent[i]+1)
    plot_network(matrix, divergent)


if __name__ == "__main__":
    while(True):
        print("0 - Exit")
        print("Select communities:")
        print("1 - dolphins")
        print("2 - football")
        print("3 - karate")
        print("4 - krebs")
        print("5 - MyTest1")
        print("6 - MyTest2")
        print("7 - MyTest3")
        print("8 - MyTest4")
        print("9 - MyTest5")
        print("10 - MyTest6")

        com = input()
        if com == "0":
            break
        print("Nr of communities wanted: ")
        nr = input()
        run(int(com), int(nr))
