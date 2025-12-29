package com.example.ms_reports_service.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "TOOLS_REPORT")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ToolsReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long toolIdReport;

    private String toolName;
    private String category;
    private Long loanCount;
}
