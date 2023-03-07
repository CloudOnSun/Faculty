#!/usr/bin/env python3
from game.dino import *
from game.agent import Agent


class MyAgent(Agent):
    """Reflex agent static class for Dino game."""

    @staticmethod
    def needs_down(game: Game, o: Obstacle) -> bool:
        dino = game.dino
        if dino.Y < o.rect.y + o.rect.height < dino.body.y:
            return True
        return False

    @staticmethod
    def is_close_enouqh(game: Game, o: Obstacle) -> bool:
        if o.rect.coords.x < game.dino.x + 115 + 5 * (game.speed - 5):
            return True
        return False

    @staticmethod
    def needs_short_jump(game: Game, o: Obstacle) -> bool:
        if o.rect.y + o.rect.height == game.GROUND_Y or o.rect.y + o.rect.height > game.dino.Y_DUCK:
            # o2 = game.previous_obstacle
            # if o2.rect.x - o.rect.x - o.rect.width > 100:
            # game.obstacles.appendleft(o2)
            # return True
            # else:
            #   game.obstacles.appendleft(o2)
            return True
        return False

    @staticmethod
    def before_obstacle(game: Game, o: Obstacle):
        if game.dino.x < o.rect.x:
            return True
        return False

    @staticmethod
    def still_on_the_ground(game: Game) -> bool:
        if game.dino.y == game.dino.Y_DUCK or game.dino.y == game.dino.Y:
            return True
        return False

    @staticmethod
    def needs_to_stay_down(game: Game, o: Obstacle) -> bool:
        if game.dino.head.x < o.rect.x + o.rect.width:
            return True
        return False

    @staticmethod
    def still_above_but_safe_to_go_down(game: Game, o: Obstacle) -> bool:
        if o.rect.x + o.rect.width / 6 < game.dino.x < o.rect.x + o.rect.width:
            return True
        return False

    @staticmethod
    def before_end_of_obstacle(game: Game, o: Obstacle) -> bool:
        if game.dino.x < o.rect.x + o.rect.width:
            return True
        return False

    @staticmethod
    def after_obstacle(game: Game, o: Obstacle) -> bool:
        if game.dino.x > o.rect.x + o.rect.width:
            return True
        return False

    @staticmethod
    def almost_after_obstacle(game: Game, o: Obstacle) -> bool:
        if o.rect.x + o.rect.width - 10 < game.dino.x < o.rect.x + o.rect.width:
            return True
        return False

    @staticmethod
    def obstacle_ignore(game: Game, o: Obstacle) -> bool:
        if o.rect.bottom < game.dino.Y:
            return True
        return False

    @staticmethod
    def enough_distance(game: Game, o: Obstacle) -> bool:
        if game.dino.x <= o.rect.x - 115 - 5 * (game.speed - 5):
            return True
        return False

    @staticmethod
    def big_obstacle(game: Game, o: Obstacle) -> bool:
        if o.rect.height > 80 or o.rect.height == 50:
            return True
        return False

    @staticmethod
    def cant_make_down_in_time(game: Game, distance: int, currentObs: Obstacle, nextObs: Obstacle) -> bool:
        # print( currentObs.rect.x, " < ", game.dino.x, " < ", nextObs.rect.x)
        # print("d: ", distance)
        if not MyAgent.still_on_the_ground(game) \
                and currentObs.rect.x < game.dino.x < nextObs.rect.right \
                and (0 < distance < (game.speed + 1) * (game.speed + 1) or 0 < distance < 150) \
                and not MyAgent.obstacle_ignore(game, nextObs):
            #print("d: ", distance, " s: ", game.speed)
            #print("o: ", currentObs.type)
            #print(game.dino.x)
            return True
        # if not MyAgent.still_on_the_ground(game) \
        #        and not MyAgent.is_close_enouqh(game, currentObs):
        #    print("d: ", distance, " s: ", game.speed)
        #    return True
        return False

    @staticmethod
    def safe_place_to_run(game: Game, o: Obstacle):
        if game.dino.x + 90 + 5 * (game.speed - 5) < o.rect.x:
            return True
        return False

    @staticmethod
    def after_jump_before_getting_down(game: Game, o: Obstacle):
        if not MyAgent.still_on_the_ground(game) and game.dino.x <= o.rect.x + o.rect.width / 6:
            return True
        return False

    @staticmethod
    def needs_straight_down(game: Game, o: Obstacle):
        if not MyAgent.still_on_the_ground(game) and o.rect.x + o.rect.width <= game.dino.x:
            return True
        return False

    @staticmethod
    def get_move(game: Game) -> DinoMove:
        """
        Note: Remember you are creating simple-reflex agent, that should not
        store or access any information except provided.
        """
        # for visual debugging intellisense you can use
        # from game.debug_game import DebugGame
        # game: DebugGame = game
        # t = game.add_text(Coords(10, 10), "red", "Text")
        # t.text = "Hello World"
        # game.add_dino_rect(Coords(-10, -10), 150, 150, "yellow")
        # l = game.add_dino_line(Coords(0, 0), Coords(100, 0), "black")
        # l.dxdy.update(50, 30)
        # l.dxdy.x += 50
        # game.add_moving_line(Coords(1000, 100), Coords(1000, 500), "purple")
        # YOUR CODE GOES HERE

        for o in game.obstacles:

            # print(game.dino.x)
            # if game.dino.x > game.WIDTH/3 and game.obstacles.__len__() > 0:
            #     i = 1
            #     currentObs = game.obstacles.__getitem__(game.obstacles.__len__() - i)
            #     while currentObs.rect.right < game.dino.x and i < game.obstacles.__len__():
            #         i = i + 1
            #         currentObs = game.obstacles.__getitem__(game.obstacles.__len__() - i)
            #     if not MyAgent.is_close_enouqh(game, currentObs) and not MyAgent.still_on_the_ground(game):
            #         return DinoMove.DOWN


            if MyAgent.is_close_enouqh(game, o):
                # print(game.speed)
                if MyAgent.obstacle_ignore(game, o):
                    #print("ignore")
                    return DinoMove.NO_MOVE
                if MyAgent.needs_down(game, o) and MyAgent.needs_to_stay_down(game, o):
                    #print("down bird")
                    return DinoMove.DOWN

            if game.obstacles.__len__() >= 2:
                # print("o2--", game.obstacles.__getitem__(0).type)
                # print("o1--", o.type)
                # print("d: ", distance)
                currentObs = game.obstacles.__getitem__(game.obstacles.__len__() - 1)
                nextObs = game.obstacles.__getitem__(game.obstacles.__len__() - 2)
                distance = nextObs.rect.x - currentObs.rect.right
                #print("d: ", distance, " s: ", game.speed)
                if MyAgent.cant_make_down_in_time(game, distance, currentObs, nextObs):
                    #print("stay up")
                    return DinoMove.RIGHT

            # if game.obstacles.__len__() >= 3:
            #     currentObs = game.obstacles.__getitem__(game.obstacles.__len__() - 2)
            #     nextObs = game.obstacles.__getitem__(game.obstacles.__len__() - 3)
            #     distance = nextObs.rect.x - currentObs.rect.right
            #     # print("d: ", distance, " s: ", game.speed)
            #     if MyAgent.cant_make_down_in_time(game, distance, currentObs, nextObs):
            #         print("stay up")
            #         return DinoMove.RIGHT

            if MyAgent.is_close_enouqh(game, o):
                if MyAgent.needs_short_jump(game, o) or not MyAgent.still_on_the_ground(game):
                    # print(game.speed)
                    if MyAgent.still_on_the_ground(game) and MyAgent.before_obstacle(game, o):
                        #print("up")
                        return DinoMove.UP
                    elif MyAgent.after_jump_before_getting_down(game, o):
                        #print("right")
                        return DinoMove.RIGHT
                    elif MyAgent.still_above_but_safe_to_go_down(game, o):
                        #print("down right")
                        return DinoMove.DOWN_RIGHT
                    elif MyAgent.needs_straight_down(game, o):
                        #print("down")
                        return DinoMove.DOWN

            # elif MyAgent.enough_distance(game, o):
            # return DinoMove.RIGHT

        return DinoMove.NO_MOVE
