import numpy as np


class Conv:
    # The Convolution layer
    def __init__(self, num_filters, filter_size):
        self.num_filters = num_filters
        self.filter_size = filter_size
        self.conv_filter = np.random.randn(num_filters, filter_size, filter_size) / (filter_size * filter_size)

    def image_region(self, image):
        # Generator function
        height, width = image.shape
        self.image = image

        for i in range(height - self.filter_size + 1):
            for j in range(width - self.filter_size + 1):
                image_patch = image[i:(i + self.filter_size), j:(j + self.filter_size)]
                yield image_patch, i, j

    def forward_prop(self, image):
        # Convolution
        height, width = image.shape
        conv_out = np.zeros((height - self.filter_size + 1, width - self.filter_size + 1, self.num_filters))

        for image_patch, i, j in self.image_region(image):
            conv_out[i, j] = np.sum(image_patch * self.conv_filter, axis=(1, 2))

        return conv_out

    def backward_prop(self, d_L_d_out, learn_rate):
        d_L_d_filters = np.zeros(self.conv_filter.shape)

        for im_region, i, j in self.image_region(self.image):
            for f in range(self.num_filters):
                d_L_d_filters[f] += d_L_d_out[i, j, f] * im_region

        self.conv_filter -= learn_rate * d_L_d_filters

        return None


