package tacos.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import tacos.model.Order;
import tacos.model.User;
import tacos.repository.OrderRepository;
import tacos.repository.UserRepository;
import tacos.service.OrderService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/current")
    public String orderForm(Model model, @AuthenticationPrincipal User user) {
        Order order = new Order();
        if (Objects.nonNull(user)) {
            order.setName(user.getUsername());
            order.setStreet(user.getStreet());
            order.setState(user.getState());
            order.setCity(user.getCity());
            order.setZip(user.getZip());
        }
        model.addAttribute("order", order);
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        order.setUser(user);
        orderService.saveOrder(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
