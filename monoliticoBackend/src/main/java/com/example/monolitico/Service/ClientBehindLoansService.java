package com.example.monolitico.Service;

import com.example.monolitico.Entities.ClientBehindLoansEntity;
import com.example.monolitico.Repositories.ClientBehindLoansRepository;
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
