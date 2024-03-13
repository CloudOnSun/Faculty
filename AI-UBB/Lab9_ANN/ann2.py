import numpy as np
from matplotlib import pyplot as plt
from sklearn.preprocessing import StandardScaler
np.set_printoptions(suppress=True)
np.random.seed(42)
from sklearn.metrics import accuracy_score

from utils import Utils

u, t = Utils.readData()

trainIn, trainOut, validIn, validOut = Utils.divide_train_validation(u, t)


feature_set = np.array([np.array(i) for i in trainIn])
output_set = np.array([np.array(i) for i in trainOut])


def sigmoid(x):
    return 1/(1+np.exp(-x))

def sigmoid_der(x):
    return sigmoid(x) *(1-sigmoid (x))

def softmax(A):
    expA = np.exp(A)
    return expA / expA.sum(axis=1, keepdims=True)


instances = feature_set.shape[0]
attributes = feature_set.shape[1]
hidden_nodes = 4
output_labels = 3

weightsHiddenLayer = np.random.rand(attributes, hidden_nodes)
biasHiddenLayer = np.random.randn(hidden_nodes)

weightsOutputLayer = np.random.rand(hidden_nodes, output_labels)
biasOutputLayer = np.random.randn(output_labels)
learningRate = 10e-4

error_cost = []

for epoch in range(1000):
#--- Feedforward

#----------- from input layer to hidden layer
    normalHidden = np.dot(feature_set, weightsHiddenLayer) + biasHiddenLayer
    activatedHidden = sigmoid(normalHidden)

#----------- from hidden layer to output layer
    normalOutput = np.dot(activatedHidden, weightsOutputLayer) + biasOutputLayer
    activatedOutput = softmax(normalOutput)


#--- Back Propagation

#----------- delta weights output layer

    dcost_dno = activatedOutput - output_set
    dno_dwo = activatedHidden

    dcost_wo = np.dot(dno_dwo.T, dcost_dno)

    dcost_bo = dcost_dno

#----------- delta hidden layer

    dno_dah = weightsOutputLayer
    dcost_dah = np.dot(dcost_dno, dno_dah.T)
    dah_dnh = sigmoid_der(normalHidden)
    dnh_dwh = feature_set
    dcost_wh = np.dot(dnh_dwh.T, dah_dnh * dcost_dah)

    dcost_bh = dcost_dah * dah_dnh

#----------- update Weights

    weightsHiddenLayer -= learningRate * dcost_wh
    biasHiddenLayer -= learningRate * dcost_bh.sum(axis=0)

    weightsOutputLayer -= learningRate * dcost_wo
    biasOutputLayer -= learningRate * dcost_bo.sum(axis=0)

    loss = np.sum(-output_set * np.log(activatedOutput))
    print(epoch, 'Loss function value: ', loss)
    error_cost.append(loss)

plt.plot(error_cost)
plt.show()

#--- validation phase
validation_set = np.array([np.array(i) for i in validIn])
validOut = np.array([np.array(i) for i in validOut])

normalHidden = np.dot(validation_set, weightsHiddenLayer) + biasHiddenLayer
activatedHidden = sigmoid(normalHidden)

normalOutput = np.dot(activatedHidden, weightsOutputLayer) + biasOutputLayer
activatedOutput = softmax(normalOutput)
for i in range(len(validOut)):
    print("Actual: {}, Predicted: {}".format(validOut[i], activatedOutput[i]))

loss = np.sum(-validOut * np.log(activatedOutput))
print('Loss function value: ', loss)

validOut = np.argmax(validOut, axis=1)
activatedOutput = np.argmax(activatedOutput, axis=1)

acc = accuracy_score(validOut, activatedOutput)
print("Accuracy: ", acc*100, "%")