package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.UserOAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UserOAuthRepository extends JpaRepository<UserOAuth, Integer>, Serializable {
}
