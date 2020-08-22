package tacos.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.model.Ingredient;
import tacos.model.Ingredient.Type;
import tacos.model.Order;
import tacos.model.Taco;
import tacos.model.User;
import tacos.repository.TacoRepository;
import tacos.service.IngredientService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/design")
@Slf4j
@SessionAttributes(names = "order")
public class DesignTacoController {

    private final IngredientService ingredientService;

    private final TacoRepository tacoRepository;

    @Autowired
    public DesignTacoController(IngredientService ingredientService, TacoRepository tacoRepository) {
        this.ingredientService = ingredientService;
        this.tacoRepository = tacoRepository;
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute
    public void model(Model model) {
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        Type[] values = Type.values();
        for (Type type : values) {
            List<Ingredient> o = filterByType(ingredients, type);
            model.addAttribute(type.toString().toLowerCase(), o);
        }
        model.addAttribute("taco", new Taco());
    }

    @GetMapping
    public String showDesignForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user.getFullname());
        return "design";
    }

    @PostMapping
    String processDesign(@Valid Taco taco, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        Taco saved = tacoRepository.save(taco);
        order.addDesign(saved);
        return "redirect:/orders/current";

    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        List<Ingredient> collect = ingredients.stream()
                .filter(x -> x.getType().equals(type)).collect(Collectors.toList());
        return collect;
    }

}
