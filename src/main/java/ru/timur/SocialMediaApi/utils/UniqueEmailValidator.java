package ru.timur.SocialMediaApi.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.timur.SocialMediaApi.dto.UniqueEmail;
import ru.timur.SocialMediaApi.repositoris.PeopleRepository;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final PeopleRepository peopleRepository;

    @Autowired
    public UniqueEmailValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && !peopleRepository.existsByEmail(email);
    }
}
