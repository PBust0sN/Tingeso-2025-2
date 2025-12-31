package com.example.ms_rates_service.Service;

import com.example.ms_rates_service.Entities.ClientEntity;
import com.example.ms_rates_service.Entities.FineEntity;
import com.example.ms_rates_service.Repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;
    @Autowired
    private ClientService clientService;

    public FineEntity getFineById(Long id) {
        return fineRepository.findById(id).get();
    }

    public List<FineEntity> getAllFine() {
        return fineRepository.findAll();
    }

    public List<FineEntity> getAllFinesByClientId(Long id){
        return fineRepository.findByClientId(id);
    }
    public FineEntity saveFine(FineEntity fineEntity) {
        return fineRepository.save(fineEntity);
    }

    public FineEntity updateFine(FineEntity fineEntity) {
        return fineRepository.save(fineEntity);
    }

    public boolean deleteFineById(Long id) throws Exception{
        try{
            fineRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public boolean hasFinesByClientId(Long id){
        //if the list of fines of a client is empty, it means that the client hasn't have fines
        if(fineRepository.findByClientId(id).isEmpty()){
            return false;
        }
        return true;
    }

    public boolean hasFinesOfToolReposition(Long id){
        if(fineRepository.getFineEntityByClientIdAndTypeIs(id, "reposition").isEmpty()){
            return false;
        }
        return true;
    }

    public boolean hasLessOrEqual5FinesByClientId(Long id){
        if(fineRepository.findByClientId(id).size()<=5){
            return true;
        }
        return false;
    }

    public void payFine(Long client_id, Long fine_id){
        ClientEntity client =  clientService.getClientById(client_id);
        FineEntity fine =  getFineById(fine_id);
        fine.setState("pagado");
        updateFine(fine);


        //need to count the fines that are not pending
        if(fineRepository.getPendingFinesByClientId(client_id).size()<=1){
            client.setState("activo");
            clientService.updateClient(client);
        }
    }
}
