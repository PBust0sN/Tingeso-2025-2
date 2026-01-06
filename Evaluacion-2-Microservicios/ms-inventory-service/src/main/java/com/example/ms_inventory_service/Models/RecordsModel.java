package com.example.ms_inventory_service.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecordsModel {

    private Long recordId;
    private Date recordDate;
    private String recordType;
    private Long recordAmount;
    private Long clientId;
    private Long loanId;
    private Long toolId;
}
