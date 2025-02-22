#!/usr/bin/env python3

import os
print("Current working directory:", os.getcwd())

import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report, precision_score
from sklearn.impute import SimpleImputer
from sklearn.pipeline import Pipeline
import joblib  # For saving the model and preprocessor
import os  # For handling file paths

def deploy():
    print("Current working directory:", os.getcwd())
    # Load the data
    data = pd.read_csv(r"/home/mathushuthanans/Downloads/transaction_dataset.csv")

    # Define features and target
    X = data.drop("IsFraud", axis=1)
    y = data["IsFraud"]

    # Preprocess the data manually
    # Apply one-hot encoding to categorical features and scale the numerical ones
    numeric_features = ["AmountSpent", "Balance"]
    categorical_features = ["Country", "Gender", "Occupation", "TransactionType"]

    # Impute missing values and scale numeric features
    numeric_transformer = Pipeline(steps=[
        ('imputer', SimpleImputer(strategy='mean')),
        ('scaler', StandardScaler())
    ])

    # Apply one-hot encoding to categorical features
    categorical_transformer = Pipeline(steps=[
        ('imputer', SimpleImputer(strategy='most_frequent')),
        ('onehot', OneHotEncoder(handle_unknown='ignore'))
    ])

    # Combine both into a column transformer
    preprocessor = ColumnTransformer(
        transformers=[
            ('num', numeric_transformer, numeric_features),
            ('cat', categorical_transformer, categorical_features)
        ])

    # Preprocess the features
    X_processed = preprocessor.fit_transform(X)

    # Split the data into training and test sets
    X_train, X_test, y_train, y_test = train_test_split(X_processed, y, test_size=0.3, random_state=42)

    # Train the Logistic Regression model
    model =LogisticRegression()
    model.fit(X_train, y_train)

    # Save the model and preprocessor to a specific directory
    output_dir = "/home/mathushuthanans/Documents/FraudDetector/detector/"
    os.makedirs(output_dir, exist_ok=True)  # Create the directory if it doesn't exist
    joblib.dump(model, os.path.join(output_dir, 'fraud_detection_model.pkl'))
    joblib.dump(preprocessor, os.path.join(output_dir, 'fraud_detection_preprocessor.pkl'))

    # Make predictions on the test set
    y_pred = model.predict(X_test)

    # Calculate precision
    precision = precision_score(y_test, y_pred)

    # Print the precision score
    print(f'Precision: {precision}')

    # Optionally, print the full classification report
    print("\nFull classification report:")
    print(classification_report(y_test, y_pred))

if __name__ == "__main__":
    print("executing")
    deploy()