package com.hasan.lms_azure_demo.repository;

import com.hasan.lms_azure_demo.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {}
