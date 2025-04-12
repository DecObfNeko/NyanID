package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import jakarta.transaction.Transactional;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.NyanIDuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Repository
public interface NyanIDuserRepository extends JpaRepository<NyanIDuser, String>, Serializable {

    @Query(value = "SELECT * FROM nyaniduser where uid = ?1",nativeQuery = true)
    NyanIDuser getUser(String uid);

    @Modifying
    @Transactional
    @Query(value = "update NyanIDuser set Description = ?1  where uid = ?2")
    void SetDescriptionByUid(String Description,String uid);



    @Modifying
    @Transactional
    @Query(value = "update NyanIDuser set nickname = ?1  where uid = ?2")
    void UpdateNickname(String nickname,String uid);



}
