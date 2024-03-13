import os
import cv2
import numpy as np
import keras
# Define the data directory, image dimensions, and train/test split ratio
from keras import Sequential
from keras.layers import Flatten, Dense
from sklearn.model_selection import train_test_split

IMG_WIDTH = 128
IMG_HEIGHT = 128
TEST_RATIO = 0.2

# Load the dataset
X = []
y = []
for root, dirs, files in os.walk('datasets/wsepia/Pins'):
    for file in files:
        if file.endswith('.jpg'):
            img_path = os.path.join(root, file)
            img = cv2.imread(img_path)
            img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
            X.append(img.flatten())
            y.append(0)

for root, dirs, files in os.walk('datasets/wsepia/PinsSepia'):
    for file in files:
        if file.endswith('.jpg'):
            img_path = os.path.join(root, file)
            img = cv2.imread(img_path)
            img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
            X.append(img.flatten())
            y.append(1)

# Convert the data to numpy arrays
X = np.array(X)
y = np.array(y)

# Split the data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=TEST_RATIO)

# Build the neural network model
model = Sequential()
model.add(Dense(64, input_dim=IMG_WIDTH*IMG_HEIGHT*3, activation='relu'))
model.add(Dense(32, activation='relu'))
model.add(Dense(1, activation='sigmoid'))

# Compile the model
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# Train the model
model.fit(X_train, y_train, epochs=50, batch_size=32, validation_data=(X_test, y_test))

# Evaluate the model
loss, accuracy = model.evaluate(X_test, y_test)
print('Test accuracy:', accuracy)