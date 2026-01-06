package com.example.ms_loans_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolsModel {

    private Long toolId;
    private String toolName;
    private String initialState;
    private String disponibility;
    private String category;
    private Long loanFee;
    private Long repositionFee;
    private Long diaryFineFee;
    private Long stock;
    private Long loanCount;
    private Long lowDmgFee;
}