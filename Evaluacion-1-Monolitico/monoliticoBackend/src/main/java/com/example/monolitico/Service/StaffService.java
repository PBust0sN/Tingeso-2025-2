package com.example.monolitico.Service;

import com.example.monolitico.Entities.StaffEntity;
import com.example.monolitico.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    //Getter of all the staff
    public List<StaffEntity> getAllStaff(){
        return (List<StaffEntity>) staffRepository.findAll();
    }

    //save a staff member in the database
    public StaffEntity saveStaff(StaffEntity staffEntity){
        return staffRepository.save(staffEntity);
    }

    //get a staff member by his id field
    public StaffEntity getStaffById(Long id){
        return staffRepository.findById(id).get();
    }

    //update info of a staff member
    public StaffEntity updateStaff(StaffEntity staffEntity){
        return staffRepository.save(staffEntity);
    }

    //delete a staff member from the database
    public boolean deleteStaff(Long id) throws Exception{
        try{
            staffRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
