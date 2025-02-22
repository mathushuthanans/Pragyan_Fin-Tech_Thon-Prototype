#!/usr/bin/env python3

import sys
import joblib
import pandas as pd

def predict_new_input(model_path, input_string):
    model = joblib.load(model_path)
    preprocessor_path = model_path.replace("_model.pkl", "_preprocessor.pkl")
    preprocessor = joblib.load(preprocessor_path)

    input_values = input_string.split(", ")
    input_dict = {
        "Country": input_values[0],
        "Gender": input_values[1],
        "Occupation": input_values[2],
        "AmountSpent": float(input_values[3]),
        "Balance": float(input_values[4]),
        "TransactionType": input_values[5],
        "IsFraud": int(input_values[6])
    }

    new_input_data = pd.DataFrame([input_dict])
    new_input_data = new_input_data.drop(columns=["IsFraud"])
    new_input_processed = preprocessor.transform(new_input_data)
    prediction = model.predict(new_input_processed)

    return prediction[0]  # Return only the single prediction value

if __name__ == "__main__":
    if len(sys.argv) != 3:
        sys.exit(1)

    model_path = sys.argv[1]
    input_string = sys.argv[2]

    prediction = predict_new_input(model_path, input_string)
    print(prediction)  # Print only the prediction value