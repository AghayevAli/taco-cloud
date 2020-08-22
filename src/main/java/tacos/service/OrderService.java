package tacos.service;

import org.springframework.data.repository.CrudRepository;
import tacos.model.Order;

public interface OrderService {
    void saveOrder(Order order);
}
