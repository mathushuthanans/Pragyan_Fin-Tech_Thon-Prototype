package com.fraud.detector.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fraud.detector.model.EntityData;
@Repository
public interface StoreTransactions extends JpaRepository<EntityData, Integer> {

}
