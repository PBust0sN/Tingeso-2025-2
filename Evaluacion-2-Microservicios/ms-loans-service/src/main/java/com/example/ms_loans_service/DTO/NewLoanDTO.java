package com.example.ms_loans_service.DTO;

import lombok.Data;

import java.util.List;

@Data
public class NewLoanDTO {
    private Long staff_id;
    private Long client_id;
    private List<Long> tools_id;
    private Long days;
}
