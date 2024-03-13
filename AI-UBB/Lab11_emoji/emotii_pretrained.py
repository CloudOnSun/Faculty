import numpy as np

from utils import Utils
print("extract data")
trainIn, trainOut, testIn, testOut = Utils.read_data()

testIn = np.array(testIn)

for i in range(len(testIn)):
    testIn[i] = np.array(testIn[i])

testOut = np.array(testOut)

for i in range(len(testOut)):
    testOut[i] = np.array(testOut[i])

print("import pretrained model")

from EmoPy.src.fermodel import FERModel
from pkg_resources import resource_filename

target_emotions = ['anger', 'disgust', 'fear', 'happiness', 'sadness', 'surprise', 'calm']
model = FERModel(target_emotions, verbose=True)

emotion_index_map = {
            'anger': 0,
            'disgust': 1,
            'fear': 2,
            'happiness': 3,
            'sadness': 4,
            'surprise': 5,
            'calm': 6
        }
acc = 0
print("test pretrained model")
for i in range(len(testIn)-6500):
    print(i, "/", len(testIn)-6500)
    p = model.predict_from_ndarray(testIn[i])
    index = emotion_index_map[p]
    valid = np.argmax(testOut[i])
    if index == valid:
        acc += 1

print("Accuracy FerModel: ", acc*100/(len(testIn)-6500), "%")

