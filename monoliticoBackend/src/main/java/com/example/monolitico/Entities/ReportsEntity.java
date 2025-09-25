package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Table(name = "REPORTS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ReportsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    private Date reportDate;
    private Long loan_id_report;
    private Long tools_id_ranking;
    private Long fine_id_reports;
    private Long client_id_behind;
}

