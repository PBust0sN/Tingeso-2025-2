package com.example.monolitico.Service;

import com.example.monolitico.Entities.FeeEntity;
import com.example.monolitico.Repositories.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeService {
    @Autowired
    private FeeRepository feeRepository;

    //getter of all fee's
    public List<FeeEntity> getAllFees(){
        return (List<FeeEntity>) feeRepository.findAll();
    }

    //save a fee unto the data base
    public FeeEntity saveFee(FeeEntity feeEntity){
        return feeRepository.save(feeEntity);
    }

    //get a fee by its id field
    public FeeEntity getFeeById(Long id){
        return feeRepository.findById(id).get();
    }

    //update info of a fee
    public FeeEntity updateFee(FeeEntity feeEntity){
        return feeRepository.save(feeEntity);
    }

    //delete a fee from the data base
    public boolean deleteFee(Long id) throws Exception{
        try{
            feeRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
