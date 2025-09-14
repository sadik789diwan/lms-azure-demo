package com.hasan.lms_azure_demo.controller;

import com.hasan.lms_azure_demo.entity.PolicyHolder;
import com.hasan.lms_azure_demo.repository.PolicyHolderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/policyholders")
public class PolicyHolderController {

    private final PolicyHolderRepository policyHolderRepository;

    public PolicyHolderController(PolicyHolderRepository policyHolderRepository) {
        this.policyHolderRepository = policyHolderRepository;
    }

    // Fetch all holders WITHOUT initializing policies
    @GetMapping("/all")
    public List<PolicyHolder> getAllPolicyHolders() {
        return policyHolderRepository.findAll();
    }

    // Fetch with explicit policies initialization (to avoid LazyInitializationException)
    @GetMapping("/all-with-policies")
    public List<PolicyHolder> getAllPolicyHoldersWithPolicies() {
        List<PolicyHolder> holders = policyHolderRepository.findAll();
       // holders.forEach(holder -> holder.getPolicies().size()); // forces initialization
        return holders;
    }
}
