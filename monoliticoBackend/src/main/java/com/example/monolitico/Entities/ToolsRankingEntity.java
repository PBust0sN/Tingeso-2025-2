package com.example.monolitico.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "TOOLS_RANKING")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ToolsRankingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long toolRankingId;
    private Long report_id;
    private String toolName;
    private String category;
    private Long loanCount;
}