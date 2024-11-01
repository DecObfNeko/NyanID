package moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository;

import moe.takanashihoshino.nyaniduserserver.SqlUtils.UserOAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UserOAuthRepository extends JpaRepository<UserOAuth, Integer>, Serializable {
}
