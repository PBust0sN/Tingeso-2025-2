package com.example.ms_reports_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineModel {

    private Long fineId;
    private Long clientId;
    private Double amount;
    private LocalDate fineDate;
    private String description;
    private Boolean paid;
}