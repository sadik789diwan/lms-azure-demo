package com.hasan.lms_azure_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "policy_holders",
        uniqueConstraints = {
                // Unique constraint: email + phone_number must be unique together
                @UniqueConstraint(columnNames = {"email", "phone_number"})
        },
        indexes = {
                // Create an index on first_name + last_name for faster searching
                @Index(name = "idx_policyholder_name", columnList = "first_name,last_name")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyHolder {

    // ---------------- PRIMARY KEY ----------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment in DB
    private Long id;

    // ---------------- BASIC INFO ----------------
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    // email must be unique
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    // Enum stored as STRING ("MALE", "FEMALE", "OTHER")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    // Java.util.Date requires @Temporal to specify DATE/TIME
    @Temporal(TemporalType.DATE)
    @Column(name = "dob")
    private Date dateOfBirth;

    // LocalDate is supported natively from JPA 2.2 onwards
    private LocalDate policyStartDate;

    // Stores large text or CLOB in DB
    @Lob
    private String medicalHistory;

    // Not persisted in DB (calculated in runtime)
    @Transient
    private int calculatedAge;

    // ---------------- RELATIONSHIPS ----------------

    // Many PolicyHolders belong to one InsurancePlan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", foreignKey = @ForeignKey(name = "FK_POLICYHOLDER_PLAN"))
    private InsurancePlan insurancePlan;

    // One PolicyHolder has one Address
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    // One PolicyHolder has many Claims
    @OneToMany(mappedBy = "policyHolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;

    // Many PolicyHolders can go to many Hospitals (network hospitals)
    @ManyToMany
    @JoinTable(
            name = "policyholder_hospitals",
            joinColumns = @JoinColumn(name = "policyholder_id"),
            inverseJoinColumns = @JoinColumn(name = "hospital_id")
    )
    private List<Hospital> networkHospitals;

    // ---------------- EMBEDDED (Reusable fields) ----------------
    // Fields of Audit (like createdBy, createdAt) will be stored as columns
    // inside the "policy_holders" table itself.
    //@Embedded is used in JPA/Hibernate to include the fields of another class directly into this entity’s table, instead of making a separate table.
    @Embedded
    private Audit audit;

    // ---------------- ENUM ----------------
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
