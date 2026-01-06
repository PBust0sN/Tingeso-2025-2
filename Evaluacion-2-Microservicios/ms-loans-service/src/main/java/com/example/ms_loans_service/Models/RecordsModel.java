package com.example.ms_loans_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordsModel {

    private Long recordId;
    private Date recordDate;
    private String recordType;
    private Long recordAmount;
    private Long clientId;
    private Long loanId;
    private Long toolId;
}