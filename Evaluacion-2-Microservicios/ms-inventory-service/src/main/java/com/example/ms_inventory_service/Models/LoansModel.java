package com.example.ms_inventory_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoansModel {

    private Long loanId;
    private LocalDate deliveryDate;
    private LocalDate returnDate;
    private String loanType;
    private LocalDate date;
    private Long staffId;
    private Long clientId;
    private Long amount;
    private Long extraCharges;
    private Boolean active;
}