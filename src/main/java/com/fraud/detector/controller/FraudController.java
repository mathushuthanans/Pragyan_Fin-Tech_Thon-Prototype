package com.fraud.detector.controller;

import com.fraud.detector.service.FraudService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fraud")
public class FraudController {

    private final FraudService fraudService;

    public FraudController(FraudService fraudService) {
        this.fraudService = fraudService;
    }

    // Get all fraud data (with time, cumulative count, zeros and ones)
    @GetMapping("/data")
    public List<Map<String, Object>> getFraudData() {
        return fraudService.getFraudData();
    }

    // Get precision and recall metrics
    @GetMapping("/metrics")
    public Map<String, Double> getMetrics() {
        return fraudService.getMetrics();
    }

    // Get model interpretability details
    @GetMapping("/interpretability")
    public Map<String, Object> getModelInterpretability() {
        return fraudService.getModelInterpretability();
    }

    // Add a fraud entry using current time; expects prediction and actual as parameters
    @PostMapping("/add")
    public String addFraudEntry(@RequestParam Integer prediction, @RequestParam Integer actual) {
        fraudService.addFraudEntry(LocalDateTime.now(), prediction, actual);
        return "Entry Added Successfully";
    }
}
