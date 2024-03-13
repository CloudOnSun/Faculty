from typing import Tuple

from pygmlparser.Parser import Parser
from pygmlparser.Graph import Graph
from pygmlparser.Edge import Edge
from pygmlparser.Node import Node
from pygmlparser.graphics.NodeGraphics import NodeGraphics
from pygmlparser.graphics.EdgeGraphics import EdgeGraphics
from pygmlparser.graphics.Point import Point




# Instantiate a parser, load a file, and parse it!
parser: Parser = Parser()
parser.loadGML('data/real/dolphins/dolphins.gml')
parser.parse()
graph: Graph = parser.graph

# Retrieve the graph nodes
nodes: Graph.Nodes = graph.graphNodes  # a map of id -> Node objects

# Retrieve the graph edges
edges: Graph.Edges = graph.graphEdges  # list of Edge objects

# Directly access the node or edge attributes
node: Node = graph.graphNodes[0]
edge: Edge = graph.graphEdges[0]

node.id      # the id of this node
edge.source  # the source id of this edge

node.is_anon  # whether or not this node actually appeared as a node block
              # in the input GML (or if it was inferred, via edge source/targets)
              # _True_ if inferred, False if actually appeared

node.forward_edges   # List of Edge instances whose source is this node
node.backward_edges  # List of Edge instances whose target is this node

# Special attributes on Edges
edge.source_node  # Node object corresponding to edge.source (which is an id)
edge.target_node  # Node object corresponding to edge.target (which is an id)

# Get the Tulip extensions
edgeGraphics: EdgeGraphics = edge.graphics
nodeGraphics: NodeGraphics = node.graphics

# Get the edge line drawing description
line:  Tuple[Point] = edgeGraphics.line