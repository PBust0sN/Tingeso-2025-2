package com.example.ms_reports_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientModel {

    private Long client_id;
    private String rut;
    private String name;
    private String last_name;
    private String mail;
    private String phone_number;
    private String state;
    private String password;
    private String role;
}