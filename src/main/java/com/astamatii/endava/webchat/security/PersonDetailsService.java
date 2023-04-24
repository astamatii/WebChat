package com.astamatii.endava.webchat.security;

import com.astamatii.endava.webchat.models.Person;
import com.astamatii.endava.webchat.repositories.PersonRepository;
import com.astamatii.endava.webchat.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<Person> userOptional = personRepository.findByUsername(usernameOrEmail);

        if(userOptional.isEmpty())
            userOptional = personRepository.findByEmail(usernameOrEmail);
        if(userOptional.isEmpty())
            throw new UsernameNotFoundException("User by this username or email not found");

        Person user = userOptional.get();
        user.setLastLoggedInAt(ZonedDateTime.now());
        personRepository.save(user);

        return new PersonDetails(user);
    }
}
