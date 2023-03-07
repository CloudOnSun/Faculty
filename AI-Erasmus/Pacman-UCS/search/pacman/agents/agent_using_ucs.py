#!/usr/bin/env python3
from game.controllers import PacManControllerBase
from game.pacman import Game, DM, Direction
from typing import List
import sys
from os.path import dirname

# hack for importing from parent package
sys.path.append(dirname(dirname(dirname(__file__))))
from search_templates import *
from ucs import ucs


class PacProblem(Problem):
    def __init__(self, game: Game) -> None:
        self.game: Game = game

    def initial_state(self) -> int:
        return self.game.pac_loc

    def actions(self, state: int) -> List[int]:
        actions: List[int] = []
        # if self.game.score > 12000:
        #     return actions
        for i in [0, 1, 2, 3]:
            if self.game.get_neighbor(state, i) != -1:
                actions.append(i)
        return actions

    def result(self, state: int, action: int) -> int:
        return self.game.get_neighbor(state, action)

    def is_goal(self, state: int) -> bool:
        if state == -1:
            return False

        if self.game.is_edible(0):
            if self.game.get_ghost_loc(0) == state:
                return True

        if self.game.is_edible(1):
            if self.game.get_ghost_loc(1) == state:
                return True

        if self.game.is_edible(2):
            if self.game.get_ghost_loc(2) == state:
                return True

        if self.game.is_edible(3):
            if self.game.get_ghost_loc(3) == state:
                return True

        if self.game.fruit_loc > 0:
            if self.game.fruit_loc == state:
                return True
            # else:
            #     return False

        someone_in_lair = self.game.is_in_lair(0) or self.game.is_in_lair(1) or self.game.is_in_lair(
            2) or self.game.is_in_lair(3)
        someone_edible = self.game.is_edible(0) or self.game.is_edible(1) or self.game.is_edible(
            2) or self.game.is_edible(3)

        # power pill
        far_ghost = self.game.get_target(state, self.game.ghost_locs, False, DM.PATH)

        ppill = self.game.get_power_pill_index(state)
        if ppill != -1 and not someone_edible:
            if self.game.check_power_pill(ppill):
                return True

        pill = self.game.get_pill_index(state)
        if pill != -1 and not someone_edible:
            if self.game.check_pill(pill):
                return True
        return False

    def cost(self, state: int, action: int) -> float:
        new_state = self.result(state, action)

        ghosts_neighb = [self.game.get_ghost_neighbors(0), self.game.get_ghost_neighbors(1),
                         self.game.get_ghost_neighbors(2),
                         self.game.get_ghost_neighbors(3)]

        near_ghost = self.game.get_target(new_state, self.game.ghost_locs, True, DM.MANHATTAN)
        dist_near_gh = self.game.get_euclidean_distance(new_state, near_ghost)

        if dist_near_gh < 20:
            #print(dist_near_gh)
            for i in [0, 1, 2, 3]:
                if self.game.is_edible(i):
                    if near_ghost == self.game.get_ghost_loc(i):
                        return 1
            if dist_near_gh < 5:
                return 100000000
            if dist_near_gh < 10:
                return 100000
            return 10000

        if self.game.fruit_loc == new_state:
            return 1

        someone_in_lair = self.game.is_in_lair(0) or self.game.is_in_lair(1) or self.game.is_in_lair(
            2) or self.game.is_in_lair(3)

        far_ghost = self.game.get_target(new_state, self.game.ghost_locs, False, DM.MANHATTAN)

        ppill = self.game.get_power_pill_index(new_state)
        if ppill != -1:
            if self.game.check_power_pill(ppill):
                #print(self.game.get_manhattan_distance(new_state, far_ghost))
                if self.game.get_euclidean_distance(new_state, far_ghost) > 60:
                    return 1000
                return 1

        dirs = self.game.ghost_dirs

        return 5


class Agent_Using_UCS(PacManControllerBase):
    def tick(self, game: Game) -> None:
        prob = PacProblem(game)
        sol = ucs(prob)
        if sol is None or not sol.actions:
            pass
            # if self.verbose:
            #     print("No path found.", file=sys.stderr)
        else:
            self.pacman.set(sol.actions[0])
