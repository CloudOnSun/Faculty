import csv
import numpy as np
import torch
from sklearn import linear_model

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
from transformers import BertTokenizer, BertModel

model_name = 'bert-base-uncased'
tokenizer = BertTokenizer.from_pretrained(model_name)
model = BertModel.from_pretrained(model_name)

input_ids = []
attention_masks = []
for text in inputs:
    encoded_dict = tokenizer.encode_plus(text,
                                          add_special_tokens=True,
                                          padding='max_length',
                                          max_length=270,
                                          return_attention_mask=True,
                                          return_tensors='pt')
    input_ids.append(encoded_dict['input_ids'])
    attention_masks.append(encoded_dict['attention_mask'])

input_ids = torch.cat(input_ids, dim=0)
attention_masks = torch.cat(attention_masks, dim=0)

print("extract features...")

with torch.no_grad():
    outputs = model(input_ids, attention_mask=attention_masks)
    features = outputs.last_hidden_state[:, 0, :]

print("reshape features")

reshaped_features = features.cpu().numpy()
reshaped_features = np.reshape(reshaped_features, (reshaped_features.shape[0], -1))


from sklearn.preprocessing import StandardScaler

scaler = StandardScaler()
normalized_features = scaler.fit_transform(reshaped_features)

trainFeatures = [normalized_features[i] for i in trainSample]
testFeatures = [normalized_features[i] for i in testSample]

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


