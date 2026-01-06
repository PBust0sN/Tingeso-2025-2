package com.example.ms_reports_service.Controller;

import com.example.ms_reports_service.Entities.ToolsLoanReportEntity;
import com.example.ms_reports_service.Entities.ToolsRankingEntity;
import com.example.ms_reports_service.Service.ToolsLoanReportService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/toolsloansreports")
@CrossOrigin("*")
public class ToolsLoanReportController {

    @Autowired
    private ToolsLoanReportService toolsLoanReportService;

    @GetMapping("/getools/{id}")
    public ResponseEntity<List<Long>> getByLoanId(@PathVariable Long id) {
        List<Long> tools = toolsLoanReportService.findByLoanId(id);
        return ResponseEntity.ok(tools);
    }

    @PostMapping("/")
    public ResponseEntity<ToolsLoanReportEntity> create(@RequestBody ToolsLoanReportEntity toolsLoanReportEntity) {
        ToolsLoanReportEntity newToolsLoanReport = toolsLoanReportService.createToolsLoanReport(toolsLoanReportEntity);
        return ResponseEntity.ok(newToolsLoanReport);
    }
}
