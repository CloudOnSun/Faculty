#!/usr/bin/env python3
from math import sqrt

from game.action import *
from game.board import *
from game.artificial_agent import ArtificialAgent
from dead_square_detector import detect
from typing import List, Union
import sys
from time import perf_counter
from os.path import dirname

from game.board import Board, EDirection, StateMinimal, ETile

# from sokoban.game.action import Action, Move, Push
# from sokoban.game.board import *

# hack for importing from parent package
sys.path.append(dirname(dirname(dirname(__file__))))
from astar import AStar
from search_templates import HeuristicProblem


class MyAgent(ArtificialAgent):
    """
    Logic implementation for Sokoban ArtificialAgent.

    See ArtificialAgent for details.
    """

    def __init__(self, optimal, verbose) -> None:
        super().__init__(optimal, verbose)  # recommended

    def new_game(self) -> None:
        """Agent got into a new level."""
        super().new_game()  # recommended

    @staticmethod
    def think(
            board: Board, optimal: bool, verbose: bool
    ) -> List[Union[EDirection, Action]]:
        """
        Code your custom agent here.
        You should use your A* implementation.

        You can find example implementation (without use of A*)
        in simple_agent.py.
        """

        prob = SokobanProblem(board)
        solution = AStar(prob)
        if not solution:
            return None

        return [a.dir for a in solution.actions]


class SokobanProblem(HeuristicProblem):
    """HeuristicProblem wrapper of Sokoban game."""

    def __init__(self, initial_board) -> None:
        # Your implementation goes here.
        # Hint: __init__(self, initial_board) -> None:
        self.init_board = initial_board
        self.dead_squares = detect(initial_board)
        self.width = initial_board.width
        self.height = initial_board.height
        self.__targets = self.__get_targets()
        self.__max = sqrt(self.init_board.width*self.init_board.width + self.init_board.height*self.init_board.height)
        self.__distances = self.__calc_min_distances()

    def __get_targets(self):
        targets = []
        for x in range(self.init_board.width):
            for y in range(self.init_board.height):
                tile = self.init_board.tile(x, y)
                if ETile.is_target(tile):
                    targets.append((x, y))
        return targets

    def __calc_min_distances(self):
        list = []
        for x in range(self.init_board.width):
            l = []
            for y in range(self.init_board.height):
                l.append(self.__get_min_distacne((x,y)))
            list.append(l)
        return list

    def __get_min_distacne(self, tile) -> float:
        min = self.__max
        x = tile[0]
        y = tile[1]
        for t in self.__targets:
            dist = sqrt((x-t[0])*(x-t[0]) + (y-t[0])*(y-t[0]))
            if dist < min:
                min = dist
        return min

    def initial_state(self) -> Union[Board, StateMinimal]:
        # Your implementation goes here.
        return self.init_board

    def actions(self, state: Union[Board, StateMinimal]) -> List[Action]:
        # Your implementation goes here.
        actions: List[Action] = []
        for m in Move.get_actions():
            if m.is_possible(state):
                actions.append(m)
        for p in Push.get_actions():
            if p.is_possible(state):
                actions.append(p)

        return actions

    def result(
            self, state: Union[Board, StateMinimal], action: Action
    ) -> Union[Board, StateMinimal]:
        # Your implementation goes here.
        cState = state.clone()
        action.perform(cState)
        return cState

    def is_goal(self, state: Union[Board, StateMinimal]) -> bool:
        # Your implementation goes here.
        return state.is_victory()

    def cost(self, state: Union[Board, StateMinimal], action: Action) -> float:
        # Your implementation goes here.
        return 1

    def estimate(self, state: Union[Board, StateMinimal]) -> float:
        # Your implementation goes here.
        heur = 0

        for x in range(state.width):
            for y in range(state.height):
                tile = state.tile(x, y)
                danger = self.dead_squares[x][y]
                if ETile.is_box(tile):
                    if danger:
                        return 100000
                    else:
                        #return 1
                        heur += self.__distances[x][y]
        return heur
