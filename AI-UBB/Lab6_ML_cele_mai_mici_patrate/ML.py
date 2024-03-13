from DataProcessor import DataReader
import numpy as np
import matplotlib as plt
from sklearn import linear_model
from sklearn.metrics import mean_squared_error


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

    def learn_on_the_model(self):

        trainIn, trainOut, validIn, validOut = self.divide_train_validation()

        # model initialisation
        regressor = linear_model.LinearRegression()
        # training the model by using the training inputs and known training outputs
        regressor.fit(trainIn, trainOut)
        # save the model parameters

        w0, w1, w2 = regressor.intercept_, regressor.coef_[0], regressor.coef_[1]
        print('the learnt model: f(x) = ', w0, ' + ', w1, ' * GPD + ', w2, ' * free')

        computedValidationOutputs = regressor.predict(validIn)

        error = mean_squared_error(validOut, computedValidationOutputs)
        print('prediction error (tool):  ', error)
        self.learn_on_the_model_manual()

    def learn_on_the_model_manual(self):

        trainIn, trainOut, validIn, validOut = self.divide_train_validation()

        w0, w1, w2 = self.fit(trainIn, trainOut)
        print('the learnt model manualy: f(x) = ', w0, ' + ', w1, ' * GPD + ', w2, ' * free')

        computedOutputs = self.computed_outputs_manual(w0, w1, w2, validIn)
        error = mean_squared_error(validOut, computedOutputs)
        print('prediction error (manualy):  ', error)


    def computed_outputs_manual(self, w0, w1, w2, validIn):
        computedOutputs = []

        for elem in validIn:
            computedOutputs.append(w0 + w1*elem[0] + w2*elem[1])

        return computedOutputs

    # learn a linear univariate regression model by using training inputs (x) and outputs (y)
    def fit(self, x, y):

        x1 = [var[0] for var in x]
        x2 = [var[1] for var in x]
        n = x1.__len__()

        x1square = 0
        for elem in x1:
            x1square += elem * elem

        x2square = 0
        for elem in x2:
            x2square += elem * elem

        x1x2 = 0.00000000000001
        for e1, e2 in zip(x1, x2):
            x1x2 += e1 * e2

        det = x1square * x2square - x1x2 * x1x2

        x1y = 0
        x2y = 0
        for i in range(x1.__len__()):
            x1y += x1[i] * y[i]
            x2y += x2[i] * y[i]

        w2 = (x1square * x2y - x1x2 * x1y) / det
        w1 = (x2square * x1y - x1x2 * x2y) / det
        w0 = (sum(y) / n) - w1 * (sum(x1) / n) - w2 * (sum(x2) / n)

        return w0, w1, w2

