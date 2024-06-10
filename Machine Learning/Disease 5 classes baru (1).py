#!/usr/bin/env python
# coding: utf-8

# In[1]:


#Import Library
import os
import zipfile
import random
import shutil
import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from shutil import copyfile
from tensorflow.keras.applications.inception_v3 import InceptionV3
from tensorflow.keras.applications.inception_v3 import preprocess_input

import numpy as np
import matplotlib.pyplot as plt
from matplotlib.pyplot import imshow
import seaborn as sns
from sklearn.metrics import confusion_matrix, ConfusionMatrixDisplay
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score
from sklearn.metrics import classification_report


# In[2]:


import zipfile

local_zip = 'catdogSkinDisease.zip'
zip_ref = zipfile.ZipFile(local_zip, 'r')
zip_ref.extractall('/tmp/data')
zip_ref.close()


# In[3]:


os.listdir('/tmp/data')

#number of images of each classes in the training_set folder
print(f"There are {len(os.listdir('/tmp/data/train/flea_allergy'))} train set images of flea allergy.")
print(f"There are {len(os.listdir('/tmp/data/train/hotspot'))} train set images of hotspot.")
print(f"There are {len(os.listdir('/tmp/data/train/mange'))} train set images of mange.")
print(f"There are {len(os.listdir('/tmp/data/train/ringworm'))} train set images of ringworm.")
print(f"There are {len(os.listdir('/tmp/data/train/leprosy'))} train set images of leprosy.")
print()

#now return the list of the content in that validation_set folder
print(f"There are {len(os.listdir('/tmp/data/validation/flea_allergy'))} val set images of flea allergy.")
print(f"There are {len(os.listdir('/tmp/data/validation/hotspot'))} val set images of hotspot.")
print(f"There are {len(os.listdir('/tmp/data/validation/mange'))} val set images of mange.")
print(f"There are {len(os.listdir('/tmp/data/validation/ringworm'))} val set images of ringworm.")
print(f"There are {len(os.listdir('/tmp/data/validation/leprosy'))} val set images of leprosy.")
print()

#now return the list of the content in that testing_set folder
print(f"There are {len(os.listdir('/tmp/data/test/flea_allergy'))} test set images of flea allergy.")
print(f"There are {len(os.listdir('/tmp/data/test/hotspot'))} test set images of hotspot.")
print(f"There are {len(os.listdir('/tmp/data/test/mange'))} test set images of mange.")
print(f"There are {len(os.listdir('/tmp/data/test/ringworm'))} test set images of ringworm.")
print(f"There are {len(os.listdir('/tmp/data/test/leprosy'))} test set images of leprosy.")


# In[4]:


TRAINING_DIR = "/tmp/data/train"
VALIDATION_DIR = "/tmp/data/validation"
TESTING_DIR = "/tmp/data/test"

TRAINING_FLEA_DIR = "/tmp/data/train/flea_allergy"
VALIDATION_FLEA_DIR = "/tmp/data/validation/flea_allergy"
TEST_FLEA_DIR = "/tmp/data/test/flea_allergy"

TRAINING_HOTSPOT_DIR = "/tmp/data/train/hotspot"
VALIDATION_HOTSPOT_DIR = "/tmp/data/validation/hotspot"
TESTING_HOTSPOT_DIR = "/tmp/data/test/hotspot"

TRAINING_MANGE_DIR = "/tmp/data/train/mange"
VALIDATION_MANGE_DIR = "/tmp/data/validation/mange"
TESTING_MANGE_DIR = "/tmp/data/test/mange"

TRAINING_RINGWORM_DIR = "/tmp/data/train/ringworm"
VALIDATION_RINGWORM_DIR = "/tmp/data/validation/ringworm"
TESTING_RINGWORM_DIR = "/tmp/data/test/ringworm"

TRAINING_LEPROSY_DIR = "/tmp/data/train/leprosy"
VALIDATION_LEPROSY_DIR = "/tmp/data/validation/leprosy"
TESTING_LEPROSY_DIR = "/tmp/data/test/leprosy"


# In[5]:


