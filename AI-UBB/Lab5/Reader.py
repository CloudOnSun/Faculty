import string
import csv
import numpy as np

class Reader:

    def __init__(self, path: str, second_path: str = None):
        self.__path = path
        self.__second_path = second_path

    def readClassificationCSVFlowers(self):

        realLabels: list = []
        computedLabels: list = []

        with open(self.__path, newline='') as csvfile:
            csvfile.readline()
            reader = csv.reader(csvfile, delimiter=',')
            for row in reader:
                realLabels.append(row[0])
                computedLabels.append(row[1])

        labelNames = list(set(realLabels))

        return realLabels, computedLabels, labelNames

    def readClassificationProbabilitiesTxT(self):

        realLabels: list = []
        computedLabels: list = []

        with open(self.__path, newline='') as file:
            for line in file:
                elems = line.split(" ")
                sample: list = []
                for elem in elems:
                    sample.append(float(elem))
                computedLabels.append(sample)

        with open(self.__second_path, newline='') as file:
            for line in file:
                elems = line.split(" ")
                sample: list = []
                for elem in elems:
                    sample.append(float(elem))
                realLabels.append(sample)
        return realLabels, computedLabels

    def readRegressionCSVSport(self):
        realOutPut = []
        computedOutPut = []

        realWeight = []
        realWaist = []
        realPulse = []
        computedWeight = []
        computedWaist = []
        computedPulse = []

        with open(self.__path, newline='') as csvfile:
            csvfile.readline()
            reader = csv.reader(csvfile, delimiter=',')
            for row in reader:
                realWeight.append(int(row[0]))
                realWaist.append(int(row[1]))
                realPulse.append(int(row[2]))
                computedWeight.append(int(row[3]))
                computedWaist.append(int(row[4]))
                computedPulse.append(int(row[5]))

        realOutPut.append(realWeight)
        realOutPut.append(realWaist)
        realOutPut.append(realPulse)
        computedOutPut.append(computedWeight)
        computedOutPut.append(computedWaist)
        computedOutPut.append(computedPulse)

        return realOutPut, computedOutPut