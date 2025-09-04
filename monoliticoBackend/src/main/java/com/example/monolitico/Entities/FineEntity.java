package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
