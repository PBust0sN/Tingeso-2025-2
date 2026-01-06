package com.example.ms_reports_service.Controller;

import com.example.ms_reports_service.Entities.ReportsEntity;
import com.example.ms_reports_service.Service.ReportsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin("*")
public class ReportsController {
    @Autowired
    ReportsServices reportsServices;

    @GetMapping("/")
    public ResponseEntity<List<ReportsEntity>> getAllReports(){
        List<ReportsEntity> reports = reportsServices.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportsEntity> getRecordById(@PathVariable Long id){
        ReportsEntity report = reportsServices.getReportsById(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/get-all-client/{id}")
    public ResponseEntity<List<ReportsEntity>> getAllClient(@PathVariable Long id){
        List<ReportsEntity> reports = reportsServices.getAllByClientId(id);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/")
    public ResponseEntity<ReportsEntity> saveRecord(@RequestBody ReportsEntity report){
        ReportsEntity newReport = reportsServices.saveReport(report);
        return ResponseEntity.ok(newReport);
    }

    @PutMapping("/")
    public ResponseEntity<ReportsEntity> updateRecord(@RequestBody ReportsEntity report){
        ReportsEntity newReport = reportsServices.updateReport(report);
        return ResponseEntity.ok(newReport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReportsEntity> deleteRecord(@PathVariable Long id) throws Exception{
        var isDeleted = reportsServices.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
