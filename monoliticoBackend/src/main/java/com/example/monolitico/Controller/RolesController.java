package com.example.monolitico.Controller;

import com.example.monolitico.Entities.RolesEntity;
import com.example.monolitico.Service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin("*")
public class RolesController {
    @Autowired
    RolesService rolesService;

    @GetMapping("/")
    public ResponseEntity<List<RolesEntity>> getAllRoles(){
        List<RolesEntity> roles = rolesService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesEntity> getRoleById(@PathVariable Long id){
        RolesEntity rolesEntity = rolesService.getRolesById(id);
        return ResponseEntity.ok(rolesEntity);
    }

    @PostMapping("/")
    public ResponseEntity<RolesEntity> saveRole(@RequestBody RolesEntity rolesEntity){
        RolesEntity newRole = rolesService.saveRole(rolesEntity);
        return ResponseEntity.ok(newRole);
    }

    @PutMapping("/")
    public ResponseEntity<RolesEntity> updateRole(@RequestBody RolesEntity rolesEntity){
        RolesEntity updateRole = rolesService.updateRole(rolesEntity);
        return ResponseEntity.ok(updateRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RolesEntity> deleteRole(@PathVariable Long id)throws Exception{
        var isDeleted = rolesService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
