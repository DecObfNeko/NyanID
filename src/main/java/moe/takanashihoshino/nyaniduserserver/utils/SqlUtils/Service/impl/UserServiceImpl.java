package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.impl;

import moe.takanashihoshino.nyaniduserserver.server.web.Public.UserResponse;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Accounts;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl  implements UserService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Value("${NyanidSetting.HOST}")
    private String Host;

    @Override
    public Accounts save(Accounts accounts) {
        return accountsRepository.save(accounts);
    }

    public List<UserResponse> searchUsers(String keyword) {
        // 创建分页请求（第0页，7条结果，按username升序）
        Pageable pageable = PageRequest.of(0, 7, Sort.by("username").ascending());

        Page<Accounts> userPage = accountsRepository.searchByUsername(keyword, pageable);

        return userPage.getContent().stream()
                .map(user -> new UserResponse(user.getUsername(), user.getUid()))
                .collect(Collectors.toList());
    }
}
