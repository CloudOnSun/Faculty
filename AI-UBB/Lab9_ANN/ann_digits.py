import numpy as np
import matplotlib.pyplot as plt
from sklearn.metrics import accuracy_score
from sklearn.preprocessing import StandardScaler

np.set_printoptions(suppress=True)
np.random.seed(42)


def loadDigitData():
    from sklearn.datasets import load_digits

    data = load_digits()
    inputs = data.images
    outputs = data['target']
    outputNames = data['target_names']

    # shuffle the original data
    noData = len(inputs)
    permutation = np.random.permutation(noData)
    inputs = inputs[permutation]
    inaux = []
    for img in inputs:
        l = []
        for row in img:
            l = l + list(row)
        inaux.append(l)
    inputs = inaux
    outputs = outputs[permutation]
    outaux = []
    for o in outputs:
        l = [0]*10
        l[o] = 1
        outaux.append(l)
    outputs = outaux

    return inputs, outputs, outputNames


inputs, outputs, outputNames = loadDigitData()

from utils import Utils

trainIn, trainOut, validIn, validOut = Utils.divide_train_validation(inputs, outputs)


feature_set = np.array([np.array(i) for i in trainIn])
output_set = np.array([np.array(i) for i in trainOut])

def linear(x, a=0.0000000001, b=0):
    return x*a+b

def linear_der(x):
    li = []
    for i in x:
        li2 = []
        for j in i:
            li2.append(1)
        li.append(np.array(li2))
    return np.array(li)

def relu(x):
    li = []
    for i in x:
        li2 = []
        for j in i:
            if j > 0:
                li2.append(j)
            else:
                li2.append(0)
        li.append(np.array(li2))
    return np.array(li)

def relu_der(x):
    return 1

def sigmoid(x):
    return 1/(1+np.exp(-x))

def sigmoid_der(x):
    return sigmoid(x) *(1-sigmoid (x))

def softmax(A):
    expA = np.exp(A)
    return expA / expA.sum(axis=1, keepdims=True)

def normalize(x):
    scaler = StandardScaler()
    return scaler.fit_transform(x)



instances = feature_set.shape[0]
attributes = feature_set.shape[1]
hidden_nodes1 = 32
hidden_nodes2 = 16
hidden_nodes3 = 8
output_labels = 10

weightsHiddenLayer1 = np.random.rand(attributes, hidden_nodes1)
biasHiddenLayer1 = np.random.randn(hidden_nodes1)

weightsHiddenLayer2 = np.random.rand(hidden_nodes1, hidden_nodes2)
biasHiddenLayer2 = np.random.randn(hidden_nodes2)

weightsHiddenLayer3 = np.random.rand(hidden_nodes2, hidden_nodes3)
biasHiddenLayer3 = np.random.randn(hidden_nodes3)

weightsOutputLayer = np.random.rand(hidden_nodes3, output_labels)
biasOutputLayer = np.random.randn(output_labels)
learningRate = 0.001

error_cost = []

