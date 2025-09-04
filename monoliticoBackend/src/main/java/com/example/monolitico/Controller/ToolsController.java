package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Service.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin("*")
public class ToolsController {
    @Autowired
    ToolsService toolsService;

    @GetMapping("/")
    public ResponseEntity<List<ToolsEntity>> getAllTools() {
        List<ToolsEntity> tools = toolsService.getAllTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolsEntity> getToolById(@PathVariable Long id) {
        ToolsEntity toolsEntity = toolsService.getToolsById(id);
        return ResponseEntity.ok(toolsEntity);
    }

    @PostMapping("/")
    public ResponseEntity<Optional<ToolsEntity>> saveTool(@RequestBody ToolsEntity toolsEntity) {
        Optional<ToolsEntity> newTool =  toolsService.saveTool(toolsEntity);
        return ResponseEntity.ok(newTool);
    }

    @PutMapping("/")
    public  ResponseEntity<ToolsEntity> updateTool(@RequestBody ToolsEntity toolsEntity) {
        ToolsEntity updateTool =  toolsService.updateTool(toolsEntity);
        return ResponseEntity.ok(updateTool);
    }

    @PutMapping("/dropdown")
    public ResponseEntity<Optional<ToolsEntity>> dropdownTool(@RequestParam Long requester_id, @RequestParam Long tool_id) {
        Optional<ToolsEntity> updatedTool = toolsService.dropDownATool(requester_id,tool_id);
        return ResponseEntity.ok(updatedTool);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ToolsEntity> deleteTool(@PathVariable Long id) throws Exception {
        var isDeleted = toolsService.deleteTools(id);
        return ResponseEntity.noContent().build();
    }
}