class Max_Pool:
    # The Max Pooling layer
    def __init__(self, filter_size):
        self.filter_size = filter_size

    def image_region(self, image):
        # Generator function
        new_height = image.shape[0] // self.filter_size
        new_width = image.shape[1] // self.filter_size
        self.image = image

        for i in range(new_height):
            for j in range(new_width):
                image_patch = image[(i * self.filter_size):(i * self.filter_size + self.filter_size),
                              (j * self.filter_size):(j * self.filter_size + self.filter_size)]
                yield image_patch, i, j

    def forward_prop(self, image):
        # Max pooling
        height, width, num_filters = image.shape
        output = np.zeros((height // self.filter_size, width // self.filter_size, num_filters))

        for image_patch, i, j in self.image_region(image):
            output[i, j] = np.amax(image_patch, axis=(0, 1))

        return output

    def backward_prop(self, d_L_d_out):
        d_L_d_input = np.zeros(self.last_input.shape)

        for image_region, i, j in self.iterate_regions(self.last_input):
            h, w, f = image_region.shape
            amax = np.amax(image_region, axis=(0, 1))

            for i2 in range(h):
                for j2 in range(w):
                    for f2 in range(f):
                        if image_region[i2, j2, f2] == amax[f2]:
                            d_L_d_input[i * 2 + i2, j * 2 + j2, f2] = d_L_d_out[i, j, f2]

        return d_L_d_input


class Flatten:
    def forward_prop(self, input):
        self.input_shape = input.shape
        return input.flatten()

    def backward_prop(self, output_gradient):
        return output_gradient.reshape(self.input_shape)


class Softmax:
    # The Softmax Layer
    def __init__(self, input_node, softmax_node):
        self.weight = np.random.randn(input_node, softmax_node) / input_node
        self.bias = np.zeros(softmax_node)

    def forward_prop(self, image):
        # Forward propagation
        input_node = image.shape[0] * image.shape[1] * image.shape[2]
        flattened = image.flatten()
        output_val = np.dot(flattened, self.weight) + self.bias
        output_val = np.exp(output_val)
        return output_val / np.sum(output_val, axis=0)


def cnn_forward_prop(image, label):
    # Forward Propagation through our CNN
    out_p = conv.forward_prop((image / 255) - 0.5)
    out_p = pool.forward_prop(out_p)
    out_p = softmax.forward_prop(out_p)

    cross_ent_loss = -np.log(out_p[label])
    accuracy_eval = 1 if np.argmax(out_p) == label else 0

    return out_p, cross_ent_loss, accuracy_eval


#
# conv = Conv(8, 3)
# pool = Max_Pool(2)
# softmax = Softmax(13 * 13 * 8, 7)

# ------------------------------------------------------------------------------------


import pandas as pd
import numpy as np


class FullyConnectedLayer:
    def __init__(self, input_size, output_size):
        self.weights = np.random.randn(input_size, output_size) * np.sqrt(2 / input_size)
        self.biases = np.zeros(output_size)

    def forward_prop(self, inputs):
        self.inputs = inputs
        return np.dot(inputs, self.weights) + self.biases

    def backward_prop(self, dvalues):
        self.dweights = np.dot(self.inputs.T, dvalues)
        self.dbiases = np.sum(dvalues, axis=0, keepdims=True)
        return np.dot(dvalues, self.weights.T)


class ReLU:
    def forward_prop(self, inputs):
        self.inputs = inputs
        return np.maximum(0, inputs)

    def backward_prop(self, dvalues):
        self.dvalues = dvalues.copy()
        self.dvalues[self.inputs <= 0] = 0
        return self.dvalues


class Softmax:
    def forward_prop(self, inputs):
        exp_values = np.exp(inputs - np.max(inputs, axis=1, keepdims=True))
        probabilities = exp_values / np.sum(exp_values, axis=1, keepdims=True)
        self.outputs = probabilities
        return probabilities

    def backward_prop(self, dvalues):
        self.dvalues = dvalues.copy()
        self.dvalues[np.arange(len(dvalues)), dvalues.argmax(axis=1)] -= 1
        return self.dvalues


# Loss function (Cross-Entropy)
def calculate_loss(y_true, y_pred):
    return -np.sum(y_true * np.log(y_pred))


# Accuracy function
def calculate_accuracy(y_true, y_pred):
    return np.sum(y_true == np.argmax(y_pred, axis=1)) / len(y_true)


# Creating the network
conv = Conv(8, 3)
pool = Max_Pool(2)
flat = Flatten()
fc = FullyConnectedLayer(13 * 13 * 8, 128)
relu = ReLU()
out = FullyConnectedLayer(128, 7)
softmax = Softmax()

# Training the network
df = pd.DataFrame()  # Assuming the dataframe has already been created and filled

# Converting the emotion labels to one-hot encoding
emotion_mapping = {'anger': 0, 'disgust': 1, 'fear': 2, 'happiness': 3, 'sadness': 4, 'surprise': 5, 'calm': 6}
df['emotion'] = df['emotion'].map(emotion_mapping)

y = pd.get_dummies(df['emotion']).values
X = df['pixels'].values

import numpy as np

from utils import Utils
print("extract data")
trainIn, trainOut, testIn, testOut = Utils.read_data()

for epoch in range(10):  # You can increase this value for more training
    for i in range(len(df)):
        image = np.array(X[i]).reshape(48, 48)
        label = y[i]

        # Forward pass
        out_p = conv.forward_prop((image / 255) - 0.5)
        out_p = pool.forward_prop(out_p)
        out_p = flat.forward_prop(out_p)
        out_p = fc.forward_prop(out_p)
        out_p = relu.forward_prop(out_p)
        out_p = out.forward_prop(out_p)
        probs = softmax.forward_prop(out_p)

        # Calculate loss and accuracy
        loss = calculate_loss(label, probs)
        accuracy = calculate_accuracy(np.argmax(label), probs)

        # Backward pass
        dvalues = softmax.backward_prop(label - probs)
        dvalues = out.backward_prop(dvalues)
        dvalues = relu.backward_prop(dvalues)
        dvalues = fc.backward_prop(dvalues)
        dvalues = flat.backward_prop(dvalues)
        dvalues = pool.backward_prop(dvalues)
        conv.backward_prop(dvalues)

        # Print loss and accuracy for the first example in this epoch
        if i == 0:
            print(f'Epoch: {epoch + 1}, loss: {loss:.3f}, accuracy: {accuracy:.3f}')

# Testing
X_test = ...  # Your test images
y_test = ...  # Your test labels

accuracy = 0
for i in range(len(X_test)):
    image = np.array(X_test[i]).reshape(48, 48)
    label = y_test[i]

    out_p = conv.forward_prop((image / 255) - 0.5)
    out_p = pool.forward_prop(out_p)
    out_p = flat.forward_prop(out_p)
    out_p = fc.forward_prop(out_p)
    out_p = relu.forward_prop(out_p)
    out_p = out.forward_prop(out_p)
    probs = softmax.forward_prop(out_p)

    accuracy += calculate_accuracy(np.argmax(label), probs)

print(f'Test accuracy: {accuracy / len(X_test):.3f}')
