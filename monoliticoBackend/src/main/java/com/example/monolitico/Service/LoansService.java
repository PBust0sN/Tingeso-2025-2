package com.example.monolitico.Service;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Repositories.LoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LoansService {
    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    FineService fineService;

    @Autowired
    ToolsService toolsService;

    //getter of all loans
    public List<LoansEntity> getAllLoans(){
        return (List<LoansEntity>) loansRepository.findAll();
    }

    //save a loan into the data base
    public Optional<LoansEntity> saveLoan(LoansEntity loansEntity){
        if(loansEntity.getReturnDate()!=null){
            return Optional.of(loansRepository.save(loansEntity));
        }
        return  Optional.empty();
    }

    // ///////////////////////////////////////////////// FALTA POR TERMINAR
    public Optional<LoansEntity> AddLoan(Long client_id){
        //verify that the client is allowed to have another loan
        ClientEntity client = clientService.getClientById(client_id);
        // we create a loan
        LoansEntity loansEntity = new LoansEntity();

        //if avaliable is true then the client is allowed for another loan
        if(client.getAvaliable()
            && !fineService.hasFinesByClientId(client_id)
            && !fineService.hasFinesOfToolReposition(client_id)
            && checkDates(loansEntity)) {

            return saveLoan(loansEntity);
        }

        return Optional.empty();
    }

    // verify date of loan
    public boolean checkDates(LoansEntity loan){
        //formating the delivery date
        String deliveryDate = loan.getDeliveryDate().toString();
        Date sqlDate1 = Date.valueOf(deliveryDate);
        LocalDate localDate1 = sqlDate1.toLocalDate();

        //formating the return date
        String returnDate = loan.getReturnDate().toString();
        Date sqlDate2 = Date.valueOf(returnDate);
        LocalDate localDate2 = sqlDate2.toLocalDate();

        long dias = ChronoUnit.DAYS.between(localDate1, localDate2);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 loan behind
        if(dias<0){
            return false;
        }
        return true;
    }

    public LoansEntity returnLoan(LoansEntity loansEntity){
        //if its returned before the return date, then the client don't gets his money back
        if(isReturnedBefore(loansEntity.getLoanId())){
            //logic of no return off the money
        }else{
            //logic of return money
        }
    }

    //verify that the loan ins't returned before its return date
    public boolean isReturnedBefore(Long id){
        LoansEntity loan = loansRepository.findById(id).get();
        //formating the delivery date
        String deliveryDate = loan.getDeliveryDate().toString();
        Date sqlDate1 = Date.valueOf(deliveryDate);
        LocalDate localDate1 = sqlDate1.toLocalDate();

        //formating the return date
        String returnDate = loan.getReturnDate().toString();
        Date sqlDate2 = Date.valueOf(returnDate);
        LocalDate localDate2 = sqlDate2.toLocalDate();

        long dias = ChronoUnit.DAYS.between(localDate1, localDate2);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 loan behind
        if(dias==0){
            return false;
        }
        return true;
    }

    public Long isReturnedAfter(Long id){
        LoansEntity loan = loansRepository.findById(id).get();
        //formating the delivery date
        String deliveryDate = loan.getDeliveryDate().toString();
        Date sqlDate1 = Date.valueOf(deliveryDate);
        LocalDate localDate1 = sqlDate1.toLocalDate();

        //formating the return date
        String returnDate = loan.getReturnDate().toString();
        Date sqlDate2 = Date.valueOf(returnDate);
        LocalDate localDate2 = sqlDate2.toLocalDate();

        long dias = ChronoUnit.DAYS.between(localDate1, localDate2);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 loan behind
        if(dias>0){
            return dias;
        }
        return (long) 0;
    }


    //calculateFine
    public Long calculateFine(Long id){
        if(isReturnedAfter(id)!=0){
            //if its return after, them we calculate the fine
            Long fine = isReturnedAfter(id); // //////////////////////////////////////
        }else{
            //nothing happens
        }
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
