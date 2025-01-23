package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import jakarta.transaction.Transactional;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.BanUserList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Repository
public interface BanUserRepository extends JpaRepository<BanUserList, String>,Serializable {

    @Query(value = "SELECT BanID FROM  BanUserList WHERE uid = ?1 AND isActive = true ")
   String findBanIDByUid(String uid);

}
