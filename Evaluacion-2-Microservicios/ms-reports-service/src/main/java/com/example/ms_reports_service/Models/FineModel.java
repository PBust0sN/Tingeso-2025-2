package com.example.ms_reports_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineModel {

    private Long fineId;
    private Long amount;
    private String type;
    private Long clientId;
    private Long loanId;
    private String state;
    private Date date;
}