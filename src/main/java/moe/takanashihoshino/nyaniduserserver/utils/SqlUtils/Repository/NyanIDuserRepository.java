package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import jakarta.transaction.Transactional;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.NyanIDuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface NyanIDuserRepository extends JpaRepository<NyanIDuser, String>, Serializable {

    @Query(value = "SELECT nickname FROM NyanIDuser where uid = ?1")
    String getNickname(String uid);

    @Query(value = "SELECT exp FROM NyanIDuser where uid = ?1")
    int getUserEXP(String uid);

    @Query(value = "SELECT Description FROM NyanIDuser where uid = ?1")
    String GetDescriptionByUid(String uid);

    @Modifying
    @Transactional
    @Query(value = "update NyanIDuser set Description = ?1  where uid = ?2")
    void SetDescriptionByUid(String Description,String uid);

    @Query(value = "SELECT IsDeveloper FROM NyanIDuser where uid = ?1")
    boolean UserIsDeveloper(String uid);


    @Modifying
    @Transactional
    @Query(value = "update NyanIDuser set nickname = ?1  where uid = ?2")
    void UpdateNickname(String nickname,String uid);



}
