package com.example.monolitico.Controller;

import com.example.monolitico.Entities.FeeEntity;
import com.example.monolitico.Service.FeeService;
import jakarta.annotation.security.PermitAll;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
@CrossOrigin("*")
public class FeeController {
    @Autowired
    FeeService feeService;

    @GetMapping("/")
    public ResponseEntity<List<FeeEntity>> getAllFees(){
        List<FeeEntity> feeEntities = feeService.getAllFees();
        return ResponseEntity.ok(feeEntities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeEntity> getFeeById(@PathVariable Long id){
        FeeEntity feeEntity = feeService.getFeeById(id);
        return ResponseEntity.ok(feeEntity);
    }

    @PostMapping("/")
    public ResponseEntity<FeeEntity> saveFee(@RequestBody FeeEntity feeEntity){
        FeeEntity newFee = feeService.saveFee(feeEntity);
        return ResponseEntity.ok(newFee);
    }

    @PutMapping("/")
    public  ResponseEntity<FeeEntity> updateFee(@RequestBody FeeEntity feeEntity){
        FeeEntity updateFee = feeService.updateFee(feeEntity);
        return ResponseEntity.ok(updateFee);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<FeeEntity> deleteFee(@PathVariable Long id) throws Exception{
        var isDeleted = feeService.deleteFee(id);
        return ResponseEntity.noContent().build();
    }
}
