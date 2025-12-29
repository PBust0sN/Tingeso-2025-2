package com.example.ms_clients_service.Service;

import com.example.ms_clients_service.Entities.ClientBehindLoansEntity;
import com.example.ms_clients_service.Repository.ClientBehindLoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientBehindLoansService {
    @Autowired
    private ClientBehindLoansRepository clientBehindLoansRepository;

    public ClientBehindLoansEntity createCBL(ClientBehindLoansEntity  clientBehindLoansEntity) {
        return clientBehindLoansRepository.save(clientBehindLoansEntity);
    }

    public List<Long> findClientIdByClientIdBehind (Long  clientIdBehind) {
        return clientBehindLoansRepository.findLoansByClientIdBehind(clientIdBehind);
    }

    public ClientBehindLoansEntity getById(Long clientBehindLoansId) {
        return  clientBehindLoansRepository.getById(clientBehindLoansId);
    }
}