def train_val_generators(TRAINING_DIR, VALIDATION_DIR):

  # Instantiate the ImageDataGenerator class (don't forget to set the rescale argument)
  train_datagen = ImageDataGenerator(rescale = 1.0/255.,
                                     fill_mode='nearest',
                                     rotation_range=45,
                                     width_shift_range=0.1,
                                     height_shift_range=0.1,
                                     shear_range=0.1,
                                     zoom_range=0.1,
                                     horizontal_flip=True,
                                     )

  # Pass in the appropriate arguments to the flow_from_directory method
  train_generator = train_datagen.flow_from_directory(directory=TRAINING_DIR,
                                                      batch_size=128,
                                                      class_mode='categorical',
                                                      target_size=(100, 100))

  # Instantiate the ImageDataGenerator class (don't forget to set the rescale argument)
  validation_datagen = ImageDataGenerator(rescale = 1.0 / 255. )

  # Pass in the appropriate arguments to the flow_from_directory method
  validation_generator = validation_datagen.flow_from_directory(directory=VALIDATION_DIR,
                                                          batch_size=128,
                                                          class_mode='categorical',
                                                          target_size=(100, 100))
  ### END CODE HERE
  return train_generator, validation_generator


# In[6]:


train_generator, validation_generator = train_val_generators(TRAINING_DIR, VALIDATION_DIR)


# In[7]:


def show_image_samples(gen):
  train_dict = train_generator.class_indices
  classes = list(train_dict.keys())
  images, labels = next(gen) #get a sample batch from generator
  plt.figure(figsize=(20,20))
  length = len(labels)
  if length < 10:
    r = length
  else:
    r = 10
  for i in range(r):
    plt.subplot(5, 5, i+1)
    image = images[i]
    plt.imshow(image)
    index = np.argmax(labels[i])
    class_name = classes[index]
    plt.title(class_name, color='black', fontsize=18)
    plt.axis('off')
  plt.show()


# In[8]:


show_image_samples(train_generator)


# In[9]:


### INCEPTIONV3

IMAGE_SIZE = [100, 100]
 
inception = InceptionV3(input_shape=IMAGE_SIZE + [3], weights='imagenet', include_top=False)
 
for layer in inception.layers:
    layer.trainable = False
model = tf.keras.models.Sequential([
    inception,
    tf.keras.layers.Flatten(),
    tf.keras.layers.Dense(1028, activation='relu'),
    tf.keras.layers.Dropout(rate=0.3),
    tf.keras.layers.Dense(5, activation='softmax')
])

model.compile(optimizer=tf.keras.optimizers.RMSprop(learning_rate=0.001),
                  loss='categorical_crossentropy',
                  metrics=['accuracy']) 

EPOCHS = 25

# Train the model
history = model.fit(
      train_generator,
      steps_per_epoch=50,  
      epochs=EPOCHS,
      verbose=1,
      validation_data = validation_generator,
      validation_steps=20)

loss = history.history['loss']
val_loss = history.history['val_loss']
accuracy = history.history['accuracy']
val_accuracy = history.history['val_accuracy']
epochs = range(1, len(loss) + 1)

