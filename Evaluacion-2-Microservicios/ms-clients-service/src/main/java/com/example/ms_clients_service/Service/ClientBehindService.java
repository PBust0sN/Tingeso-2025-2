package com.example.ms_clients_service.Service;

import com.example.ms_clients_service.Entities.ClientBehindEntity;
import com.example.ms_clients_service.Repository.ClientBehindRepository;
import com.example.ms_clients_service.Repository.ToolsRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientBehindService {

    @Autowired
    private ClientBehindRepository clientBehindRepository;

    //in this case we only gona implement create, getall and delete
    //because its a report, when save, we only can delete, not update

    public List<ClientBehindEntity> getAllClientBehind() {
        return clientBehindRepository.findAll();
    }

    public ClientBehindEntity getClientBehindByReportId(Long reportId){
        return clientBehindRepository.findByReportId(reportId);
    }

    public ClientBehindEntity createClientBehind(ClientBehindEntity clientBehindEntity) {
        return clientBehindRepository.save(clientBehindEntity);
    }

    public boolean deleteClientBehindById(Long id) throws Exception {
        try{
            clientBehindRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
