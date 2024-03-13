import csv

import numpy as np
from nltk import RegexpTokenizer
from sklearn import linear_model
from sklearn.decomposition import TruncatedSVD
from sklearn.feature_extraction.text import TfidfVectorizer


class KMeans:
    def __init__(self, n_clusters, max_iters=100):
        self.n_clusters = n_clusters
        self.max_iters = max_iters
        self.cluster_centers_ = None

    def fit(self, X):
        # Initialize cluster centers randomly
        np.random.seed(42)
        random_indices = np.random.choice(len(X), size=self.n_clusters, replace=False)
        self.cluster_centers_ = X[random_indices]

        for _ in range(self.max_iters):
            # Assign samples to the nearest cluster
            labels = self._assign_clusters(X)

            # Update cluster centers
            self._update_centers(X, labels)

    def predict(self, X):
        return self._assign_clusters(X)

    def _assign_clusters(self, X):
        labels = []
        for sample in X:
            distances = np.linalg.norm(sample - self.cluster_centers_, axis=1)
            cluster_label = np.argmin(distances)
            labels.append(cluster_label)
        return np.array(labels)

    def _update_centers(self, X, labels):
        for i in range(self.n_clusters):
            cluster_samples = X[labels == i]
            if len(cluster_samples) > 0:
                self.cluster_centers_[i] = np.mean(cluster_samples, axis=0)

class KMeansSemi:
    def __init__(self, n_clusters, max_iters=1000):
        self.n_clusters = n_clusters
        self.max_iters = max_iters
        self.cluster_centers_ = None

    def fit(self, X, labeled_indices):
        # Initialize cluster centers using labeled data
        self.cluster_centers_ = X[labeled_indices]

        for _ in range(self.max_iters):
            # Assign samples to the nearest cluster
            labels = self._assign_clusters(X)

            # Update cluster centers
            self._update_centers(X, labels)

    def predict(self, X):
        return self._assign_clusters(X)

    def _assign_clusters(self, X):
        labels = []
        for sample in X:
            distances = np.linalg.norm(sample - self.cluster_centers_, axis=1)
            cluster_label = np.argmin(distances)
            labels.append(cluster_label)
        return np.array(labels)

    def _update_centers(self, X, labels):
        for i in range(self.n_clusters):
            cluster_samples = X[labels == i]
            if len(cluster_samples) > 0:
                self.cluster_centers_[i] = np.mean(cluster_samples, axis=0)


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

def extract_features_SVD(train_inputs, test_inputs):
    tokenizer = RegexpTokenizer(r'\w+')

    tfidf = TfidfVectorizer(lowercase=True,
                            stop_words='english',
                            ngram_range=(1, 1),
                            tokenizer=tokenizer.tokenize)

    # Fit and Transform the documents
    train_data = tfidf.fit_transform(train_inputs)
    test_data = tfidf.fit_transform(test_inputs)

    lsa = TruncatedSVD(n_components=2, n_iter=100, random_state=42)

    train_feature = lsa.fit_transform(train_data)
    test_feature = lsa.fit_transform(test_data)

    return train_feature, test_feature


trainFeatures, testFeatures = extract_features_SVD(trainInputs, testInputs)


kmeans = KMeans(n_clusters=2)
kmeans.fit(trainFeatures)

computedTestIndexes = kmeans.predict(testFeatures)
computedTestOutputs1 = [labelNames[value] for value in computedTestIndexes]
computedTestOutputs2 = [labelNames[(value+1)%2] for value in computedTestIndexes]

from sklearn.metrics import accuracy_score

# just supposing that we have the true labels
ac1 = accuracy_score(testOutputs, computedTestOutputs1)
ac2 = accuracy_score(testOutputs, computedTestOutputs2)

print("acc nesupervizat (manual): ", max(ac1, ac2)*100, "%")


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

labels = [-1, -1]

for i in range(len(computedOutput)):
    if computedOutput[i] == 0:
        labels[0] = i
    else:
         labels[1] = i
    if labels[0] != -1 and labels [1] != -1:
        break

kmeans = KMeansSemi(n_clusters=2)
kmeans.fit(trainFeatures, labels)

computedTestIndexes = kmeans.predict(testFeatures)
computedTestOutputs1 = [labelNames[value] for value in computedTestIndexes]
computedTestOutputs2 = [labelNames[(value+1)%2] for value in computedTestIndexes]

from sklearn.metrics import accuracy_score

# just supposing that we have the true labels
ac1 = accuracy_score(testOutputs, computedTestOutputs1)
ac2 = accuracy_score(testOutputs, computedTestOutputs2)

print("acc hibrid (manual): ", max(ac1, ac2)*100, "%")

