package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.UserDevices;
import org.apache.catalina.User;

public interface UserDevicesService {

    UserDevices save(UserDevices userDevices);

    UserDevices delete(UserDevices userDevices);
}
