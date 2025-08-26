package com.example.monolitico.Service;

import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Repositories.ToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolsService {

    @Autowired
    ToolsRepository toolsrepository;

    //getter of al the tools
    public List<ToolsEntity> getAllTools(){
        return (List<ToolsEntity>) toolsrepository.findAll();
    }

    //save a tool into the database
    public ToolsEntity saveTool(ToolsEntity toolsEntity){
        return  toolsrepository.save(toolsEntity);
    }

    //get a tool by its id field
    public ToolsEntity getToolsById(Long id){
        return toolsrepository.findById(id).get();
    }

    //update info of a tool
    public ToolsEntity updateTool(ToolsEntity toolsEntity){
        return toolsrepository.save(toolsEntity);
    }

    //delete a tool from the database
    public boolean deleteTools(Long id) throws Exception{
        try{
            toolsrepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