for epoch in range(800):
    print(epoch)
    #--- Feedforward

    #----------- from input layer to hidden layer 1
    normalHidden1 = np.dot(feature_set, weightsHiddenLayer1) + biasHiddenLayer1
    activatedHidden1 = sigmoid(normalHidden1)

    #----------- from hidden layer1 to hidden layer 2
    normalHidden2 = np.dot(activatedHidden1, weightsHiddenLayer2) + biasHiddenLayer2
    activatedHidden2 = sigmoid(normalHidden2)

    #----------- from hidden layer3 to hidden layer 3
    normalHidden3 = np.dot(activatedHidden2, weightsHiddenLayer3) + biasHiddenLayer3
    activatedHidden3 = sigmoid(normalHidden3)

    #----------- from hidden layer to output layer
    normalOutput = np.dot(activatedHidden3, weightsOutputLayer) + biasOutputLayer
    activatedOutput = softmax(normalOutput)

    #--- Back Propagation

    # ----------- delta weights output layer
    dcost_dno = activatedOutput - output_set
    dno_dwo = activatedHidden3
    dcost_wo = np.dot(dno_dwo.T, dcost_dno)
    dcost_bo = dcost_dno

    # ----------- delta hidden layer3
    dno_dah3 = weightsOutputLayer
    dcost_dah3 = np.dot(dcost_dno, dno_dah3.T)
    dah3_dnh3 = sigmoid_der(normalHidden3)
    dnh3_dwh3 = activatedHidden2
    dcost_wh3 = np.dot(dnh3_dwh3.T, dah3_dnh3 * dcost_dah3)
    dcost_bh3 = dcost_dah3 * dah3_dnh3

    # ----------- delta hidden layer2
    dnh3_dah2 = weightsHiddenLayer3
    dcost_dah2 = np.dot(dcost_dah3, dnh3_dah2.T)
    dah2_dnh2 = sigmoid_der(normalHidden2)
    dnh2_dwh2 = activatedHidden1
    dcost_wh2 = np.dot(dnh2_dwh2.T, dah2_dnh2 * dcost_dah2)
    dcost_bh2 = dcost_dah2 * dah2_dnh2

    #----------- delta hidden layer 1

    dnh2_dah1 = weightsHiddenLayer2
    dcost_dah1 = np.dot(dcost_dah2, dnh2_dah1.T)
    dah1_dnh1 = sigmoid_der(normalHidden1)
    dnh1_dwh1 = feature_set
    dcost_wh1 = np.dot(dnh1_dwh1.T, dah1_dnh1 * dcost_dah1)

    dcost_bh1 = dcost_dah1 * dah1_dnh1


    #----------- update Weights

    weightsHiddenLayer1 -= learningRate * dcost_wh1
    biasHiddenLayer1 -= learningRate * dcost_bh1.sum(axis=0)

    weightsHiddenLayer2 -= learningRate * dcost_wh2
    biasHiddenLayer2 -= learningRate * dcost_bh2.sum(axis=0)

    weightsHiddenLayer3 -= learningRate * dcost_wh3
    biasHiddenLayer3 -= learningRate * dcost_bh3.sum(axis=0)

    weightsOutputLayer -= learningRate * dcost_wo
    biasOutputLayer -= learningRate * dcost_bo.sum(axis=0)

    loss = np.sum(-output_set * np.log(activatedOutput))
    error_cost.append(loss)
    # print(epoch, 'Loss function value: ', loss)

plt.plot(error_cost)
plt.show()

#--- validation phase
validation_set = np.array([np.array(i) for i in validIn])
validOut = np.array([np.array(i) for i in validOut])

#----------- from input layer to hidden layer 1
normalHidden1 = np.dot(validation_set, weightsHiddenLayer1) + biasHiddenLayer1
activatedHidden1 = sigmoid(normalHidden1)

#----------- from hidden layer1 to hidden layer 2
normalHidden2 = np.dot(activatedHidden1, weightsHiddenLayer2) + biasHiddenLayer2
activatedHidden2 = sigmoid(normalHidden2)

#----------- from hidden layer3 to hidden layer 3
normalHidden3 = np.dot(activatedHidden2, weightsHiddenLayer3) + biasHiddenLayer3
activatedHidden3 = sigmoid(normalHidden3)

#----------- from hidden layer to output layer
normalOutput = np.dot(activatedHidden3, weightsOutputLayer) + biasOutputLayer
activatedOutput = softmax(normalOutput)

for i in range(len(validOut)):
    print("Actual: {}, Predicted: {}".format(validOut[i], activatedOutput[i]))

loss = np.sum(-validOut * np.log(activatedOutput))
print('Loss function value: ', loss)

validOut = np.argmax(validOut, axis=1)
activatedOutput = np.argmax(activatedOutput, axis=1)

acc = accuracy_score(validOut, activatedOutput)
print("Accuracy: ", acc*100, "%")