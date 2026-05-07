package com.changarro.controller;

import com.changarro.model.Category;
import com.changarro.repository.CategoryRepository;
import com.changarro.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryController(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Category> getAll() {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if (category.getSubcategories() == null || category.getSubcategories().isEmpty()) {
                category.setSubcategories(defaultSubcategories(category.getId()));
            }
        }
        return categories;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody CreateCategoryRequest request, Authentication auth) {
        requireAdmin(auth);
        if (request == null || isBlank(request.id()) || isBlank(request.label())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id y label son requeridos");
        }

        String id = request.id().trim().toLowerCase();
        if (categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La categoria ya existe");
        }

        Category category = new Category(
            id,
            request.label().trim(),
            defaultIfBlank(request.emoji(), "🏷️"),
            defaultIfBlank(request.color(), "#DD4D2A"),
            defaultIfBlank(request.bg(), "#FFE0C2")
        );
        category.setSubcategories(new ArrayList<>());
        if (request.subcategories() != null) {
            category.setSubcategories(request.subcategories());
        }
        return categoryRepository.save(category);
    }

    @PostMapping("/{categoryId}/subcategories")
    public Category addSubcategory(@PathVariable String categoryId, @RequestBody Category.Subcategory subcategory, Authentication auth) {
        requireAdmin(auth);
        if (subcategory == null || isBlank(subcategory.getId()) || isBlank(subcategory.getLabel())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id y label de subcategoria son requeridos");
        }

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));

        List<Category.Subcategory> subs = category.getSubcategories() == null
            ? new ArrayList<>()
            : new ArrayList<>(category.getSubcategories());

        String subId = subcategory.getId().trim().toLowerCase();
        boolean exists = subs.stream().anyMatch(s -> Objects.equals(s.getId(), subId));
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La subcategoria ya existe");
        }

        Category.Subcategory clean = new Category.Subcategory(
            subId,
            subcategory.getLabel().trim(),
            defaultIfBlank(subcategory.getEmoji(), "🏷️")
        );

        subs.add(clean);
        category.setSubcategories(subs);
        return categoryRepository.save(category);
    }

    @PatchMapping("/{categoryId}/subcategories/{subcategoryId}")
    public Category updateSubcategory(
        @PathVariable String categoryId,
        @PathVariable String subcategoryId,
        @RequestBody Category.Subcategory subcategory,
        Authentication auth
    ) {
        requireAdmin(auth);
        if (subcategory == null || isBlank(subcategory.getLabel())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "label de subcategoria es requerido");
        }

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));

        List<Category.Subcategory> subs = category.getSubcategories() == null
            ? new ArrayList<>()
            : new ArrayList<>(category.getSubcategories());

        boolean updated = false;
        for (Category.Subcategory current : subs) {
            if (Objects.equals(current.getId(), subcategoryId)) {
                current.setLabel(subcategory.getLabel().trim());
                current.setEmoji(defaultIfBlank(subcategory.getEmoji(), current.getEmoji()));
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcategoria no encontrada");
        }

        category.setSubcategories(subs);
        return categoryRepository.save(category);
    }

    private List<Category.Subcategory> defaultSubcategories(String categoryId) {
        return switch (categoryId) {
            case "comida" -> List.of(
                new Category.Subcategory("tacos", "Tacos", "🌮"),
                new Category.Subcategory("tortas", "Tortas", "🥪"),
                new Category.Subcategory("pizza", "Pizza", "🍕"),
                new Category.Subcategory("hamburguesas", "Hamburguesas", "🍔"),
                new Category.Subcategory("cafe", "Cafe", "☕"),
                new Category.Subcategory("panaderia", "Panaderia", "🥐"),
                new Category.Subcategory("mariscos", "Mariscos", "🦐")
            );
            case "tienda" -> List.of(
                new Category.Subcategory("abarrotes", "Abarrotes", "🏪"),
                new Category.Subcategory("merceria", "Merceria", "🧵"),
                new Category.Subcategory("floreria", "Floreria", "💐"),
                new Category.Subcategory("tlapaleria", "Tlapaleria", "🔧")
            );
            case "servicios" -> List.of(
                new Category.Subcategory("estetica", "Estetica", "💇"),
                new Category.Subcategory("lavanderia", "Lavanderia", "🧺"),
                new Category.Subcategory("taller", "Taller", "🔩")
            );
            case "entrete" -> List.of(
                new Category.Subcategory("bar", "Bar", "🍺"),
                new Category.Subcategory("disco", "Disco", "💃"),
                new Category.Subcategory("eventos", "Eventos", "🎪")
            );
            case "salud" -> List.of(
                new Category.Subcategory("farmacia", "Farmacia", "💊"),
                new Category.Subcategory("consultorio", "Consultorio", "🩺"),
                new Category.Subcategory("dentista", "Dentista", "🦷")
            );
            default -> new ArrayList<>();
        };
    }

    private void requireAdmin(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        String userId = String.valueOf(auth.getPrincipal());
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo administradores");
        }
    }

    private String defaultIfBlank(String value, String fallback) {
        return isBlank(value) ? fallback : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private record CreateCategoryRequest(
        String id,
        String label,
        String emoji,
        String color,
        String bg,
        List<Category.Subcategory> subcategories
    ) {}
}