# Plot loss
plt.plot(epochs, loss, label='Training Loss')
plt.plot(epochs, val_loss, label='Validation Loss')
plt.title('Training and Validation Loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()
plt.show()

# Plot accuracy
plt.plot(epochs, accuracy, label='Training Accuracy')
plt.plot(epochs, val_accuracy, label='Validation Accuracy')
plt.title('Training and Validation Accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.legend()
plt.show()

predictions = model.predict(validation_generator)
predicted_labels = np.argmax(predictions, axis=1)
true_labels = validation_generator.classes

#Confusion Matrix Inception V3
cm = confusion_matrix(true_labels, predicted_labels)

# Plot confusion matrix
plt.figure(figsize=(8, 6))
sns.heatmap(cm, annot=True, cmap='Blues', fmt='d',
            xticklabels=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy'], 
            yticklabels=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy'])
plt.xlabel('Predicted labels')
plt.ylabel('True labels')
plt.title('Confusion Matrix')
plt.show()

# Classification report
print(classification_report(true_labels, predicted_labels, target_names=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy']))


# In[10]:


# MODEL CNN

def create_model():
    model2 = tf.keras.models.Sequential([
        tf.keras.layers.Conv2D(16, kernel_size=(3,3), activation='relu', input_shape=(100, 100, 3)),
        tf.keras.layers.BatchNormalization(),
        tf.keras.layers.MaxPooling2D(2,2),
        tf.keras.layers.Conv2D(32, kernel_size=(3,3), activation='relu'),
        tf.keras.layers.BatchNormalization(),
        tf.keras.layers.MaxPooling2D(2,2),
        tf.keras.layers.Conv2D(64, kernel_size=(3,3), activation='relu'),
        tf.keras.layers.BatchNormalization(),
        tf.keras.layers.MaxPooling2D(2,2),
        tf.keras.layers.Conv2D(128, kernel_size=(3,3), activation='relu'),
        tf.keras.layers.BatchNormalization(),
        tf.keras.layers.GlobalAveragePooling2D(),  # Mengubah tensor 4D menjadi tensor 2D
        tf.keras.layers.Dense(128, activation='relu'),
        tf.keras.layers.Dropout(0.5),
        tf.keras.layers.Dense(32, activation='relu'),
        tf.keras.layers.Dense(5, activation='softmax')
    ])

    model2.compile(optimizer=tf.keras.optimizers.RMSprop(learning_rate=0.001),
                  loss='categorical_crossentropy',
                  metrics=['accuracy']) 

    return model2

# Verifikasi ukuran input
model2 = create_model()
model2.summary()

EPOCHS = 25

# Train the model
history2 = model2.fit(
      train_generator,
      steps_per_epoch=25,  
      epochs=EPOCHS,
      verbose=1,
      validation_data = validation_generator,
      validation_steps=25)

loss = history2.history['loss']
val_loss = history2.history['val_loss']
accuracy = history2.history['accuracy']
val_accuracy = history2.history['val_accuracy']
epochs = range(1, len(loss) + 1)

# Plot loss
plt.plot(epochs, loss, label='Training Loss')
plt.plot(epochs, val_loss, label='Validation Loss')
plt.title('Training and Validation Loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()
plt.show()

# Plot accuracy
plt.plot(epochs, accuracy, label='Training Accuracy')
plt.plot(epochs, val_accuracy, label='Validation Accuracy')
plt.title('Training and Validation Accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.legend()
plt.show()

predictions = model2.predict(validation_generator)
predicted_labels = np.argmax(predictions, axis=1)
true_labels = validation_generator.classes

#Confusion Matrix Inception V3
cm = confusion_matrix(true_labels, predicted_labels)

# Plot confusion matrix
plt.figure(figsize=(8, 6))
sns.heatmap(cm, annot=True, cmap='Blues', fmt='d',
            xticklabels=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy'], 
            yticklabels=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy'])
plt.xlabel('Predicted labels')
plt.ylabel('True labels')
plt.title('Confusion Matrix')
plt.show()

# Classification report
print(classification_report(true_labels, predicted_labels, target_names=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy']))


# In[12]:


## MODEL VGG16

from tensorflow.keras.applications.vgg16 import VGG16
from tensorflow.keras.applications.vgg16 import preprocess_input

## Loading VGG16 model
base_model = VGG16(weights="imagenet", include_top=False, input_shape=(100, 100, 3))
base_model.trainable = False ## Not trainable weights

## Preprocessing input
#train_ds = preprocess_input(train_generator) 
#test_ds = preprocess_input(validation_generator)

from tensorflow.keras import layers, models

flatten_layer = layers.Flatten()
dense_layer_1 = layers.Dense(50, activation='relu')
dense_layer_2 = layers.Dense(20, activation='relu')
prediction_layer = layers.Dense(5, activation='softmax')


model3 = models.Sequential([
    base_model,
    flatten_layer,
    dense_layer_1,
    dense_layer_2,
    prediction_layer
])

model3.compile(
    optimizer='adam',
    loss='categorical_crossentropy',
    metrics=['accuracy'],
)

EPOCHS = 25

# Train the model
history = model3.fit(
      train_generator,
      steps_per_epoch=25,  
      epochs=EPOCHS,
      verbose=1,
      validation_data = validation_generator,
      validation_steps=25)

loss = history.history['loss']
val_loss = history.history['val_loss']
accuracy = history.history['accuracy']
val_accuracy = history.history['val_accuracy']
epochs = range(1, len(loss) + 1)

# Plot loss
plt.plot(epochs, loss, label='Training Loss')
plt.plot(epochs, val_loss, label='Validation Loss')
plt.title('Training and Validation Loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()
plt.show()

# Plot accuracy
plt.plot(epochs, accuracy, label='Training Accuracy')
plt.plot(epochs, val_accuracy, label='Validation Accuracy')
plt.title('Training and Validation Accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.legend()
plt.show()

predictions = model3.predict(validation_generator)
predicted_labels = np.argmax(predictions, axis=1)
true_labels = validation_generator.classes

#Confusion Matrix Inception V3
cm = confusion_matrix(true_labels, predicted_labels)

# Plot confusion matrix
plt.figure(figsize=(8, 6))
sns.heatmap(cm, annot=True, cmap='Blues', fmt='d',
            xticklabels=['flea allergy', 'hotspot', 'mange' 'ringworm', 'leprosy'], 
            yticklabels=['flea allergy', 'hotspot', 'mange' 'ringworm', 'leprosy'])
plt.xlabel('Predicted labels')
plt.ylabel('True labels')
plt.title('Confusion Matrix')
plt.show()


# Classification report
print(classification_report(true_labels, predicted_labels, target_names=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy']))


# In[13]:


## MODEL VGG19

from tensorflow.keras.applications.vgg19 import VGG19
from tensorflow.keras.applications.vgg19 import preprocess_input

## Loading VGG19 model
base_model = VGG19(weights="imagenet", include_top=False, input_shape=(100, 100, 3))
base_model.trainable = False ## Not trainable weights

## Preprocessing input
# train_ds = preprocess_input(train_generator) 
# test_ds = preprocess_input(validation_generator)

from tensorflow.keras import layers, models

flatten_layer = layers.Flatten()
dense_layer_1 = layers.Dense(50, activation='relu')
dense_layer_2 = layers.Dense(20, activation='relu')
prediction_layer = layers.Dense(5, activation='softmax')

model4 = models.Sequential([
    base_model,
    flatten_layer,
    dense_layer_1,
    dense_layer_2,
    prediction_layer
])

model4.compile(
    optimizer='adam',
    loss='categorical_crossentropy',
    metrics=['accuracy'],
)

EPOCHS = 25

# Train the model
history = model4.fit(
    train_generator,
    steps_per_epoch=25,  
    epochs=EPOCHS,
    verbose=1,
    validation_data=validation_generator,
    validation_steps=25
)

loss = history.history['loss']
val_loss = history.history['val_loss']
accuracy = history.history['accuracy']
val_accuracy = history.history['val_accuracy']
epochs = range(1, len(loss) + 1)

# Plot loss
import matplotlib.pyplot as plt

plt.plot(epochs, loss, label='Training Loss')
plt.plot(epochs, val_loss, label='Validation Loss')
plt.title('Training and Validation Loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()
plt.show()

# Plot accuracy
plt.plot(epochs, accuracy, label='Training Accuracy')
plt.plot(epochs, val_accuracy, label='Validation Accuracy')
plt.title('Training and Validation Accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.legend()
plt.show()

# Predictions and evaluation
import numpy as np
from sklearn.metrics import confusion_matrix, classification_report
import seaborn as sns

predictions = model3.predict(validation_generator)
predicted_labels = np.argmax(predictions, axis=1)
true_labels = validation_generator.classes

# Confusion Matrix
cm = confusion_matrix(true_labels, predicted_labels)

# Plot confusion matrix
plt.figure(figsize=(8, 6))
sns.heatmap(cm, annot=True, cmap='Blues', fmt='d',
            xticklabels=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy'], 
            yticklabels=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy'])
plt.xlabel('Predicted labels')
plt.ylabel('True labels')
plt.title('Confusion Matrix')
plt.show()

# Classification report
print(classification_report(true_labels, predicted_labels, target_names=['flea allergy', 'hotspot', 'mange', 'ringworm', 'leprosy']))

