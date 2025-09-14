package com.hasan.lms_azure_demo.repository;

import com.hasan.lms_azure_demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {}
