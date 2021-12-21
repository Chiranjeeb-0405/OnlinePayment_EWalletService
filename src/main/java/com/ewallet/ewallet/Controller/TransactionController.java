package com.ewallet.ewallet.Controller;

import com.ewallet.ewallet.Model.Transaction;
import com.ewallet.ewallet.Model.User;
import com.ewallet.ewallet.Model.Wallet;
import com.ewallet.ewallet.Repository.TransactionRepository;
import com.ewallet.ewallet.Repository.WalletRepository;
import com.ewallet.ewallet.ThirdPartyService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private  static  final Logger LOGGER= LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    //send money
    @PostMapping("/sendMoney")
    public Transaction sendMoney(@RequestBody Transaction transaction) throws Exception{
        //from the request transaction body, we will get sender id
        //and receiver id and corresponding users by using our another user service running
        //on different spring boot application
        //and update their wallet

        User sender=userService.findUserById(transaction.getSender_id());
        User receiver=userService.findUserById(transaction.getReceiver_id());

        if(sender==null || receiver==null){
            LOGGER.error("Transaction can't be happen since one of the sender or receiver does not exist for request : {}",transaction.toString() );
            throw new Exception("Bad Payload");
        }
        Wallet senderWallet=walletRepository.findByUserId(sender.getId());
        Wallet receiverWallet=walletRepository.findByUserId(receiver.getId());

        int amount=transaction.getAmount();

        if(senderWallet.getBalance()< amount){
            LOGGER.error("Not sufficirnt balance for this transaction {}", transaction.toString());
            throw new Exception("Not sufficent balance");
        }
        senderWallet.setBalance(senderWallet.getBalance()-amount);
        receiverWallet.setBalance(receiverWallet.getBalance()+amount);
        //false-no transaction, true - successful transaction
        transaction.setStatus(Boolean.FALSE);
        LOGGER.info("transaction was successful with sender {} and receiver {}", sender.toString(),receiver.toString());
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        return transactionRepository.save(transaction);
    }
}
