package com.example.ms_loans_service.Service;

import com.example.ms_loans_service.DTO.ReturnLoanDTO;
import com.example.ms_loans_service.Entities.*;
import com.example.ms_loans_service.Models.ClientModel;
import com.example.ms_loans_service.Models.FineModel;
import com.example.ms_loans_service.Models.RecordsModel;
import com.example.ms_loans_service.Models.ToolsLoansModel;
import com.example.ms_loans_service.Models.ToolsModel;
import com.example.ms_loans_service.Repositories.LoansRepository;
import com.sun.jdi.LongValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LoansService {
    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    ClientRemoteService clientRemoteService;

    @Autowired
    FineRemoteService fineRemoteService;

    @Autowired
    ToolsRemoteService toolsRemoteService;

    @Autowired
    ToolsLoansRemoteService toolsLoansRemoteService;
    @Autowired
    private RecordsRemoteService recordsRemoteService;

    //getter of all loans
    public List<LoansEntity> getAllLoans() {
        return (List<LoansEntity>) loansRepository.findAll();
    }

    //save a loan into the data base
    public Optional<LoansEntity> saveLoan(LoansEntity loansEntity) {
        if (loansEntity.getReturnDate() != null) {
            return Optional.of(loansRepository.save(loansEntity));
        }
        return Optional.empty();
    }


    public List<String> addLoan(Long staff_id, Long client_id, List<Long> tools_ids, Long days) {
        List<String> errors = new ArrayList<>();

        System.out.println(staff_id);
        System.out.println(client_id);
        System.out.println(tools_ids);
        System.out.println(days);

        // get client
        ClientModel client = clientRemoteService.getClientById(client_id);

        // Verification 1: avaliable client
        //if (!client.getAvaliable()) {
        //    errors.add("Client is not available for new loans.");
        //}

        // Verification 2: fines, aready taking cared in the state section
        //if (fineService.hasFinesByClientId(client_id)) {
        //    errors.add("Client has pending fines.");
        //}

        // Verification 3: fine by reposition
        // TODO: Implement check for tool reposition fines
        // if (fineRemoteService.hasFinesOfToolReposition(client_id)) {
        //     errors.add("Client has fines for tool replacement.");
        // }

        // Verification 4: behind loans
        if (clientRemoteService.hasExpiredLoansById(client_id)) {
            errors.add("Client has expired loans.");
        }
        // Verification 5: client state
        if(Objects.equals(client.getState(), "restringido")){
            errors.add("Client has fines to be pay");
        }
        // Verification 6: number of loans
        if(clientRemoteService.findAllLoansByClientId(client.getClient_id()).size()>=5){
            errors.add("Client has already max out the number of loans he can have.");
        }
        // if we encounter errors then we don't continue
        if (!errors.isEmpty()) {
            System.out.println(errors);
            return errors;
        }

        // create the loan
        LoansEntity loansEntity = new LoansEntity();
        LocalDateTime date = LocalDateTime.now();

        loansEntity.setDate(date.toLocalDate());
        loansEntity.setDeliveryDate(date.toLocalDate());
        loansEntity.setReturnDate(date.plusDays(days).toLocalDate());
        loansEntity.setActive(true);

        // validate dates
        if (!checkDates(loansEntity)) {
            errors.add("Loan dates are incorrect.");
            return errors;
        }

        loansEntity.setLoanType("loan");
        loansEntity.setStaffId(staff_id);
        loansEntity.setClientId(client_id);
        loansEntity.setExtraCharges(0L);

        long amount = 0L;

        //check if the client has other loans with the tools
        for (Long tool_id : tools_ids) {
            ToolsModel tool = toolsRemoteService.getToolById(tool_id);

            //is the tool loaned to much?
            //the criteria: if a tool is present in more than the actual stock, then
            //the tool cant be loan
            // TODO: Implement proper check for tool loan count
            // Long loanNumber = Long.valueOf(toolsLoansRemoteService.getToolLoansByToolId(tool_id).size());
            // if(loanNumber>=tool.getStock()){
            //     errors.add("Too many " + tool.getToolName() + " are loan, cant loan more.");
            // }

            if(tool.getStock()<=0){
                errors.add("Tool " + tool.getToolName() + " isnt in stock.");
            }
            if(!Objects.equals(tool.getDisponibility(), "Disponible")){
                errors.add("Tool " + tool.getToolName() + " hans't disponibility.");
            }

            if (clientRemoteService.HasTheSameToolInLoanByClientId(client_id, tool_id)) {
                errors.add("Client already has a loan with tool: " + tool.getToolName());
            } else {
                amount += tool.getLoanFee();
            }
        }

        // if we have errors, don't continue
        if (!errors.isEmpty()) {
            System.out.println(errors);
            return errors;
        }

        // save the loan
        loansEntity.setAmount(amount);
        saveLoan(loansEntity);

        //update tools
        for (Long tool_id : tools_ids) {
            ToolsModel tool = toolsRemoteService.getToolById(tool_id);
            // Update tool stock
            toolsRemoteService.updateToolQuantity(tool_id, tool.getStock() - 1);

            ToolsLoansModel toolsLoansModel = new ToolsLoansModel();
            toolsLoansModel.setToolId(tool_id);
            toolsLoansModel.setLoanId(loansEntity.getLoanId());
            toolsLoansRemoteService.saveToolLoan(toolsLoansModel);
        }

        // Registrar movimiento en Kardex
        RecordsModel record = new RecordsModel();
        record.setRecordType("Loan");
        record.setRecordDate(Date.valueOf(date.toLocalDate()));
        record.setLoanId(loansEntity.getLoanId());
        record.setClientId(client_id);
        record.setRecordAmount(amount);
        recordsRemoteService.saveRecord(record);

        System.out.println(errors);
        return errors;
    }


    // verify date of loan
    public boolean checkDates(LoansEntity loan) {
        // Fecha actual
        LocalDate today = LocalDate.now();

        //formating the return date

        LocalDate localDate = loan.getReturnDate();

        long dias = ChronoUnit.DAYS.between(today, localDate);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 loan behind
        if (dias < 0) {
            return false;
        }
        return true;
    }

    public ReturnLoanDTO returnLoan(LoansEntity loansEntity) {
        // first we calculate the costs
        ReturnLoanDTO loanCost = calculateCosts(loansEntity.getLoanId());
        System.out.println("1");
        List<String> toolslist = new ArrayList<>();
        loanCost.setLowDmgAmount(0L);
        loanCost.setTools(toolslist);
        // we have to update the tools state
        List<ToolsLoansModel> toolsLoans = toolsLoansRemoteService.getToolLoansByLoanId(loansEntity.getLoanId());
        List<Long> toolsId = new ArrayList<>();
        for (ToolsLoansModel tl : toolsLoans) {
            toolsId.add(tl.getToolId());
        }
        for (Long toolId : toolsId) {
            ToolsModel toolsEntity = toolsRemoteService.getToolById(toolId);

            if ("Dañada".equals(toolsEntity.getInitialState())) {
                toolsEntity.setInitialState("Bueno");
                //if is damaged then that tool is taken out of system by reducing the stock
                toolsEntity.setStock(toolsEntity.getStock() - 1);
            }else if("Malo".equals(toolsEntity.getInitialState())){
                toolsEntity.setInitialState("Bueno");
                List<String> tools = loanCost.getTools();
                tools.add(toolsEntity.getToolName());
                loanCost.setTools(tools);
                Long lowDmgAmount = loanCost.getLowDmgAmount();
                loanCost.setLowDmgAmount(lowDmgAmount+toolsEntity.getLowDmgFee());
            }
            //we add up to the stock
            toolsRemoteService.updateToolQuantity(toolId, toolsEntity.getStock() + 1);
        }
        //NOTE: the extra chargues only account for the repo ammount other costs
        //are held by fines
        loansEntity.setExtraCharges(loanCost.getRepoAmount()+loanCost.getFineAmount()+loanCost.getLowDmgAmount());

        // then create a record
        RecordsModel record = new RecordsModel();
        record.setRecordType("Return");
        record.setRecordAmount(loanCost.getRepoAmount()+loanCost.getFineAmount()+loanCost.getLowDmgAmount());
        LocalDateTime date = LocalDateTime.now();
        record.setRecordDate(Date.valueOf(date.toLocalDate()));
        record.setLoanId(loansEntity.getLoanId());
        record.setClientId(loansEntity.getClientId());
        recordsRemoteService.saveRecord(record);

        // save loan
        //once is return the loan isnt active no more
        loansEntity.setActive(false);
        Optional<LoansEntity> returnLoan = saveLoan(loansEntity);
        loanCost.setLoan(returnLoan.get());
        return loanCost;
    }

    public ReturnLoanDTO calculateCosts(Long loanId) {
        ReturnLoanDTO dto = new ReturnLoanDTO();
        ReturnLoanDTO dto1 = new ReturnLoanDTO();
        ReturnLoanDTO dto2 = new ReturnLoanDTO();

        Optional<LoansEntity> loan = loansRepository.findById(loanId);
        if (reamaningDaysOnLoan(loanId) > 0) {
            dto2 = calculateRepoFine(loanId, loan.get().getClientId());
        }
        if (reamaningDaysOnLoan(loanId) < 0) {
            dto1 = calculateFine(loanId, loan.get().getClientId());
            dto2 = calculateRepoFine(loanId, loan.get().getClientId());
        }
        dto.setRepoAmount(dto2.getRepoAmount());
        dto.setRepoFine(dto2.getRepoFine());
        dto.setFineAmount(dto1.getFineAmount());
        dto.setFine(dto1.getFine());
        if(dto2.getRepoAmount()==null){
            dto.setRepoAmount(0L);
        }
        if(dto1.getFineAmount()==null){
            dto.setFineAmount(0L);
        }
        return dto;
    }


    public ReturnLoanDTO calculateRepoFine(Long id, Long clientId){
        ReturnLoanDTO dto = new ReturnLoanDTO();
        List<ToolsLoansModel> toolsLoans = toolsLoansRemoteService.getToolLoansByLoanId(id);
        List<Long> tools = new ArrayList<>();
        for (ToolsLoansModel tl : toolsLoans) {
            tools.add(tl.getToolId());
        }
        Long repofine = 0L;
        for(Long toolId : tools){
            if(toolsRemoteService.getToolById(toolId).getInitialState().equals("Dañada")) {
                repofine += toolsRemoteService.getToolById(toolId).getRepositionFee();

                //record to the cardex of a damaged tool
                RecordsModel record = new RecordsModel();
                record.setRecordType("DownTool");
                record.setRecordAmount(toolsRemoteService.getToolById(toolId).getRepositionFee());
                record.setToolId(toolId);
                record.setLoanId(id);
                record.setRecordDate(Date.valueOf(LocalDate.now()));
                recordsRemoteService.saveRecord(record);
            }
        }

        if(repofine > 0){
            FineModel newFine =  new FineModel();
            newFine.setState("pendiente");
            newFine.setAmount(repofine);
            newFine.setType("dmg fine");
            newFine.setClientId(clientId);
            LocalDateTime date = LocalDateTime.now();
            newFine.setDate(Date.valueOf(date.toLocalDate()));
            newFine.setLoanId(id);
            fineRemoteService.saveFine(newFine);
            //if a client has a pending fine, then we mark it as "restringido"

            ClientModel clientEntity = clientRemoteService.getClientById(clientId);
            clientEntity.setState("restringido");
            clientRemoteService.updateClient(clientEntity);

            dto.setRepoAmount(repofine);
            dto.setRepoFine(newFine);
        }else {
            dto.setRepoAmount(0L);
        }
        if(dto.getFineAmount()==null){
            dto.setFineAmount(0L);
        }
        return dto;
    }

    //verify that the loan ins't returned before its return date
    public Long reamaningDaysOnLoan(Long id){
        LoansEntity loan = loansRepository.findById(id).get();

        LocalDate date = LocalDate.now();

        //formating the return date
        String returnDate = loan.getReturnDate().toString();
        Date sqlDate2 = Date.valueOf(returnDate);
        LocalDate localDate2 = sqlDate2.toLocalDate();

        long dias = ChronoUnit.DAYS.between(date, localDate2);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 day behind
        return dias;
    }



    //calculateFine
    public ReturnLoanDTO calculateFine(Long id, Long clientId){
        ReturnLoanDTO dto = new ReturnLoanDTO();
        if(reamaningDaysOnLoan(id)<0){
            //if its return after, them we calculate the fine
            //we need to get all the tools in the loan
            List<ToolsLoansModel> toolsLoans = toolsLoansRemoteService.getToolLoansByLoanId(id);
            List<Long> tools = new ArrayList<>();
            for (ToolsLoansModel tl : toolsLoans) {
                tools.add(tl.getToolId());
            }
            Long fine = 0L;

            for(Long toolId : tools){
                //Multa atraso = días de atraso × tarifa diaria de multa.
                //per tool
                //multiply by -1 because if the loan is behind in days, those are going to be negative
                fine += -1 * reamaningDaysOnLoan(id) * toolsRemoteService.getToolById(toolId).getDiaryFineFee();
            }

            if(fine > 0) {
                FineModel newFine = new FineModel();
                newFine.setState("pendiente");
                newFine.setAmount(fine);
                newFine.setType("behind fine");
                newFine.setClientId(clientId);
                LocalDateTime date = LocalDateTime.now();
                newFine.setDate(Date.valueOf(date.toLocalDate()));
                newFine.setLoanId(id);
                fineRemoteService.saveFine(newFine);

                //The client now has a pending fine, then he gets restricted from taking more loans
                ClientModel clientEntity = clientRemoteService.getClientById(clientId);
                clientEntity.setState("restringido");
                clientRemoteService.updateClient(clientEntity);

                dto.setFineAmount(fine);
                dto.setFine(newFine);
            }else{
                dto.setFineAmount(0L);
            }
        }
        if(dto.getFineAmount()==null){
            dto.setFineAmount(0L);
        }
        return dto;
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
