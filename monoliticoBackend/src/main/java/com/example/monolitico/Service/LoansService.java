package com.example.monolitico.Service;

import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Repositories.LoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoansService {
    @Autowired
    private LoansRepository loansRepository;

    //getter of all loans
    public List<LoansEntity> getAllLoans(){
        return (List<LoansEntity>) loansRepository.findAll();
    }

    //save a loan into the data base
    public LoansEntity saveLoan(LoansEntity loansEntity){
        return loansRepository.save(loansEntity);
    }

    //get a loan by its id field
    public LoansEntity findLoanById(Long id){
        return loansRepository.findById(id).get();
    }

    //update info of a loan
    public LoansEntity updateLoan(LoansEntity loansEntity){
        return loansRepository.save(loansEntity);
    }

    //delete a loan from the database
    public boolean deleteLoan(Long id) throws Exception{
        try{
            loansRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
