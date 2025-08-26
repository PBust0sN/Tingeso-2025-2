package com.example.monolitico.Service;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    //Getter of all clients
    public List<ClientEntity> getAllClients(){
        return (List<ClientEntity>)  clientRepository.findAll();
    }

    //save a client into the data base
    public ClientEntity saveClient(ClientEntity clientEntity){
        return clientRepository.save(clientEntity);
    }

    //get a client by his id field
    public ClientEntity getClientById(Long id){
        return clientRepository.findById(id).get();
    }

    //update info of a client
    public ClientEntity updateClient(ClientEntity clientEntity){
        return clientRepository.save(clientEntity);
    }

    //delete a client from the data base
    public boolean deleteClient(Long id) throws Exception{
        try{
            clientRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
