package com.example.ms_clients_service.Service;

import com.example.ms_clients_service.Entities.ClientEntity;
import com.example.ms_clients_service.Entities.ClientLoansEntity;
import com.example.ms_clients_service.Repository.ClientLoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientLoansService {

    @Autowired
    ClientLoansRepository clientLoansRepository;

    //getter of all the relations
    public List<ClientLoansEntity> getAllClientsLoans(){
        return (List<ClientLoansEntity>) clientLoansRepository.findAll();
    }

    //save a clientloan relation into the data base
    public ClientLoansEntity saveClientLoan(ClientLoansEntity clientLoansEntity){
        return clientLoansRepository.save(clientLoansEntity);
    }

    //get a relation by his id field
    public ClientLoansEntity getClientLoansById(Long id){
        return clientLoansRepository.findById(id).get();
    }

    //update info of a client
    public ClientLoansEntity updateClientLoans(ClientLoansEntity clientLoansEntity){
        return clientLoansRepository.save(clientLoansEntity);
    }

    //delete a client from the data base
    public boolean deleteClientLoans(Long id) throws Exception{
        try{
            clientLoansRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
