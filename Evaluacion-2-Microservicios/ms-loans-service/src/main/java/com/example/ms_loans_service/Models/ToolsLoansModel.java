package com.example.ms_loans_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolsLoansModel {

    private Long toolLoanId;
    private Long loanId;
    private Long toolId;
    private Long quantity;
}