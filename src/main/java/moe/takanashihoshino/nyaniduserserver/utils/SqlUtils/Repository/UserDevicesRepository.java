package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import jakarta.transaction.Transactional;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.UserDevices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public interface UserDevicesRepository extends JpaRepository<UserDevices, String>, Serializable {

    @Query(value = "SELECT uid FROM UserDevices WHERE Token = ?1  AND IsActive = true")
    String findUidByToken(String Token);

    @Query(value = "SELECT clientid FROM UserDevices WHERE Token = ?1  AND IsActive = true")
    String findClientIdByToken(String Token);

    @Query(value = "SELECT Token FROM UserDevices WHERE Session = ?1  AND IsActive = true")
    String findTokenBySession(String Session);

    @Query(value = "SELECT clientid FROM UserDevices WHERE Session = ?1  AND IsActive = true")
    String findClientIdBySession(String Session);

    @Query(value = "SELECT Session FROM UserDevices WHERE Session = ?1  AND IsActive = true")
    String findSessionBySession(String Session);

    @Query(value = "SELECT CreateTime FROM UserDevices WHERE Session = ?1  AND IsActive = true")
    LocalDateTime findTimeBySession(String Session);

    @Transactional
    @Modifying
    @Query(value = "delete UserDevices WHERE Session = ?1")
    void deleteBySession(String Session);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserDevices u WHERE  u.CreateTime < :cutoffDateTime")
    int deleteByCreateTimeBefore(@Param("cutoffDateTime") LocalDateTime cutoffDateTime);


}
