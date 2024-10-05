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
    @Query(value = "INSERT INTO nyaniduser (uid, clientid,is_developer ,exp, hwid, nickname,BCookie,is_developer) VALUES (?1, null,0, 0, null, ?2,null,0)" , nativeQuery = true)
    void CreateNyanID(String uid,String nickname);


    @Query(value = "SELECT uid FROM NyanIDuser WHERE BCookie = ?1")
    String getAccount(String Bcookie);


    @Query(value = "SELECT clientid FROM NyanIDuser where uid = ?1")
    String getClienID(String uid);

    @Query(value = "SELECT nickname FROM NyanIDuser where uid = ?1")
    String getNickname(String uid);



    @Modifying
    @Transactional
    @Query(value = "update NyanIDuser set nickname = ?1  where uid = ?2")
    void UpdateNickname(String nickname,String uid);


    @Modifying
    @Transactional
    @Query(value = "update NyanIDuser set clientid = ?1,BCookie = ?2  where uid = ?3")
    void UpdateNyanID(String clientid,String bcookie,String uid);

}
