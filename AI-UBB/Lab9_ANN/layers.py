import random

from neuron import Neuron
from utils import Utils
from random import Random


class Layer:
    def __init__(self, noOfInputs, activationFunction, noOfNeurons):
        self.noOfNeurons = noOfNeurons
        self.neurons = [Neuron(noOfInputs, activationFunction) for i in
                        range(self.noOfNeurons)]
        self.activationFunction = activationFunction

    def forward(self, inputs):
        for x in self.neurons:
            x.fireNeuron(inputs)
        return ([x.output for x in self.neurons])


class FirstLayer(Layer):
    def __init__(self, noOfNeurons, bias=False):
        if bias:
            noOfNeurons = noOfNeurons + 1
        Layer.__init__(self, 1, Utils.linear, noOfNeurons)
        # random = Random()
        # for x in self.neurons:
        #     x.setWeights([random.random()])

    def forward(self, inputs):
        for i in range(len(self.neurons)):
            self.neurons[i].fireNeuron([inputs[i]])
        return ([x.output for x in self.neurons])

