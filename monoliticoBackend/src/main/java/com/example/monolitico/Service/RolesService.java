package com.example.monolitico.Service;

import com.example.monolitico.Entities.RolesEntity;
import com.example.monolitico.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;

    //getter of all roles
    public List<RolesEntity> getAllRoles(){
        return (List<RolesEntity>) rolesRepository.findAll();
    }

    //save a role into the database
    public RolesEntity saveRole(RolesEntity rolesEntity){
        return rolesRepository.save(rolesEntity);
    }

    //get a role by its id field
    public RolesEntity getRolesById(Long id){
        return  rolesRepository.findById(id).get();
    }

    //update info of a role
    public RolesEntity updateRole(RolesEntity rolesEntity){
        return rolesRepository.save(rolesEntity);
    }

    //delete a role from the database
    public boolean deleteRole(Long id) throws Exception{
        try{
            rolesRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
