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
    private String category;
    private Double repositionFee;
    private String state;
    private Long quantity;
    private Double dailyRate;
    private String image;
}