import numpy as np

from Reader import Reader
from Evaluator import Evaluator

def evaluateLabelsFlowers():
    print()
    print("--- FLOWERS.csv ---")
    reader: Reader = Reader("inputFiles/flowers.csv")
    evaluator: Evaluator = Evaluator()
    realLabels, computedLabels, labelNames = reader.readClassificationCSVFlowers()
    acc, prec, recall = evaluator.evalClassification(realLabels, computedLabels, labelNames)
    print(labelNames)
    print('acc: ', acc, ' precision: ', prec, ' recall: ', recall)

def evaluateRegressionSport():
    print()
    print("--- SPORT.csv ---")
    reader: Reader = Reader("inputFiles/sport.csv")
    evaluator: Evaluator = Evaluator()
    realOutput, computedOutput = reader.readRegressionCSVSport()
    errors = evaluator.evalRegression(realOutput, computedOutput)
    for i in range(errors.__len__()):
        print(i, " error: ", errors[i])

    print("Avg error: ", sum(errors) / errors.__len__())
    loss = 0
    for instReal, instComp in zip(realOutput, computedOutput):
        for real, prob in zip(instReal, instComp):
            loss += (real-prob)*(real-prob)
    print("Loss pentru regresie: ", loss)

def evaluateLabelsBinaryTxt():
    print()
    print("--- binary.txt ---")
    reader: Reader = Reader("inputFiles/evaluation-metrics-main/probabilities-binary.txt",
                            "inputFiles/evaluation-metrics-main/true-binary.txt")
    evaluator: Evaluator = Evaluator()
    realLables, computedLabels = reader.readClassificationProbabilitiesTxT()
    loss = 0
    for instReal, instComp in zip(realLables, computedLabels):
        for real, prob in zip(instReal, instComp):
            loss += (real-prob)*(real-prob)
    print("Loss pentru binary: ", loss)

    realLabels2: list = []
    computedLabels2: list = []
    for inst in realLables:
        index = np.argmax(inst)
        realLabels2.append(index)
    for inst in computedLabels:
        index = np.argmax(inst)
        computedLabels2.append(index)
    acc, prec, recall = evaluator.evalClassification(realLabels2, computedLabels2)
    print('acc: ', acc, ' precision: ', prec, ' recall: ', recall)


def evaluateLabelsMultiClassTxt():
    print()
    print("--- multiClass.txt ---")
    reader: Reader = Reader("inputFiles/evaluation-metrics-main/probabilities-multi-class.txt",
                            "inputFiles/evaluation-metrics-main/true-multi-class.txt")
    evaluator: Evaluator = Evaluator()
    realLables, computedLabels = reader.readClassificationProbabilitiesTxT()
    loss = 0
    for instReal, instComp in zip(realLables, computedLabels):
        for real, prob in zip(instReal, instComp):
            loss += (real - prob) * (real - prob)
    print("Loss pentru Multi Class: ", loss)
    realLabels2: list = []
    computedLabels2: list = []
    for inst in realLables:
        index = np.argmax(inst)
        realLabels2.append(index)
    for inst in computedLabels:
        index = np.argmax(inst)
        computedLabels2.append(index)
    acc, prec, recall = evaluator.evalClassification(realLabels2, computedLabels2)
    print('acc: ', acc, ' precision: ', prec, ' recall: ', recall)

def evaluateLabelsMultiTargetTxt():
    print()
    print("--- multiClass.txt ---")
    reader: Reader = Reader("inputFiles/evaluation-metrics-main/probabilities-multi-target.txt",
                            "inputFiles/evaluation-metrics-main/true-multi-target.txt")
    evaluator: Evaluator = Evaluator()
    realLables, computedLabels = reader.readClassificationProbabilitiesTxT()
    loss = 0
    for instReal, instComp in zip(realLables, computedLabels):
        for real, prob in zip(instReal, instComp):
            loss += (real - prob) * (real - prob)
    print("Loss pentru Multi Target: ", loss)
    computedLabels2: list = []
    for inst in computedLabels:
        sample: list = []
        for prob in inst:
            if prob > 0.5:
                sample.append(1)
            else:
                sample.append(0)
        computedLabels2.append(sample)
    acc, prec, recall = evaluator.evalClassification(realLables, computedLabels2)
    print('acc: ', acc, ' precision: ', prec, ' recall: ', recall)

if __name__ == "__main__":
    evaluateLabelsFlowers()
    evaluateRegressionSport()
    evaluateLabelsBinaryTxt()
    evaluateLabelsMultiClassTxt()
    evaluateLabelsMultiTargetTxt()