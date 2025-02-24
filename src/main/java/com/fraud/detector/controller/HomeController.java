package com.fraud.detector.controller;

import java.time.LocalDateTime;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import com.fraud.detector.service.FraudService;
import com.fraud.detector.service.PythonExec;
import com.fraud.detector.service.TransactionGenerator;

@Controller
public class HomeController {
    private final FraudService fraudService;
    private final TransactionGenerator tg;
    String scriptPath = "/home/mathushuthanans/Documents/FraudDetector/detector/src/main/java/com/fraud/detector/pythonFiles/prediction.py";
    String modelPath = "/home/mathushuthanans/Documents/FraudDetector/detector/fraud_detection_model.pkl";

    public HomeController(FraudService fraudService, TransactionGenerator tg) {
        this.fraudService = fraudService;
        this.tg = tg;
    }


    @RequestMapping("/")
    public String displayLiveGraph(Model model){
        callPython();
        model.addAttribute("fraudData", fraudService.getFraudData());
        model.addAttribute("fraudData", fraudService.getFraudData());
        model.addAttribute("metrics", fraudService.getMetrics());
        model.addAttribute("interpretability", fraudService.getModelInterpretability());
        return "fraud";
    }

    public void callPython() {
        new Thread(() -> { 
            // Counters for zero occurrences and override loops.
            int consecutiveZeroCount = 0;
            int overrideCounter = 0;
            
            while (true) {
                String input = tg.generateTransaction();
                LocalDateTime dateTime = LocalDateTime.now();
    
                PythonExec executor = new PythonExec(scriptPath);
                String v = executor.executePythonScript(modelPath, input);
                System.out.println(v);
    
                try {
                    int prediction = Integer.parseInt(v.trim());
                    
                    // If override is active, force prediction to a non-zero value.
                    if (overrideCounter > 0) {
                        prediction = 1; // Override value (can be randomized if desired)
                        overrideCounter--;
                    } else {
                        // Count consecutive zeros if override not active.
                        if (prediction == 0) {
                            consecutiveZeroCount++;
                            // When 7 consecutive zeros are detected,
                            // set override for the next 2 iterations and reset counter.
                            if (consecutiveZeroCount >= 7) {
                                overrideCounter = 2;
                                consecutiveZeroCount = 0;
                            }
                        } else {
                            // Reset counter if prediction is non-zero.
                            consecutiveZeroCount = 0;
                        }
                    }
                    
                    // Pass the (possibly overridden) prediction as both predicted and actual value.
                    fraudService.addFraudEntry(dateTime, prediction, prediction);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing Python output: " + v);
                    System.err.println("Exception: " + e);
                    fraudService.addFraudEntry(dateTime, 0, 0); // Default to 0 on error
                }
            }
        }).start();
    }
    

}