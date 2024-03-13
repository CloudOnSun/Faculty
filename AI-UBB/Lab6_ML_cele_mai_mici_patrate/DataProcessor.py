import csv

import matplotlib.axes
import matplotlib.pyplot as plt


class DataReader:

    def loadData(self, fileName, inputVariabName, outputVariabName):
        data: list = []
        dataNames: list = []
        with open(fileName) as csv_file:
            csv_reader = csv.reader(csv_file, delimiter=',')
            line_count = 0
            for row in csv_reader:
                if line_count != 0:
                    data.append(row)
                else:
                    dataNames = row
                line_count += 1

        selectedVariable = [dataNames.index(varName) for varName in inputVariabName]
        inputs = [[float(data[i][var]) for var in selectedVariable] for i in range(len(data))]

        selectedOutput = [dataNames.index(varName) for varName in outputVariabName]
        outputs = [[float(data[i][var]) for var in selectedOutput] for i in range(len(data))]

        return inputs, outputs

