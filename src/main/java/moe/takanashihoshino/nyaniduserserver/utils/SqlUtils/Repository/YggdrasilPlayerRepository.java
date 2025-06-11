package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.YggdrasilPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface YggdrasilPlayerRepository extends JpaRepository<YggdrasilPlayer,String>, Serializable {

    @Query(value = "SELECT y.SkinTexturesHash FROM  YggdrasilPlayer y WHERE y.uuid = ?1")
    String getSkinTexturesHash(String uuid);

    @Query(value = "SELECT y.CAPETexturesHash FROM  YggdrasilPlayer y WHERE y.uuid = ?1")
    String getCAPETexturesHash(String uuid);

    @Query(value = "SELECT y.SkinTexturesType FROM  YggdrasilPlayer y WHERE y.uuid = ?1")
    int getSkinTexturesType(String uuid);
}
