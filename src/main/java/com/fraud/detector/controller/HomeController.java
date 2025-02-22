package com.fraud.detector.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fraud.detector.service.FraudService;
import com.fraud.detector.service.PythonExec;
import com.fraud.detector.service.TransactionGenerator;

@Controller
public class HomeController {
    private final FraudService fraudService;
    private final TransactionGenerator tg;
    private List<String> values = new ArrayList<>();

    public HomeController(FraudService fraudService, TransactionGenerator tg) {
        this.fraudService = fraudService;
        this.tg = tg;
    }


    @RequestMapping("/1")
    public String displayLiveGraph(Model model){
        System.out.println(fraudService.getFraudData());
        model.addAttribute("fraudData", fraudService.getFraudData());
        return "fraud";
    }

    @RequestMapping("/2")
    public String addFraudEntry() {
        List<Integer> fraudCount = Arrays.asList(23, 45, 83, 98, 9, 34, 56, 72, 11, 67);
        for (Integer i : fraudCount) {
            // fraudService.addFraudEntry(i);
        }
        return "display";
    }

    @RequestMapping("/3")
    @ResponseBody
    public void callPython(){
        String scriptPath = "/home/mathushuthanans/Documents/FraudDetector/detector/src/main/java/com/fraud/detector/pythonFiles/prediction.py";
        String modelPath = "/home/mathushuthanans/Documents/FraudDetector/detector/fraud_detection_model.pkl";

        while (true){
            String input = tg.generateTransaction(); 
            LocalDateTime dateTime = LocalDateTime.now();
            
            PythonExec executor = new PythonExec(scriptPath);
            String v = executor.executePythonScript(modelPath, input);
            System.out.println(v);
            fraudService.addFraudEntry(dateTime, Integer.parseInt(v));
        }


    }
}