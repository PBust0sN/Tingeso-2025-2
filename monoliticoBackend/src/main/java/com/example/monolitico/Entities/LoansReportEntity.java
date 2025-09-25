package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Table(name = "LOANS_REPORT")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class LoansReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long loanReportId;
    private Long report_id;
    private Date deliveryDate;
    private Date returnDate;
    private String loanType;
    private Date date;
    private Long staffId;
    private Long clientId;
    private Long amount;
    private Long extraCharges;
}