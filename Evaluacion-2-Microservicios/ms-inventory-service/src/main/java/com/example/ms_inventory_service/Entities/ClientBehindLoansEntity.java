package com.example.monolitico.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "CLIENT_BEHIND_LOANS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ClientBehindLoansEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long clientBehindLoansId;

    private Long loanReportId;
    private Long clientIdBehind;
}