package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.UserDevices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface UserDevicesRepository extends JpaRepository<UserDevices, String>, Serializable {


    @Query(value = "SELECT DeviceID,Session,Token FROM UserDevices WHERE uid = ?1 AND IsActive = true")
    UserDevices findByUid(String uid);

}
