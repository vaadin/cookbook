package com.vaadin.recipes.recipe.binderautoapplycustomconverter;

import java.time.Duration;
import java.util.Optional;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.ConverterFactory;
import com.vaadin.flow.data.converter.DefaultConverterFactory;

public class CustomBinder<BEAN> extends Binder<BEAN> {

    private final CustomConverterFactory converterFactory = new CustomConverterFactory();

    public CustomBinder(Class<BEAN> beanType) {
        super(beanType);
    }

    @Override
    protected ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    /**
     * A custom converter factory that handles String -> Duration conversion
     * and delegates to default converter factory for every other types.
     */
    private static class CustomConverterFactory implements ConverterFactory {

        @SuppressWarnings("unchecked")
        @Override
        public <P, M> Optional<Converter<P, M>> newInstance(
                Class<P> presentationType, Class<M> modelType) {
            if (String.class == presentationType
                    && Duration.class == modelType) {
                return Optional.of((Converter<P, M>) DurationConverter.INSTANCE);
            }
            return DefaultConverterFactory.INSTANCE
                    .newInstance(presentationType, modelType);
        }

    }

    /**
     * Converter that handles string representation of Duration.
     *
     * Duration can be expressed as a string in format {@literal Nh Nm Ns} where
     * N is a numeric value and {@literal h, m, s} stands for hours, minutes and
     * seconds.
     *
     * Examples of string representations are {@literal 1h 23m 45s} or
     * {@literal 97m}
     */
    private static class DurationConverter
            implements Converter<String, Duration> {

        private static final DurationConverter INSTANCE = new DurationConverter();

        @Override
        public Result<Duration> convertToModel(String value,
                                               ValueContext context) {
            if (value == null || value.isBlank()) {
                return Result.ok(Duration.ZERO);
            }
            try {
                return Result.ok(Duration.parse("PT" + value.replace(" ", "")));
            } catch (Exception ex) {
                return Result.error(ex.getMessage());
            }
        }

        @Override
        public String convertToPresentation(Duration value,
                                            ValueContext context) {
            if (value == null || value.isZero()) {
                return "";
            }
            return value.toString().substring(2).replaceAll("([HMS])", "$1 ")
                    .toLowerCase().trim();
        }
    }

}
