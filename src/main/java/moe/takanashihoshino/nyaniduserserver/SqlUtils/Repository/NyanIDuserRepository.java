package moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository;

import jakarta.transaction.Transactional;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.NyanIDuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface NyanIDuserRepository extends JpaRepository<NyanIDuser, String>, Serializable {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO nyaniduser (uid, clientid, exp, hwid, nickname) VALUES (?1, null, 0, null, ?2)" , nativeQuery = true)
    void CreateNyanID(String uid,String nickname);
}
