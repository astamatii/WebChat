package com.astamatii.endava.webchat.services;

import com.astamatii.endava.webchat.models.Person;
import com.astamatii.endava.webchat.repositories.PersonRepository;
import com.astamatii.endava.webchat.utils.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Person findById(Long id){
        Optional<Person> personOptional = personRepository.findById(id);
        return personOptional.orElseThrow(PersonIdNotFoundException::new);
    }

    @Transactional
    public Person findByUsername(String username){
        Optional<Person> personOptional = personRepository.findByUsername(username);
        return personOptional.orElseThrow(PersonUsernameNotFoundException::new);
    }

    @Transactional
    public Person findByEmail(String email){
        Optional<Person> personOptional = personRepository.findByEmail(email);
        return personOptional.orElseThrow(PersonEmailNotFoundException::new);
    }

    @Transactional
    public void updateProfile(Person person){
//        Person updatedPerson = findById(person.getId());
////        updatedPerson.setLanguage(person.getLanguage());
////        updatedPerson.setCity(person.getCity());
////        updatedPerson.setTextColor(person.getTextColor());
////        updatedPerson.setDob(person.getDob());
////        updatedPerson.setName(person.getName());
        personRepository.save(person);
    }

    @Transactional
    public void register(Person person) {
        if(verifyUsernameExistence(person))
            throw new PersonUsernameExistsException();
        if(verifyEmailExistence(person))
            throw new PersonEmailExistsException();

        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        personRepository.save(person);
    }

    public void deletePerson(Person person) {
        findById(person.getId());
        personRepository.delete(person);
    }

    private boolean verifyUsernameExistence(Person person){
        return personRepository.findByUsername(person.getUsername()).isPresent();
    }

    private boolean verifyEmailExistence(Person person){
        return personRepository.findByEmail(person.getEmail()).isPresent();
    }
//    @Transactional
//    public void updateProfileByValues(Long id, String name, LocalDate dob, Long languageId,
//                                      Long cityId, String textColor){
//        Person updatedPerson = findById(id);
//        personRepository.updateProfileByIdWithValues(id, name, dob, languageId, cityId, textColor);
//    }
//
//    @Transactional
//    public void updateProfileByPerson(Person person){
//        Person updatedPerson = findById(person.getId());
//        personRepository.updateProfileByIdWithPersonValues(person.getId(), person.getName(),person.getDob(),person.getLanguage(),
//                person.getCity(), person.getTextColor());
//    }
//
//    @Transactional
//    public void updateEmail(Long id, String email){
//        findById(id);
//        personRepository.updateEmailById(id, email);
//    }
//
//    public void updateUsername(Long id, String username){
//        findById(id);
//        personRepository.updateUsernameById(id, username);
//    }
//
//    @Transactional
//    public void updatePassword(Long id, String password) {
//        findById(id);
//        String encodedPassword = passwordEncoder.encode(password);
//
//        personRepository.updatePasswordById(id, encodedPassword);
//    }
}
