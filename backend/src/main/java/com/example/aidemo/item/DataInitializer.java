package com.example.aidemo.item;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner seedItems(ItemRepository repository) {
        return args -> {
            if (repository.count() > 0) return;

            repository.saveAll(List.of(
                    new Item(new ItemData(
                            "Wireless Noise-Cancelling Headphones",
                            "Premium over-ear headphones with 40h battery life, active noise cancellation, and Hi-Res audio support. Foldable design for easy travel.",
                            new BigDecimal("249.99"), "Electronics",
                            "https://picsum.photos/seed/headphones/600/400", 18)),
                    new Item(new ItemData(
                            "Mechanical Keyboard",
                            "Compact TKL mechanical keyboard with Cherry MX Brown switches, RGB backlighting, and aircraft-grade aluminium frame.",
                            new BigDecimal("129.99"), "Electronics",
                            "https://picsum.photos/seed/keyboard/600/400", 34)),
                    new Item(new ItemData(
                            "4K Webcam",
                            "Ultra-sharp 4K webcam with auto-focus, built-in noise-cancelling mic array, and wide-angle 90° lens. Plug-and-play USB-C.",
                            new BigDecimal("89.99"), "Electronics",
                            "https://picsum.photos/seed/webcam/600/400", 27)),
                    new Item(new ItemData(
                            "Running Shoes",
                            "Lightweight trail running shoes with responsive foam midsole, breathable mesh upper, and all-terrain rubber outsole.",
                            new BigDecimal("119.99"), "Footwear",
                            "https://picsum.photos/seed/shoes/600/400", 42)),
                    new Item(new ItemData(
                            "Merino Wool Hoodie",
                            "100% extra-fine merino wool hoodie. Naturally temperature-regulating, odour-resistant, and machine washable. Available in 6 colours.",
                            new BigDecimal("149.99"), "Clothing",
                            "https://picsum.photos/seed/hoodie/600/400", 15)),
                    new Item(new ItemData(
                            "Cargo Pants",
                            "Durable ripstop cargo pants with 8 pockets, articulated knees, and water-repellent DWR coating. Great for hiking or city wear.",
                            new BigDecimal("79.99"), "Clothing",
                            "https://picsum.photos/seed/cargo/600/400", 31)),
                    new Item(new ItemData(
                            "Pour-Over Coffee Set",
                            "Barista-quality pour-over kit including borosilicate glass dripper, gooseneck kettle, precision scale, and 50 unbleached filters.",
                            new BigDecimal("64.99"), "Kitchen",
                            "https://picsum.photos/seed/coffee/600/400", 22)),
                    new Item(new ItemData(
                            "Cast Iron Skillet",
                            "Pre-seasoned 12\" cast iron skillet. Works on all cooktops including induction. Oven-safe to 500°C. Lasts a lifetime.",
                            new BigDecimal("44.99"), "Kitchen",
                            "https://picsum.photos/seed/skillet/600/400", 19)),
                    new Item(new ItemData(
                            "Stainless Steel Water Bottle",
                            "Double-wall vacuum insulated 750ml bottle. Keeps drinks cold 24h or hot 12h. Leak-proof lid, BPA-free, dishwasher safe.",
                            new BigDecimal("34.99"), "Kitchen",
                            "https://picsum.photos/seed/bottle/600/400", 60)),
                    new Item(new ItemData(
                            "The Pragmatic Programmer",
                            "20th anniversary edition of the landmark book on software craftsmanship. Essential reading for developers at any stage of their career.",
                            new BigDecimal("49.99"), "Books",
                            "https://picsum.photos/seed/pragprog/600/400", 11)),
                    new Item(new ItemData(
                            "Designing Data-Intensive Applications",
                            "Deep-dive into the principles behind reliable, scalable, and maintainable systems. The go-to reference for backend and data engineers.",
                            new BigDecimal("54.99"), "Books",
                            "https://picsum.photos/seed/ddia/600/400", 8)),
                    new Item(new ItemData(
                            "Portable SSD 2TB",
                            "USB-C 3.2 Gen2 portable SSD with up to 1050 MB/s read speed. Shock-resistant, IP55 rated, weighs only 43g.",
                            new BigDecimal("139.99"), "Electronics",
                            "https://picsum.photos/seed/ssd/600/400", 25))
            ));
        };
    }
}
