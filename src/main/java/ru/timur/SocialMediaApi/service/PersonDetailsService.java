package ru.timur.SocialMediaApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timur.SocialMediaApi.models.Person;
import ru.timur.SocialMediaApi.repositoris.PeopleRepository;
import ru.timur.SocialMediaApi.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);
        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }

    public Person findByUsername(String username){
        return peopleRepository.findByUsername(username).get();
    }

    public Person findByPersonId(int id){
        return peopleRepository.findById(id).get();
    }

    @Transactional
    public void edit(Person person){
        peopleRepository.save(person);
    }
}
