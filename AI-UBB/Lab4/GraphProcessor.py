import string
import numpy as np
from pygmlparser.Parser import Parser
from pygmlparser.Graph import Graph
import tsplib95



class GraphProcessor:
    def __init__(self, path: string):
        self.__path = path
        self.__graph = None

    def __readFile(self):
        parser: Parser = Parser()
        parser.loadGML(self.__path)
        parser.parse()
        self.__graph: Graph = parser.graph

    def __readFile2(self):
        problem = tsplib95.load(self.__path)
        return problem

    def create_network2(self):
        return self.__readFile2()

    def create_network(self):
        self.__readFile()
        nodes: Graph.Nodes = self.__graph.graphNodes  # a map of id -> Node objects
        edges: Graph.Edges = self.__graph.graphEdges  # list of Edge objects
        nodes_values = nodes.values()
        list_adc = [node.forward_edges + node.backward_edges for node in nodes_values]
        net = {}
        net['noNodes'] = nodes.__len__()
        net['nodes'] = nodes.keys()
        net['noEdges'] = edges.__len__()
        net['edges'] = edges
        net['listAdc'] = list_adc
        return net
        # nodes: Graph.Nodes = self.__graph.graphNodes  # a map of id -> Node objects
        # edges: Graph.Edges = self.__graph.graphEdges  # list of Edge objects
        # nrNodes = nodes.__len__()
        # matrix = np.array([0]*(nrNodes*nrNodes)).reshape(nrNodes, nrNodes)
        # for edge in edges:
        #     matrix[edge.source][edge.target] = 1
        #     matrix[edge.target][edge.source] = 1
        # net = {}
        # net['noNodes'] = nodes.__len__()
        # net["mat"] = matrix
        # degrees = []
        # noEdges = edges.__len__()
        # degrees = [sum(x) for x in matrix]
        # net["noEdges"] = noEdges
        # net["degrees"] = degrees
        # return net

    # def create_network2(self):
    #     file = open(self.__path, "r")
    #     nrNodes = 0
    #     el = None
    #     for line in file:
    #         elems = line.split("\t")
    #         if nrNodes < int(elems[0]):
    #             nrNodes = int(elems[0])
    #             el = elems
    #         if nrNodes < int(elems[1]):
    #             nrNodes = int(elems[1])
    #             el = elems
    #     nrNodes += 1
    #     # matrix = np.array([0]*(nrNodes*nrNodes)).reshape(nrNodes, nrNodes)
    #     file.close()
    #     matrix = []
    #     file = open(self.__path, "r")
    #     for i in range(317080):
    #         matrix.append([])
    #         for j in range(317080):
    #             matrix[-1].append(0)
    #     noEdges = 0
    #     for line in file:
    #         elems = line.split("\t")
    #         matrix[int(elems[0])][int(elems[1])] = 1
    #         noEdges += 1
    #     net = {}
    #     net['noNodes'] = nrNodes
    #     net["mat"] = matrix
    #     degrees = []
    #     noEdges = 0
    #     degrees = [sum(x) for x in matrix]
    #     net["noEdges"] = noEdges
    #     net["degrees"] = degrees
    #     return net
