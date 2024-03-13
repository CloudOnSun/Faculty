import numpy as np
from sklearn.preprocessing import StandardScaler


class Utils:

    @staticmethod
    def sigmoid(x):
        return 1 / (1 + np.exp(-x))

    @staticmethod
    def sigmoid_derivative(x):
        return Utils.sigmoid(x) * (1 - Utils.sigmoid(x))

    @staticmethod
    def linear(x, a=0.1, b=0):
        return a * x + b

    @staticmethod
    def linear_derivative(x, a=0.1, b=0):
        return a

    @staticmethod
    def relu(x):
        return max(0, x)

    @staticmethod
    def relu_derivativ(x):
        return 1

    import numpy as np

    @staticmethod
    def soft_max(x):
        """
        Custom activation function that replaces softmax.
        Computes the exponent of each input element, and then normalizes by the sum of the exponents.
        """
        exp_x = np.exp(x)
        return exp_x / np.sum(exp_x, axis=1, keepdims=True)

    @staticmethod
    def readData():

        dataIn = []
        dataOut = []
        with open('iris.data', newline='') as file:
            for line in file:
                line = line.replace('\n', '')
                d = []
                features = line.split(",")
                if len(features) > 0:
                    for i in range(len(features) - 1):
                        d.append(float(features[i]))
                    dataIn.append(d)
                    f = features[len(features) - 1]
                    if f == "Iris-setosa":
                        dataOut.append([1, 0, 0])
                    elif f == "Iris-versicolor":
                        dataOut.append([0, 1, 0])
                    else:
                        dataOut.append([0, 0, 1])
        indexes = np.random.permutation(len(dataIn))

        input = [dataIn[i] for i in indexes]
        output = [dataOut[i] for i in indexes]
        return input, output

    @staticmethod
    def divide_train_validation(inputs, outputs):
        np.random.seed(5)
        indexes = [i for i in range(len(inputs))]
        trainSample = np.random.choice(indexes, int(0.8 * len(inputs)), replace=False)
        validationSample = [i for i in indexes if not i in trainSample]

        trainInputs = [inputs[i] for i in trainSample]
        trainOutputs = [outputs[i] for i in trainSample]

        validationInputs = [inputs[i] for i in validationSample]
        validationOutputs = [outputs[i] for i in validationSample]

        scaler = StandardScaler()
        scaler.fit(trainInputs)
        normalisedTrainData = scaler.transform(trainInputs)
        normalisedTestData = scaler.transform(validationInputs)
        trainInputs = [list(x) for x in normalisedTrainData]
        validationInputs = [list(x) for x in normalisedTestData]

        return trainInputs, trainOutputs, validationInputs, validationOutputs

    @staticmethod
    def divide_train_validation_no_std(inputs, outputs):
        np.random.seed(5)
        indexes = [i for i in range(len(inputs))]
        trainSample = np.random.choice(indexes, int(0.8 * len(inputs)), replace=False)
        validationSample = [i for i in indexes if not i in trainSample]

        trainInputs = [inputs[i] for i in trainSample]
        trainOutputs = [outputs[i] for i in trainSample]

        validationInputs = [inputs[i] for i in validationSample]
        validationOutputs = [outputs[i] for i in validationSample]

        return trainInputs, trainOutputs, validationInputs, validationOutputs