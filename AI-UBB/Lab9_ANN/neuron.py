from random import random
from random import uniform

"""
Basic neuron class, with a number of inputs and an activation function.
"""


class Neuron:
    def __init__(self, inputs, activationFunction=None):
        self.activationFunction = activationFunction
        self.inputs = inputs
        self.weights = [uniform(-0.9, 0.9) for i in range(self.inputs)]
        self.output = 0

    def setWeights(self, new):
        self.weights = new

    def fireNeuron(self, inputs):

        u = sum([x * y for x, y in zip(inputs, self.weights)])
        self.output = self.activationFunction(u)
        return self.output

    def __str__(self):
        return str(self.weights)