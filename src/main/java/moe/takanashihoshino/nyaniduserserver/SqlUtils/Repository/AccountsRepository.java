package moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository;

import jakarta.transaction.Transactional;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String>, Serializable {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO accounts (uid, bind, email, is_banned, is_active, password, username) VALUES (?1, null, ?2, false, true, ?3, ?4)" , nativeQuery = true)
    void CreateNewAccount(String uid,String email,String password,String username);


    @Query(value = "SELECT uid FROM Accounts WHERE uid = ?1")
     String findByUid(String uid);

    @Query(value = "SELECT email FROM Accounts WHERE email = ?1")
    String findByEmail(String email);
}