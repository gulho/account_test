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

    @Insert("insert into balance (amount, currency, account_id, created_session_id) VALUES (#{amount}, #{currency}, #{account}, #{sessionId})")
    Long insertBalance(BigDecimal amount, String currency, UUID account, String sessionId);

    @Update("update balance set amount = #{amount}, created_session_id = #{sessionId} where id = #{id}")
    void updateBalanceAmount(BigDecimal amount, Integer id, String sessionId);
}
