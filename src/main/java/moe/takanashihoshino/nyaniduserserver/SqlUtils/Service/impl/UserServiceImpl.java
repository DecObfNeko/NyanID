package moe.takanashihoshino.nyaniduserserver.SqlUtils.Service.impl;

import moe.takanashihoshino.nyaniduserserver.SqlUtils.Accounts;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  implements UserService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public Accounts save(Accounts accounts) {
        return accountsRepository.save(accounts);
    }
}
