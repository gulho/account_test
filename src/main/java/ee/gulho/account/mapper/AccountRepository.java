package ee.gulho.account.mapper;

import ee.gulho.account.entity.Account;
import ee.gulho.account.entity.Balance;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
@Repository
public interface AccountRepository {

    @Select("select * from account")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "balances", column = "id", javaType = List.class, many = @Many(select = "getAllBalancesByAccount"))
        })
    List<Account> getAllAccounts();

    @Select("select * from account where id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "balances", column = "id", javaType = List.class, many = @Many(select = "getAllBalancesByAccount"))
    })
    Optional<Account> getAccountById(UUID id);

    @Select("select id, amount::numeric, currency, account_id from balance where account_id=#{account_id}")
    List<Balance> getAllBalancesByAccount();

    @Insert("insert into account (id, customer_id, created_session_id) values (#{id}, #{customerId}, #{sessionId})")
    void insertAccount(UUID id, UUID customerId, String sessionId);

}
