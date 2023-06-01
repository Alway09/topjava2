package com.github.Alway09.RestaurantVotingApp.web.menu;

import com.github.Alway09.RestaurantVotingApp.to.MenuTo;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;

@Component
public class CreationDateValidator implements org.springframework.validation.Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MenuTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MenuTo menuTo = (MenuTo) target;
        if (menuTo.getActualDate() != null && menuTo.getActualDate().isBefore(LocalDate.now())) {
            errors.rejectValue("actualDate", "error.actualDate");
        }
    }
}
