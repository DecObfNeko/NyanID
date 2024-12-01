package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.UserDevices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UserDevicesRepository extends JpaRepository<UserDevices, String>, Serializable {






}
