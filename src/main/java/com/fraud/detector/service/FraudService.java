package com.fraud.detector.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class FraudService {

    private final List<Map<String, Object>> fraudData = Collections.synchronizedList(new ArrayList<>());
    private int cumulativeFraudCount = 0; // Track cumulative count

    public List<Map<String, Object>> getFraudData() {
        return new ArrayList<>(fraudData); // Return a copy to avoid concurrency issues
    }

    public synchronized void addFraudEntry(LocalDateTime dateTime, Integer fraudCount) {
        cumulativeFraudCount += fraudCount; // Increment cumulative count

        Map<String, Object> entry = new HashMap<>();
        entry.put("time", dateTime.toString());
        entry.put("count", cumulativeFraudCount); // Use cumulative count
        fraudData.add(entry);

        if (fraudData.size() > 10) {
            fraudData.remove(0);
        }
    }
}