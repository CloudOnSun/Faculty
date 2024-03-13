import csv
import numpy as np
from sklearn import linear_model

data = []

fileName = 'review_mixed.csv'
with open(fileName) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    line_count = 0
    for row in csv_reader:
        if line_count == 0:
            dataNames = row
        else:
            data.append(row)
        line_count += 1

inputs = [data[i][0] for i in range(len(data))]
outputs = [data[i][1] for i in range(len(data))]

# indexes = np.random.permutation(len(inputs))
#
# inputs = [inputs[i] for i in indexes]
# outputs = [outputs[i] for i in indexes]


labelNames = list(set(outputs))

import numpy as np

np.random.seed(5)
# noSamples = inputs.shape[0]
noSamples = len(inputs)
indexes = [i for i in range(noSamples)]
trainSample = np.random.choice(indexes, int(0.8 * noSamples), replace = False)
testSample = [i for i in indexes  if not i in trainSample]

trainInputs = [inputs[i] for i in trainSample]
trainOutputs = [outputs[i] for i in trainSample]
testInputs = [inputs[i] for i in testSample]
testOutputs = [outputs[i] for i in testSample]

from sklearn.feature_extraction.text import CountVectorizer
vectorizer = CountVectorizer()

trainFeatures = vectorizer.fit_transform(trainInputs)
testFeatures = vectorizer.transform(testInputs)

# vocabbulary from the train data
print('vocab: ', vectorizer.get_feature_names_out()[:20])
# extracted features
print('features: ', trainFeatures.toarray()[:3][:20])

from sklearn.cluster import KMeans

unsupervisedClassifier = KMeans(n_clusters=2, random_state=42, n_init=1000)
unsupervisedClassifier.fit(trainFeatures)

computedTestIndexes = unsupervisedClassifier.predict(testFeatures)
computedTestOutputs1 = [labelNames[value] for value in computedTestIndexes]
computedTestOutputs2 = [labelNames[(value+1)%2] for value in computedTestIndexes]

from sklearn.metrics import accuracy_score

# just supposing that we have the true labels
ac1 = accuracy_score(testOutputs, computedTestOutputs1)
ac2 = accuracy_score(testOutputs, computedTestOutputs2)

print("acc nesupervizat: ", max(ac1, ac2)*100, "%")

classifier = linear_model.LogisticRegression(max_iter=1000)


trainOut = []
for i in trainOutputs:
    if i == 'negative':
        trainOut.append(0)
    else:
        trainOut.append(1)

validOut = []
for i in testOutputs:
    if i == 'negative':
        validOut.append(0)
    else:
        validOut.append(1)

classifier.fit(trainFeatures, trainOut)
computedOutput = classifier.predict(testFeatures)

acc=0

for i in range(len(computedOutput)):
    if computedOutput[i] == validOut[i]:
        acc += 1
print("Accuracy supervised:", acc * 100 / len(computedOutput), "%")