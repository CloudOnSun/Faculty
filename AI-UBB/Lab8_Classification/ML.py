from sklearn.preprocessing import StandardScaler
from MyLogisiticRegression import MyLogisticRegression
from DataReader import DataReader
import numpy as np
from math import sqrt

from sklearn import linear_model
from sklearn.metrics import mean_squared_error


class Machine_Learning:

    def __init__(self, path: str):
        data_reader = DataReader(path)
        self.__inputs, self.__outputs = data_reader.read_file()

    def divide_train_validation(self, division):
        np.random.seed(5)
        indexes = [i for i in range(len(self.__inputs))]
        trainSample = np.random.choice(indexes, int(division * len(self.__inputs)), replace=False)
        validationSample = [i for i in indexes if not i in trainSample]

        trainInputs = [self.__inputs[i] for i in trainSample]
        trainOutputs = [self.__outputs[i] for i in trainSample]

        validationInputs = [self.__inputs[i] for i in validationSample]
        validationOutputs = [self.__outputs[i] for i in validationSample]

        scaler = StandardScaler()
        scaler.fit(trainInputs)
        normalisedTrainData = scaler.transform(trainInputs)
        normalisedTestData = scaler.transform(validationInputs)
        trainInputs = normalisedTrainData
        validationInputs = normalisedTestData

        return trainInputs, trainOutputs, validationInputs, validationOutputs

    def learn_with_tool(self):

        trainIn, trainOut, validIn, validOut = self.divide_train_validation(0.8)

        classifier = linear_model.LogisticRegression(max_iter=1000)

        classifier.fit(trainIn, trainOut)
        computedOutput = classifier.predict(validIn)
        print("First flower: y = ", classifier.intercept_[0],
              " + w1 * ", classifier.coef_[0][0],
              " + w1 * ", classifier.coef_[0][1],
              " + w1 * ", classifier.coef_[0][2],
              " + w1 * ", classifier.coef_[0][3])

        print("Secnd flower: y = ", classifier.intercept_[0],
              " + w1 * ", classifier.coef_[1][0],
              " + w1 * ", classifier.coef_[1][1],
              " + w1 * ", classifier.coef_[1][2],
              " + w1 * ", classifier.coef_[1][3])

        print("Third flower: y = ", classifier.intercept_[0],
              " + w1 * ", classifier.coef_[2][0],
              " + w1 * ", classifier.coef_[2][1],
              " + w1 * ", classifier.coef_[2][2],
              " + w1 * ", classifier.coef_[2][3])

        acc = 0

        for i in range(len(computedOutput)):
            if computedOutput[i] == validOut[i]:
                acc += 1
        print("Accuracy:", acc * 100 / len(computedOutput), "%")

    def loss_function1(self, predicted, real):
        return (predicted - real) ** 2

    def loss_function2(self, predicted, real):
        return abs(predicted - real)

    def learn_manual(self):

        acc = 0
        loss1 = 0
        loss2 = 0
        trainIn, trainOut, validIn, validOut = self.divide_train_validation(0.8)

        trainOut1 = []
        for val in trainOut:
            if val == "Iris-setosa":
                trainOut1.append(0)
            else:
                trainOut1.append(1)
        classifier1 = MyLogisticRegression()
        classifier1.fit(trainIn, trainOut1)
        print("First flower: y = ", classifier1.intercept_,
              " + w1 * ", classifier1.coef_[0],
              " + w1 * ", classifier1.coef_[1],
              " + w1 * ", classifier1.coef_[2],
              " + w1 * ", classifier1.coef_[3])

        trainOut2 = []
        for val in trainOut:
            if val == "Iris-versicolor":
                trainOut2.append(0)
            else:
                trainOut2.append(1)
        classifier2 = MyLogisticRegression()
        classifier2.fit(trainIn, trainOut2)
        print("Secnd flower: y = ", classifier2.intercept_,
              " + w1 * ", classifier2.coef_[0],
              " + w1 * ", classifier2.coef_[1],
              " + w1 * ", classifier2.coef_[2],
              " + w1 * ", classifier2.coef_[3])

        trainOut3 = []
        for val in trainOut:
            if val == "Iris-virginica":
                trainOut3.append(0)
            else:
                trainOut3.append(1)
        classifier3 = MyLogisticRegression()
        classifier3.fit(trainIn, trainOut3)
        print("Third flower: y = ", classifier3.intercept_,
              " + w1 * ", classifier3.coef_[0],
              " + w1 * ", classifier3.coef_[1],
              " + w1 * ", classifier3.coef_[2],
              " + w1 * ", classifier3.coef_[3])

        computedOutputs1 = classifier1.predict(validIn)
        computedFloat1 = classifier1.predictFloat(validIn)
        for i in range(len(computedOutputs1)):
            if computedOutputs1[i] == 0:
                if validOut[i] == "Iris-setosa":
                    acc += 1
                    loss1 += self.loss_function1(computedFloat1[i], 0)
                    loss2 += self.loss_function2(computedFloat1[i], 0)

        computedOutputs2 = classifier2.predict(validIn)
        computedFloat2 = classifier2.predictFloat(validIn)
        for i in range(len(computedOutputs2)):
            if computedOutputs2[i] == 0:
                if validOut[i] == "Iris-versicolor":
                    acc += 1
                    loss1 += self.loss_function1(computedFloat2[i], 0)
                    loss2 += self.loss_function2(computedFloat2[i], 0)

        computedOutputs3 = classifier3.predict(validIn)
        computedFloat3 = classifier3.predictFloat(validIn)
        for i in range(len(computedOutputs3)):
            if computedOutputs3[i] == 0:
                if validOut[i] == "Iris-virginica":
                    acc += 1
                    loss1 += self.loss_function1(computedFloat3[i], 0)
                    loss2 += self.loss_function2(computedFloat3[i], 0)


        print("Accuracy manual:", acc * 100 / len(validOut), "%")

        print("MeanSquareError: ", sqrt(loss1 / len(validOut)))

        print("MeanAbsolutError: ", loss2 / len(validOut))

    def learn_manual_incrucisata(self):

        accMean = 0
        for i in range(1, 9):
            acc = 0
            division = i/10
            trainIn, trainOut, validIn, validOut = self.divide_train_validation(division)

            trainOut1 = []
            for val in trainOut:
                if val == "Iris-setosa":
                    trainOut1.append(0)
                else:
                    trainOut1.append(1)
            classifier1 = MyLogisticRegression()
            classifier1.fit(trainIn, trainOut1)

            trainOut2 = []
            for val in trainOut:
                if val == "Iris-versicolor":
                    trainOut2.append(0)
                else:
                    trainOut2.append(1)
            classifier2 = MyLogisticRegression()
            classifier2.fit(trainIn, trainOut2)

            trainOut3 = []
            for val in trainOut:
                if val == "Iris-virginica":
                    trainOut3.append(0)
                else:
                    trainOut3.append(1)
            classifier3 = MyLogisticRegression()
            classifier3.fit(trainIn, trainOut3)

            computedOutputs1 = classifier1.predict(validIn)
            for i in range(len(computedOutputs1)):
                if computedOutputs1[i] == 0:
                    if validOut[i] == "Iris-setosa":
                        acc += 1


            computedOutputs2 = classifier2.predict(validIn)
            for i in range(len(computedOutputs2)):
                if computedOutputs2[i] == 0:
                    if validOut[i] == "Iris-versicolor":
                        acc += 1


            computedOutputs3 = classifier3.predict(validIn)
            for i in range(len(computedOutputs3)):
                if computedOutputs3[i] == 0:
                    if validOut[i] == "Iris-virginica":
                        acc += 1

            accMean += acc * 100 / len(validOut)

        accMean = accMean / 9

        print("Accuracy manual validare incrucisata:", accMean, "%")

