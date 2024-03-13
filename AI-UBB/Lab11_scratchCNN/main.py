import numpy as np
from skimage import feature
from skimage.feature import hog
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from keras.models import Sequential
from keras.layers import Dense, Conv2D, MaxPooling2D, Flatten

from utils import Utils

print('Încărcați imaginile și etichetele asociate')
X, Y = Utils.read_data()
for i in range(len(X)):
    X[i] = np.array(X[i]).reshape(48, 48)

X = np.array(X)

print('Extrageti features din poze')
features = []
for image in X:
    features.append(hog(image, orientations=9, pixels_per_cell=(8, 8), cells_per_block=(2, 2), visualize=False))


print('Convertiți caracteristicile și etichetele în numpy arrays')
features = np.array(features)
features2 = []
for i in range(len(features)):
    features2.append(features[i].reshape(30, 30))
features = np.array(features2)
y = np.array(Y)


print('Impărțiți datele în set de antrenare și set de testare')
X_train, X_test, y_train, y_test = train_test_split(features, y, test_size=0.2, random_state=42)

# print('Reshape pentru a fi compatibil cu rețeaua neurală (adaugați o dimensiune pentru canale)')
# X_train = X_train.reshape(X_train.shape[0], 1, 59, 1)
# X_test = X_test.reshape(X_test.shape[0], 1, 59, 1)

print('Definiți modelul CNN')
model = Sequential()
model.add(Conv2D(128, kernel_size=(3, 3), activation='relu', input_shape=(30, 30, 1)))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Conv2D(64, kernel_size=(3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Flatten())
model.add(Dense(128, activation='relu'))
model.add(Dense(7, activation='softmax'))

print('Compilați și antrenați modelul')
model.compile(loss='sparse_categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
model.fit(X_train, y_train, epochs=10, batch_size=32, validation_data=(X_test, y_test))

print('Evaluate the model performance on the test set')
score = model.evaluate(X_test, y_test, verbose=0)
print('Accuracy on the test set:', score[1])
