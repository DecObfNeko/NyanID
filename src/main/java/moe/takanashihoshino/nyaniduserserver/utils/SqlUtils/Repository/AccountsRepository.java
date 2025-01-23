package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import jakarta.transaction.Transactional;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String>, Serializable {

//    @Query(value = "SELECT uid FROM Accounts WHERE uid = ?1 or email = ?1 or username = ?1")
//    String find(String info);

    @Query(value = "SELECT uid FROM Accounts WHERE uid = ?1 or email = ?1 or username = ?1")
    Accounts find(String info);

    @Query(value = "SELECT email FROM Accounts WHERE username = ?1 ")
    String findByUsername(String username);

    @Query(value = "SELECT email FROM Accounts WHERE uid = ?1")
    String GetEmailByUid(String uid);

    @Query(value = "SELECT uid FROM Accounts WHERE email = ?1")
    String findByEmail(String email);

    @Query(value = "SELECT password FROM Accounts WHERE email = ?1")
    String LoginByEmail(String email);

    @Query(value = "SELECT COUNT(*) AS nums FROM Accounts ")
    String GetAllUser();

//    @Query(value = "SELECT COUNT(*) AS nums FROM Accounts where isBanned = true ")
//    String GetAllBannedUser();

    @Modifying
    @Transactional
    @Query(value = "update Accounts set username = ?1 where uid = ?2")
    void UpdateUsername(String username,String uid);

    @Modifying
    @Transactional
    @Query(value = "update Accounts set password = ?2 where email = ?1")
    void UpdatePassword(String email,String pwd);
}
