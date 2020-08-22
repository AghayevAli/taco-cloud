package tacos.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tacos.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Modifying
    @Query(value = "insert into taco_order_tacos values(?1,?2)", nativeQuery = true)
    void saveTacoToOrder(Long tacoId, Long orderId);
}
