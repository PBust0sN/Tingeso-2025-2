package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "LOANS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class LoansEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long loanId;

    private Date deliveryDate;
    private Date returnDate;
    private String loanType;
    private Date date;
    private Long staffId;
    private Long clientId;
    private Long amount;
    private Long extraCharges;
}
