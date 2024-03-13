print("Extract emojis")
from emoji import emoji_data_python

import cv2
import numpy as np
import keras
# Define the data directory, image dimensions, and train/test split ratio
from keras import Sequential
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense
from sklearn.model_selection import train_test_split

X = []
y = []
IMG_WIDTH = 128
IMG_HEIGHT = 128
TEST_RATIO = 0.2
for e in emoji_data_python.emoji_data:
    if e.category == 'Smileys & Emotion':
        if e.subcategory == 'face-smiling' or e.subcategory == 'face-affection' or e.subcategory == 'face-tongue' or \
                e.subcategory == 'face-hand':
            if e.has_img_apple:
                img = cv2.imread('img-apple-160/' + e.image)
                img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
                X.append(img)
                y.append(0)
            if e.has_img_google:
                img = cv2.imread('img-google-136/' + e.image)
                img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
                X.append(img)
                y.append(0)
            if e.has_img_facebook:
                img = cv2.imread('img-facebook-96/' + e.image)
                img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
                X.append(img)
                y.append(0)
        elif e.subcategory == 'face-neutral-skeptical' or e.subcategory == 'face-sleepy' or \
                e.subcategory == 'face-unwell' or e.subcategory == 'face-concerned' or e.subcategory == 'face-negative':
            if e.has_img_apple:
                img = cv2.imread('img-apple-160/' + e.image)
                img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
                X.append(img)
                y.append(1)
            if e.has_img_google:
                img = cv2.imread('img-google-136/' + e.image)
                img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
                X.append(img)
                y.append(1)
            if e.has_img_facebook:
                img = cv2.imread('img-facebook-96/' + e.image)
                img = cv2.resize(img, (IMG_WIDTH, IMG_HEIGHT))
                X.append(img)
                y.append(1)

# Convert the data to numpy arrays
X = np.array(X)
y = np.array(y)

# Split the data into training and testing sets
print("Divide train test")
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=TEST_RATIO, random_state=42, shuffle=True)

# Build the neural network model
print("Build model")
model = Sequential()
model.add(Conv2D(16, (3, 3), activation='relu', input_shape=(IMG_WIDTH, IMG_HEIGHT, 3)))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Conv2D(16, (3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Conv2D(32, (3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Conv2D(64, (3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
#flattern the output into 1D array for further processing
model.add(Flatten())
model.add(Dense(64, activation='relu'))
model.add(Dense(1, activation='sigmoid'))


# Compile the model
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# Train the model
print("Train model")
model.fit(X_train, y_train, epochs=50, batch_size=32, validation_data=(X_test, y_test))

# Evaluate the model
print("Test model")
loss, accuracy = model.evaluate(X_test, y_test)
print('Test accuracy:', accuracy)
