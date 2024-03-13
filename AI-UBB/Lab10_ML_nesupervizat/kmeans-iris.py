import numpy as np
from sklearn.preprocessing import StandardScaler

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

labelNames = ['Iris-setosa', 'Iris-versicolor', 'Iris-virginica']


u, t = readData()

trainIn, trainOut, validIn, validOut = divide_train_validation(u, t)

from sklearn.cluster import KMeans

unsupervisedClassifier = KMeans(n_clusters=3, random_state=42, n_init=5000, max_iter=3000)
unsupervisedClassifier.fit(trainIn)

original_labels = np.argmax(trainOut, axis=1)

kmeans_labels = unsupervisedClassifier.labels_
cluster_labels = np.unique(kmeans_labels)  # Unique cluster labels returned by k-means

mapping = {}  # Mapping dictionary to store the cluster-label mapping

for cluster_label in cluster_labels:
    cluster_indices = np.where(kmeans_labels == cluster_label)  # Indices of samples in the cluster
    labels_in_cluster = original_labels[cluster_indices]  # Original labels of samples in the cluster
    majority_label = np.bincount(labels_in_cluster).argmax()  # Find the most frequent label
    mapping[cluster_label] = majority_label


computedTestIndexes = unsupervisedClassifier.predict(validIn)
computedTestOutputs = [mapping[value] for value in computedTestIndexes]

from sklearn.metrics import accuracy_score

validOut = np.argmax(validOut, axis=1)
#computedTestOutputs = np.argmax(computedTestOutputs, axis=1)

acc1 = accuracy_score(validOut, computedTestOutputs)

print("acc: ", acc1 * 100, "%")
