#!/usr/bin/env python3
import heapq
from heapq import heappush, heappop
from search_templates import Problem, Solution
from typing import Optional
from dataclasses import dataclass, field


@dataclass(order=True)
class Node:
    state: object = field(compare=False)
    parent: "Node" = field(compare=False)
    action: object = field(compare=False)
    cost: float
    depth: int = field(compare=False)

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


def ucs(prob: Problem) -> Optional[Solution]:
    """Return Solution of the problem solved by UCS search."""
    # Your implementation goes here.
    init_state = prob.initial_state()
    root = Node(init_state, None, None, 0, 0)

    frontier: heapq = []
    heappush(frontier, root)

    visited = set()

    while frontier.__len__() != 0:
        node = heappop(frontier)
        state = node.get_state()

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
                new_node = Node(new_state, node, action, cost + new_cost, node.get_depth() + 1)
                heappush(frontier, new_node)

    return None
