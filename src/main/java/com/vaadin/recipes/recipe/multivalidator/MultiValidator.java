package com.vaadin.recipes.recipe.multivalidator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use MultiValidator to show all failing validations for a field
 * at once instead of stopping at the first Validator that fails.
 * <p>
 * MultiValidator is a {@link Validator} you can use
 * to hold all the validators of a field
 * so it can then apply them all at once and
 * report back the result of all validations in one message.
 *</p>
 *
 * @param <T> The type of the value being validated.
 */
public class MultiValidator<T> extends AbstractValidator<T> {
    private final List<Validator<T>> validators;

    /**
     * Constructs a multi-validator with the given validators.
     *
     * @param validators the validators to apply
     */
    @SafeVarargs
    public MultiValidator(Validator<T>... validators) {
        super("");

        this.validators = new ArrayList<>();
        add(validators);
    }

    /**
     * Adds validators to the multi-validator.
     *
     * @param validators the validators to add
     * @return the multi-validator for chaining
     */
    @SafeVarargs
    public final MultiValidator<T> add(Validator<T>... validators) {
        this.validators.addAll(Arrays.stream(validators).toList());
        return this;
    }

    /**
     * Removes validators from the multi-validator.
     *
     * @param validators the validators to remove
     * @return the multi-validator for chaining
     */
    @SafeVarargs
    public final MultiValidator<T> remove(Validator<T>... validators) {
        this.validators.removeAll(Arrays.stream(validators).toList());
        return this;
    }

    /**
     * Removes all validators from the multi-validator.
     *
     * @return the multi-validator for chaining
     */
    public final MultiValidator<T> removeAll() {
        this.validators.clear();
        return this;
    }

    @Override
    public ValidationResult apply(T value, ValueContext context) {
        var message = validators.stream()
                .map(v -> v.apply(value, context))
                .filter(ValidationResult::isError)
                .map(ValidationResult::getErrorMessage)
                .collect(Collectors.joining(" "));
        return message.isEmpty()
                ? ValidationResult.ok()
                : ValidationResult.error(message);
    }
}
