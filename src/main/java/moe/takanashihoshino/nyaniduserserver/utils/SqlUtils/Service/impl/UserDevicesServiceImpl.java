package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.impl;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.UserDevicesService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.UserDevices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserDevicesServiceImpl implements UserDevicesService {

    @Autowired
    private UserDevicesRepository userDevicesRepository;

    @Override
    public UserDevices save(UserDevices userDevices) {
        return userDevicesRepository.save(userDevices);
    }

    @Override
    public UserDevices delete(UserDevices userDevices) {
        userDevicesRepository.delete(userDevices);
        return null;
    }
}
