package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "CLIENT")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ClientBehindEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long clientIdBehind;

    private String rut;
    private String name;
    private String lastName;
    private String mail;
    private String phoneNumber;
    private String state;
    private Boolean avaliable;
}
