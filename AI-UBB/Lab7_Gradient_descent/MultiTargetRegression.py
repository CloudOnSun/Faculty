import numpy as np

from sklearn import linear_model
from sklearn.metrics import mean_squared_error
from sklearn import datasets as ds


class MLMutliTarget:

    def __init__(self):
        data = ds.load_linnerud(return_X_y=True)
        self.__inputs = data[0]
        self.__outputs = data[1]

    def divide_train_validation(self):
        np.random.seed(5)
        indexes = [i for i in range(len(self.__inputs))]
        trainSample = np.random.choice(indexes, int(0.8 * len(self.__inputs)), replace=False)
        validationSample = [i for i in indexes if not i in trainSample]

        trainInputs = [self.__inputs[i] for i in trainSample]
        trainOutputs = [self.__outputs[i] for i in trainSample]

        validationInputs = [self.__inputs[i] for i in validationSample]
        validationOutputs = [self.__outputs[i] for i in validationSample]

        return trainInputs, trainOutputs, validationInputs, validationOutputs

    def learn_the_model(self):

        trainIn, trainOut, validIn, validOut = self.divide_train_validation()

        regressor = linear_model.SGDRegressor(alpha=0.001, max_iter=1000)

        trainOut = np.array(trainOut).T
        validOut = np.array(validOut).T

        print("Target features: Weight, Waist, Pulse")

        for i in range(len(trainOut)):
            train = trainOut[i]
            valid = validOut[i]
            regressor.fit(trainIn, train)
            w0, w1, w2, w3 = regressor.intercept_[0], regressor.coef_[0], regressor.coef_[1], regressor.coef_[2]
            print()
            print(i, " Target")
            print('f(x) = ', w0, ' + ', w1, ' * Chins + ', w2, ' * Situps + ', w3, ' * Jumps')

            computedValidationOutputs = regressor.predict(validIn)

            error = mean_squared_error(valid, computedValidationOutputs)
            print('prediction error:  ', error)
