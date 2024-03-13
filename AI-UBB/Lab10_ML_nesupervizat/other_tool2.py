import csv

from sklearn.decomposition import TruncatedSVD
from sklearn.feature_extraction.text import TfidfVectorizer
from nltk.tokenize import RegexpTokenizer


def extract_features_LSI(train_inputs, test_inputs):
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



data = []
print("Citeste date")
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

print("imparte train test")
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

print("extrage features")

trainFeatures, testFeatures = extract_features_LSI(trainInputs, testInputs)

print("train model")

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
