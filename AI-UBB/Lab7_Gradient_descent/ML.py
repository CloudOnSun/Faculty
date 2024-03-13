from DataProcessor import DataReader
from MySGDRegressor import MySGDRegression
import numpy as np

from sklearn import linear_model
from sklearn.metrics import mean_squared_error
from sklearn import datasets as ds


class Machine_Learning:

    def __init__(self, path, inputVariabName, outputVariabName):
        self.__path = path
        self.__inputVariabName = inputVariabName
        self.__outputVariabName = outputVariabName
        dataProcessor: DataReader = DataReader()
        input, output = dataProcessor.loadData(self.__path, self.__inputVariabName, self.__outputVariabName)
        self.__inputs = input
        self.__outputs = output


    def divide_train_validation(self):
        np.random.seed(5)
        indexes = [i for i in range(len(self.__inputs))]
        trainSample = np.random.choice(indexes, int(0.8 * len(self.__inputs)), replace=False)
        validationSample = [i for i in indexes if not i in trainSample]

        trainInputs = [self.__inputs[i] for i in trainSample]
        trainOutputs = [self.__outputs[i][0] for i in trainSample]

        validationInputs = [self.__inputs[i] for i in validationSample]
        validationOutputs = [self.__outputs[i][0] for i in validationSample]

        return trainInputs, trainOutputs, validationInputs, validationOutputs

    def normalization(self, features, s = None, m = None):
        if m is None:
            m = sum(features) / len(features)
        if s is None:
            s = (1 / len(features) * sum([ (f - m) ** 2 for f in features])) ** 0.5
        featuresZ = [(f - m) / s for f in features]

        return featuresZ, s, m

    def learn_model_tool(self, nrvar):
        trainInNot, trainOutNot, validInNot, validOutNot = self.divide_train_validation()

        trainInNot = np.array(trainInNot).T
        trainIn = []
        sIn = []
        mIn = []
        for t in trainInNot:
            tr, s, m = self.normalization(t)
            trainIn.append(tr)
            sIn.append(s)
            mIn.append(m)
        trainIn = np.array(trainIn).T

        trainOut, sOut, mOut = self.normalization(trainOutNot)

        validInNot = np.array(validInNot).T
        validIn = []
        for i in range(len(sIn)):
            va, none, none2 = self.normalization(validInNot[i], sIn[i], mIn[i])
            validIn.append(va)
        validIn = np.array(validIn).T

        validOut, none, none2 = self.normalization(validOutNot, sOut, mOut)

        regressor = linear_model.SGDRegressor(alpha=0.001, max_iter=1000)
        regressor.fit(trainIn, trainOut)

        if nrvar == 2:
            w0, w1, w2 = regressor.intercept_[0], regressor.coef_[0], regressor.coef_[1]
            print('the learnt model tool: f(x) = ', w0, ' + ', w1, ' * GPD + ', w2, ' * free')
        elif nrvar == 1:
            w0, w1 = regressor.intercept_[0], regressor.coef_[0]
            print('the learnt model tool: f(x) = ', w0, ' + ', w1, ' * GPD')

        computedValidationOutputs = regressor.predict(validIn)

        error = mean_squared_error(validOut, computedValidationOutputs)
        print('prediction error (tool):  ', error)

    def learn_the_model_manual(self, nrvar):
        trainInNot, trainOutNot, validInNot, validOutNot = self.divide_train_validation()

        trainInNot = np.array(trainInNot).T
        trainIn = []
        sIn = []
        mIn = []
        for t in trainInNot:
            tr, s, m = self.normalization(t)
            trainIn.append(tr)
            sIn.append(s)
            mIn.append(m)
        trainIn = np.array(trainIn).T

        trainOut, sOut, mOut = self.normalization(trainOutNot)

        validInNot = np.array(validInNot).T
        validIn = []
        for i in range(len(sIn)):
            va, none, none2 = self.normalization(validInNot[i], sIn[i], mIn[i])
            validIn.append(va)
        validIn = np.array(validIn).T

        validOut, none, none2 = self.normalization(validOutNot, sOut, mOut)

        regressor = MySGDRegression()
        regressor.fit(trainIn, trainOut)

        if nrvar == 2:
            w0, w1, w2 = regressor.intercept_, regressor.coef_[0], regressor.coef_[1]
            print('the learnt model manual: f(x) = ', w0, ' + ', w1, ' * GPD + ', w2, ' * free')
        elif nrvar == 1:
            w0, w1 = regressor.intercept_, regressor.coef_[0]
            print('the learnt model manual: f(x) = ', w0, ' + ', w1, ' * GPD')

        computedValidationOutputs = regressor.predict(validIn)

        error = mean_squared_error(validOut, computedValidationOutputs)
        print('prediction error (manual):  ', error)
