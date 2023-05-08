package ru.javaops.topjava2.web.menu;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.javaops.topjava2.to.MenuTo;

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
            errors.rejectValue("creationDate", "error.creationDate");
        }
    }
}
