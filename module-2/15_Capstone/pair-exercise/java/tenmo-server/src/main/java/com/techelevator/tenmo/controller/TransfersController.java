package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.dao.UserDAO;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

@RestController
@RequestMapping(path="/transfers")




public class TransfersController {

    @Autowired
    TransfersDAO dao;
    @Autowired
    UserDAO userdao;
    @Autowired
    AccountsDAO accountsdao;

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public Transfers sendBucks(@RequestBody Transfers transfers, Principal principal){
    	String username = principal.getName();
		int userId = userdao.findIdByUsername(username);
		BigDecimal senderBalance = accountsdao.getBalance(userId);
		boolean enoughToTransfer = transfers.getTransferAmount().compareTo(senderBalance)< 0;
		if (userId != transfers.getAccountFrom())
		{
			throw new RuntimeException("Invalid Account");
		}
		else if (enoughToTransfer == true)
		{
			return dao.sendBucks(transfers.getAccountFrom(), transfers.getAccountTo(), transfers.getTransferAmount());
		}
		throw new RuntimeException ("Insufficient Funds");
		
		
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public List<User> listUser(){
    	
        return userdao.findAll();
    }

}
