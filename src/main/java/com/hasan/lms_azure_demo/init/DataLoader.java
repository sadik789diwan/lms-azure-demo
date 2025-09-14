package com.hasan.lms_azure_demo.init;

import com.hasan.lms_azure_demo.entity.*;
import com.hasan.lms_azure_demo.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final PolicyHolderRepository policyHolderRepository;
    private final InsurancePlanRepository insurancePlanRepository;
    private final AddressRepository addressRepository;
    private final ClaimRepository claimRepository;
    private final HospitalRepository hospitalRepository;

    @PostConstruct
    public void initData() {
       try {
           // ---------- Insurance Plans ----------
           InsurancePlan planA = new InsurancePlan();
           planA.setPlanName("Basic Health Cover");
           planA.setCoverageAmount(200000.0);
           planA.setPremium(2500.0);

           InsurancePlan planB = new InsurancePlan();
           planB.setPlanName("Premium Family Cover");
           planB.setCoverageAmount(1000000.0);
           planB.setPremium(7500.0);

           insurancePlanRepository.saveAll(Arrays.asList(planA, planB));

           // ---------- Hospitals ----------
           Hospital hospital1 = new Hospital();
           hospital1.setHospitalName("Apollo Hospital");
           hospital1.setCity("Pune");

           Hospital hospital2 = new Hospital();
           hospital2.setHospitalName("Fortis Hospital");
           hospital2.setCity("Mumbai");

           hospitalRepository.saveAll(Arrays.asList(hospital1, hospital2));

           // ---------- Address ----------
           Address address = new Address();
           address.setStreet("MG Road");
           address.setCity("Pune");
           address.setState("Maharashtra");
           address.setZipCode("411001");
           //addressRepository.save(address);

           // ---------- PolicyHolder ----------
           PolicyHolder holder = new PolicyHolder();
           holder.setFirstName("Rahul");
           holder.setLastName("Sharma");
           holder.setEmail("rahul.sharma@example.com");
           holder.setPhoneNumber("9876543210");
           holder.setGender(PolicyHolder.Gender.MALE);
           holder.setDateOfBirth(new Date());
           holder.setPolicyStartDate(LocalDate.now());
           holder.setMedicalHistory("Diabetic, Hypertension");
           holder.setInsurancePlan(planA);
           holder.setAddress(address);
           holder.setNetworkHospitals(List.of(hospital1, hospital2));

           // Audit Info
           Audit audit = new Audit();
           audit.setCreatedBy("system");
           audit.setCreatedAt(LocalDate.now().atStartOfDay());
           holder.setAudit(audit);

           policyHolderRepository.save(holder);

           // ---------- Claims ----------
           Claim claim1 = new Claim();
           claim1.setClaimNumber("CLM1001");
           claim1.setClaimAmount(50000.0);
           claim1.setStatus("APPROVED");
           claim1.setPolicyHolder(holder);

           Claim claim2 = new Claim();
           claim2.setClaimNumber("CLM1002");
           claim2.setClaimAmount(20000.0);
           claim2.setStatus("PENDING");
           claim2.setPolicyHolder(holder);

           claimRepository.saveAll(List.of(claim1, claim2));

           System.out.println("✅ Sample health insurance data loaded successfully!");
       } catch (Exception e) {
           e.printStackTrace();
           throw e;
       }
    }
}
