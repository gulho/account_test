package ee.gulho.account.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
@Mapper
public interface BalanceRepository {

    @Insert("insert into balance (amount, currency, account_id) VALUES (#{amount}, #{currency}, #{account})")
    Long insertBalance(BigDecimal amount, String currency, UUID account);

    @Update("update balance set amount = #{amount} where id = #{id}")
    void updateBalanceAmount(BigDecimal amount, Integer id);
}
