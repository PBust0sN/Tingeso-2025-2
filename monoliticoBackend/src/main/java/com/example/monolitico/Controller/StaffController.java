package com.example.monolitico.Controller;

import com.example.monolitico.Entities.StaffEntity;
import com.example.monolitico.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin("*")
public class StaffController {
    @Autowired
    StaffService staffService;

    @GetMapping("/")
    public ResponseEntity<List<StaffEntity>> getAllStaff(){
        List<StaffEntity> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffEntity> getStaffById(@PathVariable Long id){
        StaffEntity staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    @PostMapping("/")
    public ResponseEntity<StaffEntity> saveStaff(@RequestBody StaffEntity staffEntity){
        StaffEntity newStaff = staffService.saveStaff(staffEntity);
        return ResponseEntity.ok(newStaff);
    }

    @PutMapping("/")
    public ResponseEntity<StaffEntity> updateStaff(@RequestBody StaffEntity staffEntity){
        StaffEntity updatedStaff = staffService.updateStaff(staffEntity);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StaffEntity> deleteStaff(@PathVariable Long id)throws Exception{
        var isDeleted = staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
