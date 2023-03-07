#!/usr/bin/env python3
from search_templates import Solution, HeuristicProblem
import heapq
from heapq import heappush, heappop
from dataclasses import dataclass, field
@dataclass(order=True)
class Node:
    state: object = field(compare=False)
    parent: "Node" = field(compare=False)
    action: object = field(compare=False)
    cost: float = field(compare=False)
    depth: int = field(compare=False)
    heuristic: float = field(compare=False)
    fcost : float

    def get_state(self) -> object:
        return self.state

    def get_parent(self) -> "Node":
        return self.parent

    def get_action(self) -> object:
        return self.action

    def get_cost(self) -> float:
        return self.cost

    def get_depth(self) -> int:
        return self.depth


def get_actions(node_crt: Node) -> []:
    all_actions = []
    while node_crt.get_parent() is not None:
        all_actions.insert(0, node_crt.get_action())
        node_crt = node_crt.get_parent()
    return all_actions


def AStar(prob: HeuristicProblem) -> Solution:
    """Return Solution of the problem solved by AStar search."""
    # Your implementation goes here.
    init_state = prob.initial_state()
    root = Node(init_state, None, None, 0, 0, prob.estimate(init_state), prob.estimate(init_state))

    frontier: heapq = []
    heappush(frontier, root)

    visited = set()
    i=0
    while frontier.__len__() != 0:
        node = heappop(frontier)
        state = node.get_state()
        #print(i)
        #i += 1

        if state not in visited:
            cost = node.get_cost()
            visited.add(state)

            if prob.is_goal(state):
                actions_to_goal = get_actions(node)
                return Solution(actions_to_goal, state, node.get_cost())

            actions = prob.actions(state)
            for action in actions:
                new_state = prob.result(state, action)
                new_cost = prob.cost(state, action)
                heur = prob.estimate(new_state)
                new_node = Node(new_state, node, action, cost + new_cost, node.get_depth() + 1,
                                heur, cost+new_cost+heur)
                heappush(frontier, new_node)

    return None

# frontier: heapq = []
#
# heappush(frontier, Node(None, None, None, 5, 0, 3, 8))
# heappush(frontier, Node(None, None, None, 4, 0, 5, 9))
# heappush(frontier, Node(None, None, None, 6, 0, 1, 7))
# for f in frontier:
#     print(f)
#     # return Solution([actions leading to goal], goal_state, path_cost)
