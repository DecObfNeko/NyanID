package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository;

import jakarta.transaction.Transactional;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Accounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String>, Serializable {

    @Query(value = "SELECT a FROM Accounts a WHERE a.uid = ?1 or a.email = ?1 or a.username = ?1 or a.bind = ?1 ")
    Accounts GetUser(String info);

    @Query(value = "SELECT uid FROM Accounts WHERE uid = ?1 or email = ?1 or username = ?1")
    Accounts find(String info);

    @Query(value = "SELECT email FROM Accounts WHERE uid = ?1")
    String GetEmailByUid(String uid);

    @Query(value = "SELECT uid FROM Accounts WHERE email = ?1")
    String findByEmail(String email);

    @Query(value = "SELECT password FROM Accounts WHERE email = ?1")
    String LoginByEmail(String email);

    @Query(value = "SELECT COUNT(*) AS nums FROM Accounts ")
    int GetAllUser();


    @Modifying
    @Transactional
    @Query(value = "update Accounts set username = ?1 where uid = ?2")
    void UpdateUsername(String username,String uid);

    @Modifying
    @Transactional
    @Query(value = "update Accounts set SecretKey = ?1 where uid = ?2")
    void UpdateSecretKey(String SecretKey,String uid);

    @Modifying
    @Transactional
    @Query(value = "update Accounts set SecretKey = null where uid = ?1")
    void DeleteSecretKey(String uid);

    @Modifying
    @Transactional
    @Query(value = "update Accounts set bind = ?1 where uid = ?2")
    void BindMinecraftAccount(String uuid,String uid);

    @Modifying
    @Transactional
    @Query(value = "update Accounts set password = ?2 where email = ?1")
    void UpdatePassword(String email,String pwd);

    @Query("SELECT u FROM Accounts u " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY LOWER(u.username) ASC")
    Page<Accounts> searchByUsername(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
