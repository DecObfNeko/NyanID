package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.impl;


import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.BanUserList;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.BanUserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.BanUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BanUserServiceImpl implements BanUserService {

    @Autowired
    private BanUserRepository banUserRepository;

    @Override
    public BanUserList save(BanUserList banUserList) {
        return banUserRepository.save(banUserList);
    }
}
