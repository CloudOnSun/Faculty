# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

from statistics import mean

import numpy as np

from utils import Utils
from ann import ANN


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    nn = ANN([4, 8, 3], Utils.linear, Utils.linear_derivative)
    u, t = Utils.readData()
    trainIn, trainOut, validIn, validOut = Utils.divide_train_validation(u, t)

    for i in range(100):
        print(i)
        loss = []
        for j in range(len(trainIn)):
            loss.append(nn.computeLoss(trainIn[j], trainOut[j]))
        lossMean = []
        loss = np.array(loss).transpose()
        for l in loss:
            lossMean.append(sum(l) / len(l))
        # for l in nn.layers:
        #     print(l)
        #     for node in l.neurons:
        #         print(node.weights)
        nn.backPropag(lossMean, 0.01)

    # compute the errors
    diff = []
    for i in range(len(validIn)):
        predicted = nn.feedForward(validIn[i])
        # diff.append(abs(predicted[0] - t[i][0]))
        print("Actual: {}, Predicted:{}".format(validOut[i], predicted))

    # print("Mean of errors: {}".format(mean(diff)))

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
