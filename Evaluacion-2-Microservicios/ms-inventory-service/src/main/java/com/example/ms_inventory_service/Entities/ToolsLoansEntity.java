package com.example.ms_inventory_service.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "TOOLS_LOANS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ToolsLoansEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long toolId;
    private Long loanId;
}
