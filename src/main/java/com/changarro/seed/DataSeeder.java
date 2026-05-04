package com.changarro.seed;

import com.changarro.model.*;
import com.changarro.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepo;
    private final BusinessRepository businessRepo;
    private final StampRepository stampRepo;
    private final RewardRepository rewardRepo;
    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(CategoryRepository categoryRepo, BusinessRepository businessRepo,
                      StampRepository stampRepo, RewardRepository rewardRepo,
                      ReviewRepository reviewRepo, UserRepository userRepo,
                      PasswordEncoder passwordEncoder) {
        this.categoryRepo = categoryRepo;
        this.businessRepo = businessRepo;
        this.stampRepo = stampRepo;
        this.rewardRepo = rewardRepo;
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Only seed if DB is empty
        if (categoryRepo.count() > 0) {
            System.out.println("[Seeder] Base de datos ya tiene datos, saltando seed.");
            return;
        }
        System.out.println("[Seeder] Sembrando datos iniciales...");

        seedCategories();
        seedStamps();
        List<Business> businesses = seedBusinesses();
        seedRewards(businesses);
        User demoUser = seedDemoUser();
        seedReviews(businesses, demoUser);

        System.out.println("[Seeder] Datos iniciales listos.");
    }

    private void seedCategories() {
        categoryRepo.saveAll(List.of(
            new Category("comida",    "Comida",    "🌮", "#DD4D2A", "#FFE0C2"),
            new Category("tienda",    "Tienda",    "🛍️", "#4A8A3A", "#D4ECC2"),
            new Category("servicios", "Servicios", "🔧", "#2B6FA0", "#C2E0EC"),
            new Category("entrete",   "Diversion", "🎉", "#E8628E", "#FFD0DA"),
            new Category("salud",     "Salud",     "⚕️",  "#7A3FA8", "#E8C8DD")
        ));
    }

    private void seedStamps() {
        stampRepo.saveAll(List.of(
            new Stamp("taquero",    "Taquero",    "🌮", "#DD4D2A", "Visita 5 taquerias"),
            new Stamp("cafeicon",   "Cafeicon",   "☕",       "#C68A52", "Visita 3 cafes"),
            new Stamp("bohemio",    "Bohemio",    "🎸", "#7A3FA8", "Visita 3 bares o cantinas"),
            new Stamp("mananero",   "Mananero",   "🌅", "#F5B92E", "Visita antes de las 9am"),
            new Stamp("vecino",     "Vecino",     "🏘️", "#4A8A3A", "Visita 10 negocios diferentes"),
            new Stamp("madrugador", "Madrugador", "🐓", "#2B6FA0", "Haz 5 check-ins antes de las 8am"),
            new Stamp("curandero",  "Curandero",  "🌿", "#7A3FA8", "Visita 3 farmacias o consultorios"),
            new Stamp("bailarin",   "Bailarin",   "💃", "#E8628E", "Visita 3 lugares de entretenimiento")
        ));
    }

    private List<Business> seedBusinesses() {
        Business b1 = biz("Tacos Don Juan", "comida", "Al carbon desde 1987",
                "Av. Alvaro Obregon 145", "Roma Norte", "55 1234 5678", "@tacosdonjuan",
                "Solo efectivo", 4.8, 234, 1200, "🌮", "#FFB57A",
                List.of("Pastor", "Suadero"), "Tacos al pastor", "$60-120", "Lun-Dom 12:00-23:00",
                true, false);
        b1.setActivePromo(new Business.Promo("2x1 al pastor", "2x1", "HOY HASTA 11PM", "Menciona Changarro al pedir", 50));
        b1.setMenuItems(List.of(
            new Business.MenuItem("Pastor", "🌮", "$25", 142),
            new Business.MenuItem("Suadero", "🌯", "$22", 89),
            new Business.MenuItem("Campechano", "🥙", "$28", 67),
            new Business.MenuItem("Quesadillas", "🫓", "$35", 45)
        ));

        Business b2 = biz("Merceria Lupita", "tienda", "Todo para tus proyectos de costura",
                "Calle Durango 78", "Roma Norte", "55 2345 6789", "@mercerialupita",
                "Efectivo y tarjeta", 4.6, 89, 450, "🧵", "#A8D08B",
                List.of("Hilos", "Botones"), "Hilos y botones", "$30-200", "Lun-Sab 9:00-19:00",
                false, false);

        Business b3 = biz("Estetica Rocio", "servicios", "Corte, tinte y tratamientos capilares",
                "Calle Tabasco 210", "Roma Norte", "55 3456 7890", "@esteticarocio",
                "Efectivo y tarjeta", 4.9, 156, 800, "💇", "#8FC4DC",
                List.of("Corte", "Tinte"), "Corte y tinte", "$150-500", "Mar-Dom 10:00-20:00",
                false, false);

        Business b4 = biz("Cafe Avellaneda", "comida", "Cafe de especialidad de Oaxaca",
                "Calle Orizaba 32", "Roma Norte", "55 4567 8901", "@cafeavellaneda",
                "Efectivo y tarjeta", 4.7, 412, 2100, "☕", "#C68A52",
                List.of("Especialidad"), "Especialidad", "$80-180", "Lun-Dom 7:00-22:00",
                true, false);

        Business b5 = biz("Disco La Cumbia", "entrete", "El mejor lugar para bailar salsa y cumbia",
                "Calle Puebla 55", "Roma Norte", "55 5678 9012", "@discolacumbia",
                "Efectivo y tarjeta", 4.4, 78, 600, "💃", "#D87FA0",
                List.of("Salsa", "Bachata"), "Salsa y cumbia", "$100-300", "Jue-Dom 20:00-3:00",
                false, false);

        Business b6 = biz("Farmacia 24h", "salud", "Medicamentos y productos de salud las 24 horas",
                "Av. Insurgentes Sur 320", "Roma Norte", "55 6789 0123", null,
                "Efectivo y tarjeta", 4.5, 203, 3500, "💊", "#C99FD9",
                List.of("Abierta"), "Abierto 24h", "$20-500", "24 horas",
                false, false);

        Business b7 = biz("Floreria Camelia", "tienda", "Flores frescas para toda ocasion",
                "Calle Colima 123", "Roma Norte", "55 7890 1234", "@floreriacamelia",
                "Efectivo y tarjeta", 5.0, 67, 350, "💐", "#F5A8B8",
                List.of("Ramos"), "Flores frescas", "$100-800", "Lun-Sab 8:00-20:00",
                false, true);

        Business b8 = biz("Panaderia Sol", "comida", "Pan artesanal horneado cada manana",
                "Calle Merida 88", "Roma Norte", "55 8901 2345", "@panaderiasol",
                "Solo efectivo", 4.8, 521, 4200, "🥖", "#E8B860",
                List.of("Conchas"), "Pan artesanal", "$15-80", "Lun-Dom 6:00-21:00",
                false, false);
        b8.setActivePromo(new Business.Promo("20% en conchas", "−20%", "HOY TODO EL DIA", "Compra 6 o mas", 30));

        Business b9 = biz("Cantina Don Beto", "entrete", "Cervezas artesanales y botanas de la casa",
                "Calle Jalapa 45", "Roma Norte", "55 9012 3456", "@cantinadonbeto",
                "Efectivo y tarjeta", 4.7, 312, 1800, "🍺", "#C9A06B",
                List.of("Cervezas"), "Cervezas y botanas", "$80-250", "Lun-Dom 13:00-1:00",
                false, false);

        Business b10 = biz("Tlapaleria El Sol", "tienda", "Pinturas, herramientas y material electrico",
                "Calle Queretaro 90", "Roma Norte", "55 0123 4567", null,
                "Solo efectivo", 4.5, 95, 700, "🔧", "#D9A87A",
                List.of("Pinturas"), "Pinturas y herramientas", "$20-2000", "Lun-Sab 8:00-19:00",
                false, false);

        Business b11 = biz("Dr. Mendoza", "salud", "Medicina general con mas de 20 anos de experiencia",
                "Calle Tonala 156", "Roma Norte", "55 1234 0000", null,
                "Efectivo y tarjeta", 4.9, 188, 950, "🩺", "#B8C4DC",
                List.of("General"), "Medicina general", "$300-600", "Lun-Vie 9:00-18:00",
                false, false);

        Business b12 = biz("Lavanderia Express", "servicios", "Lavado y planchado el mismo dia",
                "Calle Zacatecas 67", "Roma Norte", "55 2345 0000", "@lavanderiaexpress",
                "Efectivo y tarjeta", 4.6, 142, 1100, "🧺", "#9FCDD9",
                List.of("Mismo dia"), "Mismo dia", "$40-200", "Lun-Sab 7:00-20:00",
                false, false);

        return businessRepo.saveAll(List.of(b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12));
    }

    private void seedRewards(List<Business> businesses) {
        rewardRepo.saveAll(List.of(
            new Reward("50% en Cafe Avellaneda", 800, "☕", "#C68A52", businesses.get(3).getId()),
            new Reward("Conchas gratis Pan Sol", 400, "🥖", "#E8B860", businesses.get(7).getId()),
            new Reward("Manicure en Rocio", 1200, "💅", "#E8628E", businesses.get(2).getId())
        ));
    }

    private User seedDemoUser() {
        User user = new User("Maria Hernandez", "maria@changarro.com", passwordEncoder.encode("demo123"));
        user.setCoins(1240);
        user.setLevel(4);
        user.setLevelName("Embajadora");
        user.setXp(520);
        user.setAvatarEmoji("😊");
        user.setStampIds(List.of("taquero", "cafeicon", "bohemio", "mananero"));
        return userRepo.save(user);
    }

    private void seedReviews(List<Business> businesses, User user) {
        String tacosId = businesses.get(0).getId();

        Review r1 = new Review();
        r1.setBusinessId(tacosId);
        r1.setUserId(user.getId());
        r1.setUserName("Maria G.");
        r1.setUserLevel("Embajadora");
        r1.setUserEmoji("👑");
        r1.setRating(5);
        r1.setText("Los mejores del barrio, neta. Don Juan siempre con la mejor onda y las salsas de la casa son top.");
        r1.setLikes(42);

        Review r2 = new Review();
        r2.setBusinessId(tacosId);
        r2.setUserId("guest");
        r2.setUserName("Roberto L.");
        r2.setUserLevel("Explorador");
        r2.setUserEmoji("🧭");
        r2.setRating(4);
        r2.setText("Sabor autentico al carbon. Llega temprano porque se llena rapidisimo el martes.");
        r2.setLikes(28);

        reviewRepo.saveAll(List.of(r1, r2));
    }

    private Business biz(String name, String catId, String desc, String address,
                          String neighborhood, String phone, String instagram,
                          String payment, double rating, int reviews, int visits,
                          String emoji, String color, List<String> tags, String tag,
                          String priceRange, String schedule, boolean trending, boolean nuevo) {
        Business b = new Business();
        b.setName(name);
        b.setCategoryId(catId);
        b.setDescription(desc);
        b.setAddress(address);
        b.setNeighborhood(neighborhood);
        b.setPhone(phone);
        b.setInstagram(instagram);
        b.setPaymentMethod(payment);
        b.setRating(rating);
        b.setReviewCount(reviews);
        b.setVisitCount(visits);
        b.setEmoji(emoji);
        b.setColor(color);
        b.setTags(tags);
        b.setTag(tag);
        b.setPriceRange(priceRange);
        b.setSchedule(schedule);
        b.setTrending(trending);
        b.setNuevo(nuevo);
        return b;
    }
}
