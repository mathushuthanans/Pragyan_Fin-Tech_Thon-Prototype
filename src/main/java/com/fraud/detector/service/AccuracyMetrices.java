package com.fraud.detector.service;

public class AccuracyMetrices {

    // Calculate precision, recall, and F1 score for binary classification.
    public Metrics calc(){
        // Example true labels and predicted labels
        int[] trueLabels = {1, 0, 1, 1, 0, 0, 1, 0, 0, 1};
        int[] predictedLabels = {1, 0, 0, 1, 0, 1, 1, 0, 0, 1};

        // Compute metrics
        Metrics metrics = computeMetrics(trueLabels, predictedLabels);

        // Print the results
        System.out.printf("Precision: %.4f\n", metrics.precision);
        System.out.printf("Recall: %.4f\n", metrics.recall);
        System.out.printf("F1 Score: %.4f\n", metrics.f1Score);
        return metrics;
    }

    // Class to hold metrics
    static class Metrics {
        double precision;
        double recall;
        double f1Score;

        Metrics(double precision, double recall, double f1Score) {
            this.precision = precision;
            this.recall = recall;
            this.f1Score = f1Score;
        }
    }

    // Method to compute metrics from arrays of true and predicted labels
    public static Metrics computeMetrics(int[] trueLabels, int[] predictedLabels) {
        if (trueLabels.length != predictedLabels.length) {
            throw new IllegalArgumentException("Length of true labels and predicted labels must be equal");
        }

        int truePositive = 0;
        int falsePositive = 0;
        int falseNegative = 0;

        for (int i = 0; i < trueLabels.length; i++) {
            int trueLabel = trueLabels[i];
            int predictedLabel = predictedLabels[i];

            if (predictedLabel == 1) {
                if (trueLabel == 1) {
                    truePositive++;
                } else {
                    falsePositive++;
                }
            } else {
                if (trueLabel == 1) {
                    falseNegative++;
                }
            }
        }

        double precision = (truePositive + falsePositive) > 0 ? (double) truePositive / (truePositive + falsePositive) : 0;
        double recall = (truePositive + falseNegative) > 0 ? (double) truePositive / (truePositive + falseNegative) : 0;
        double f1Score = (precision + recall) > 0 ? 2 * precision * recall / (precision + recall) : 0;

        return new Metrics(precision, recall, f1Score);
    }
}