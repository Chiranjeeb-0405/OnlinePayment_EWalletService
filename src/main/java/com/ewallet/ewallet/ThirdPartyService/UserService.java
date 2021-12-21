package com.ewallet.ewallet.ThirdPartyService;

import com.ewallet.ewallet.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private RestTemplate restTemplate;

    //rest call to another endpoint (network call)
    public User findUserById(int userId){
        String url="http://localhost:8080/users/findUser/{userId}";
        Map<String,Integer> parms=new HashMap<>();
        parms.put("userId",userId);
        ResponseEntity<User> responseEntity=restTemplate.getForEntity(url,User.class,parms);
        if(responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return null;
        }
        else{
            return responseEntity.getBody();
        }
    }
}
