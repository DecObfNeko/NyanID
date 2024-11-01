package moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository;

import moe.takanashihoshino.nyaniduserserver.SqlUtils.OAuthApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface OAuthAppRepository extends JpaRepository<OAuthApp, Integer>, Serializable {
    @Query(value = "SELECT COUNT(*) AS nums FROM OAuthApp where IsActive = true ")
    String GetAllApplication();

}
