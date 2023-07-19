package ee.gulho.account.mapper;

import ee.gulho.account.entity.Transaction;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
@Mapper
public interface TransactionRepository {

    @Insert("insert into transaction (id, account_id, currency, amount, direction, description) " +
            "VALUES (#{id}, #{accountId}, #{currency}, #{amount}, #{direction}, #{description})")
    void createTransaction(UUID id, UUID accountId, String currency, BigDecimal amount, String direction, String description);

    @Select("select * from transaction where id = #{id}")
    @Results(value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "transactionId", column = "id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "direction", column = "direction"),
            @Result(property = "description", column = "description")
    })
    Optional<Transaction> getTransactionById(UUID id);
}
