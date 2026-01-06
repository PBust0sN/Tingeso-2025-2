package com.example.ms_rates_service.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Table(name = "FINE")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class FineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;

    private Long amount;
    private String type;
    private Long clientId;
    private Long loanId;
    private String state;
    private Date date;
}
