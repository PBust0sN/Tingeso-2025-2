package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ToolsRankingEntity;
import com.example.monolitico.Service.ToolsRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/toolsRanking")
@CrossOrigin("*")
public class ToolsRankingController {
    @Autowired
    private ToolsRankingService toolsRankingService;

    @GetMapping("/{id}")
    public ResponseEntity<List<ToolsRankingEntity>> getToolsRankingById(@PathVariable("id") Long id) {
        List<ToolsRankingEntity> toolsRankingEntities = toolsRankingService.getToolsRankingByReportId(id);
        return ResponseEntity.ok(toolsRankingEntities);
    }

    @GetMapping("/")
    public ResponseEntity<List<ToolsRankingEntity>> getAllToolsRanking(){
        List<ToolsRankingEntity>  toolsRankingEntities = toolsRankingService.getAllToolsRanking();
        return ResponseEntity.ok(toolsRankingEntities);
    }

    @PostMapping("/")
    public ResponseEntity<ToolsRankingEntity> createToolsRanking(@RequestBody ToolsRankingEntity toolsRankingEntity){
        ToolsRankingEntity newToolsRanking =  toolsRankingService.createToolsRanking(toolsRankingEntity);
        return ResponseEntity.ok(newToolsRanking);
    }

    @PutMapping("/")
    public ResponseEntity<ToolsRankingEntity> updatedToolsRanking(@RequestBody ToolsRankingEntity toolsRankingEntity){
        ToolsRankingEntity updatedToolsRanking = toolsRankingService.updateToolsRanking(toolsRankingEntity);
        return ResponseEntity.ok(updatedToolsRanking);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<ToolsRankingEntity> deleteToolsRanking(@PathVariable("id") Long id) throws Exception{
        var isDeleted =  toolsRankingService.deleteToolsRankingById(id);
        return ResponseEntity.noContent().build();
    }
}
