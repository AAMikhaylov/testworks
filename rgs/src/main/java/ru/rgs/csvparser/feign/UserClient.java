package ru.rgs.csvparser.feign;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.rgs.csvparser.model.UserReq;
import ru.rgs.csvparser.model.UserResp;

public interface UserClient {
    @RequestMapping(method = RequestMethod.POST, value = "/score")
    UserResp getUser(UserReq userReq);
}
