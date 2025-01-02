package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service;


import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.BanUserList;

public interface BanUserService {

    BanUserList save(BanUserList banUserList);
}
