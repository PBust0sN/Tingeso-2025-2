package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Table(name = "FINE_REPORT")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class FineReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineReportId;
    private Long report_id;
    private Long amount;
    private String type;
    private Long clientId;
    private Long loanId;
    private String state;
    private Date date;
}