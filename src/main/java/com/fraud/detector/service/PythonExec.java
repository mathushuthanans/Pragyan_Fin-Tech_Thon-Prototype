package com.fraud.detector.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonExec {
    private String pythonScriptPath;

    public PythonExec(String pythonScriptPath) {
        this.pythonScriptPath = pythonScriptPath;
    }

    public String executePythonScript(String modelPath, String inputString) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python3",
                    pythonScriptPath,
                    modelPath,
                    inputString
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine(); // Read only the first line

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script execution failed with exit code: " + exitCode);
            }

            if (line == null) {
                return ""; // Return an empty string if no output
            }
            return line; // Return the first line as a string
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error executing Python script", e);
        }
    }
}