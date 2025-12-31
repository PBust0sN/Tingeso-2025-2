package com.example.ms_loans_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordsModel {

    private Long recordId;
    private Long toolId;
    private Long staffId;
    private String action;
    private LocalDate recordDate;
    private String description;
}