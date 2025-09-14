package com.hasan.lms_azure_demo.repository;

import com.hasan.lms_azure_demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

// Core repositories
public interface PolicyHolderRepository extends JpaRepository<PolicyHolder, Long> {}

