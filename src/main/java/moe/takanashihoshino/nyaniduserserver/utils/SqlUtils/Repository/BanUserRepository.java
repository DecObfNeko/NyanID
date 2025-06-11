package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.BanUserList;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.UserDevices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface BanUserRepository extends JpaRepository<BanUserList, String>,Serializable {

    @Query(value = "SELECT BanID FROM  BanUserList WHERE uid = ?1 AND isActive = true ")
   String findBanIDByUid(String uid);

    @Query(value = "SELECT COUNT(*) AS nums FROM BanUserList WHERE uid = ?1 AND isActive = false AND BannedBy = 'NAC' ")
    int COUNTByUid(String uid);

    @Query("SELECT b FROM BanUserList b WHERE b.uid = ?1")
    Page<BanUserList> searchByUid(String keyword, Pageable pageable);
}
