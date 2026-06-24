package com.example.aidemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.aidemo.cart.CartData;
import com.example.aidemo.item.ItemData;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;

import java.util.List;

@Configuration
public class JdbcConfig {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(
                new ItemDataWritingConverter(),
                new ItemDataReadingConverter(),
                new CartDataWritingConverter(),
                new CartDataReadingConverter()
        ));
    }

    @WritingConverter
    static class ItemDataWritingConverter implements Converter<ItemData, PGobject> {
        @Override
        public PGobject convert(ItemData source) {
            try {
                PGobject pgo = new PGobject();
                pgo.setType("jsonb");
                pgo.setValue(MAPPER.writeValueAsString(source));
                return pgo;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @ReadingConverter
    static class ItemDataReadingConverter implements Converter<PGobject, ItemData> {
        @Override
        public ItemData convert(PGobject source) {
            try {
                return MAPPER.readValue(source.getValue(), ItemData.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @WritingConverter
    static class CartDataWritingConverter implements Converter<CartData, PGobject> {
        @Override
        public PGobject convert(CartData source) {
            try {
                PGobject pgo = new PGobject();
                pgo.setType("jsonb");
                pgo.setValue(MAPPER.writeValueAsString(source));
                return pgo;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @ReadingConverter
    static class CartDataReadingConverter implements Converter<PGobject, CartData> {
        @Override
        public CartData convert(PGobject source) {
            try {
                return MAPPER.readValue(source.getValue(), CartData.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
