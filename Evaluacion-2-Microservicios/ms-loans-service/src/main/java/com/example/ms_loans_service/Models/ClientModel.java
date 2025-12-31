package com.example.ms_loans_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientModel {

    private Long client_id;
    private String name;
    private String mail;
    private String password;
    private String rut;
    private LocalDate createdDate;
    private String phone;
    private String role;
    private Boolean active;
}