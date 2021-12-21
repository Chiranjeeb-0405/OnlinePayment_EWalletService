package com.ewallet.ewallet.Controller;

import com.ewallet.ewallet.Model.Wallet;
import com.ewallet.ewallet.Repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @PostMapping("/create")
    public Integer createNewWallet(@RequestBody Wallet wallet){
        Wallet walletObject=walletRepository.save(wallet);
        return walletObject.getId();
    }

    @GetMapping("/findWallet/{userid}")
    public Wallet findWalletByUserId(@PathVariable int userid){
        //doubtful
        return walletRepository.findByUserId(userid);
    }

    @PutMapping("/update")
    public Wallet updateWallet(@RequestBody Wallet wallet){
        Wallet wallet1=walletRepository.save(wallet);
        return wallet1;
    }
}
