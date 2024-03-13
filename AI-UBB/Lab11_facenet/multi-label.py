import os
import cv2
import numpy as np
import keras
from keras import Sequential
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from sklearn.model_selection import train_test_split
import tensorflow as tf

IMG_WIDTH = 48
IMG_HEIGHT = 48
TEST_RATIO = 0.2

print('Load the dataset')
X = []
y = []
j = 0

with open('fer2013new.csv', newline='') as file:
    for line in file:
        j += 1
        if j < 1000:
            line = line.replace('\n', '')
            d = []
            features = line.split(",")
            if len(features) == 12 and features[0] != 'Usage' and len(features[1]) > 0:
                img_path = ''
                if features[0] == 'Training':
                    img_path = 'datafer/FER2013Train/' + features[1]
                # if features[0] == 'PrivateTest':
                #     img_path = 'datafer/FER2013Test/' + features[1]
                # else:
                #     img_path = 'datafer/FER2013Valid/' + features[1]
                    img = cv2.imread(img_path)
                    img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
                    X.append(img)

                    feats = []
                    for i in range(2, len(features)):
                        feats.append(int(features[i])/10)
                    y.append(np.array(feats))

# Convert the data to numpy arrays
X = np.array(X)
y = np.array(y)

feature_model = tf.keras.applications.VGG16(weights='imagenet', include_top=False)
features=[]
i = 0
for image in X:
    print(i)
    i += 1
    preprocessed_image = tf.keras.applications.vgg16.preprocess_input(image)

    preprocessed_image = np.expand_dims(preprocessed_image, axis=0)

    feature = feature_model.predict(preprocessed_image, verbose=0)
    features.append(feature)

features = np.array(features)
copyf = []
for f in features:
    copyf.append(f[0][0][0])
features = np.array(copyf)
# Split the data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(features, y, test_size=TEST_RATIO, random_state=42)

# Build the neural network model
model = Sequential()
model.add(Dense(64, activation='relu'))
model.add(Dense(64, activation='relu'))
model.add(Dense(128, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(10, activation='softmax'))

# Compile the model
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# Train the model
model.fit(X_train, y_train, epochs=50, batch_size=32, validation_data=(X_test, y_test))

# Evaluate the model
loss, accuracy = model.evaluate(X_test, y_test)
print('Test accuracy:', accuracy)