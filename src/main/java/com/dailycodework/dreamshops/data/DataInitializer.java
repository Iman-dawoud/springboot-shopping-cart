package com.dailycodework.dreamshops.data;

import com.dailycodework.dreamshops.model.Role;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");

        createDefaultUserIfNotExists();
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultAdminIfNotExists();
    }


    //this method well create 5 user when the application is run so we are going to have default users in database so we can actually test without wasting much time
    public void createDefaultUserIfNotExists(){
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for(int i=0 ; i < 5 ; i++) {
            String defaultEmail = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default vet user " + i + "created successfully");
        }

    }



    public void createDefaultAdminIfNotExists() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 0; i < 5; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + "created successfully");
        }
    }
    private void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream().filter(role -> roleRepository.findByName(role).isEmpty()) // Keep roles that are not in DB
                .map(Role :: new) //Role::new is a constructor reference, which is shorthand for: role -> new Role(role)
                .forEach(roleRepository::save); // Save each new Role object in the database

        //map fn explanation: Role::new is a constructor reference, which is shorthand for: role -> new Role(role)
    }
}
