package com.hasan.lms_azure_demo.repository;

import com.hasan.lms_azure_demo.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {}
