package com.astamatii.endava.webchat.controllers;

import com.astamatii.endava.webchat.dto.ProfileDto;
import com.astamatii.endava.webchat.dto.helpers.ProfileDtoNotBlankNotNullFlags;
import com.astamatii.endava.webchat.models.Person;
import com.astamatii.endava.webchat.security.PersonDetailsService;
import com.astamatii.endava.webchat.services.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final PersonService personService;
    private final PersonDetailsService personDetailsService;
    private final ModelMapper modelMapper;

    @GetMapping
    public String profilePage(Model model) {
        Person currentUser = personService.findUserByUsername(personDetailsService.getCurrentUsername());
        model.addAttribute("user", currentUser);

        return "profile/profile";
    }

    @GetMapping("/edit_profile")
    public String editProfilePage(Model model) {
        Person currentUser = personService.findUserByUsername(personDetailsService.getCurrentUsername());
        ProfileDto user = modelMapper.map(currentUser, ProfileDto.class);
        model.addAttribute("user", user);

        //This object will rule which error field in edit_profile to show, if exist (because by default BindingResult holds everything)
        model.addAttribute("errorFlags", new ProfileDtoNotBlankNotNullFlags());

        return "profile/edit_profile";
    }

    @PostMapping("/edit_profile")
    public String updateProfile(@ModelAttribute("user") @Valid ProfileDto profileDto,
                                BindingResult bindingResult, Model model) {

        Person currentUser = personService.findUserByUsername(personDetailsService.getCurrentUsername());

        // Profile flags are without errors. True - means fields were changed, False - fields will be ignored.
        ProfileDtoNotBlankNotNullFlags profileDtoNotBlankNotNullFlags = personService.profileDtoNotBlankNotNullFlags(profileDto, currentUser);

        // Handling Validation. In order to update profile ignoring empty fields
        // Error Flags: True - means having errors, False - no errors.
        ProfileDtoNotBlankNotNullFlags errorFlags = profileDtoNotBlankNotNullFlags.getErrorFlags(bindingResult);

        model.addAttribute("errorFlags", errorFlags);

        if (errorFlags.findAnyError())
            return "profile/edit_profile";

        if (profileDtoNotBlankNotNullFlags.isAnyChangedCredentials()) {
            if (personService.passwordCheck(profileDto.getPasswordCheck(), currentUser.getPassword())) {
                Person updatedUser = personService.prepareUpdatedUser(profileDto, currentUser, profileDtoNotBlankNotNullFlags);
                personDetailsService.updateUserDetails(personService.updateUser(updatedUser));
                return "redirect:/profile";
            } else {
                bindingResult.rejectValue("passwordCheck", "", "Password Check failed");
                return "profile/edit_profile";
            }
        }

        //When username, email or password are blank or unchanged, the update process will begin without password check.
        Person updatedUser = personService.prepareUpdatedUser(profileDto, currentUser, profileDtoNotBlankNotNullFlags);
       personService.updateUser(updatedUser);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteProfile(@ModelAttribute("user") ProfileDto profileDto,
                                BindingResult bindingResult) {
        Person currentUser = personService.findUserByUsername(personDetailsService.getCurrentUsername());

        if (personService.passwordCheck(profileDto.getPasswordCheck(), currentUser.getPassword())) {

            personService.deleteUser(currentUser);
            return "redirect:/logout";
        }
        bindingResult.rejectValue("passwordCheck", "", "Password Check Failed");
        return "profile/edit_profile";
    }

}
