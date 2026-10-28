package com.algaworks.algashop.product.catalog.presentation;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CaseInsensitiveStringToEnumConverterFactory());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static class CaseInsensitiveStringToEnumConverterFactory implements ConverterFactory<String, Enum> {

        @Override
        public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
            return source -> {
                String value = source.trim();
                if (value.isEmpty()) {
                    return null;
                }
                for (T constant : targetType.getEnumConstants()) {
                    if (constant.name().equalsIgnoreCase(value)) {
                        return constant;
                    }
                }
                throw new IllegalArgumentException(
                        "No enum constant " + targetType.getCanonicalName() + " for value " + source);
            };
        }
    }
}
