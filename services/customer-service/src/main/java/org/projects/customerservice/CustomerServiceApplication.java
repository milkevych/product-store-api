package org.projects.customerservice;

import java.util.Optional;

import org.projects.customerservice.store.entity.Role;
import org.projects.customerservice.store.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final RoleRepository roleRepository) {
        return args -> {
            final Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
            if (userRole.isEmpty()) {
                final Role role = new Role();
                role.setName("ROLE_USER");
                role.setCreatedBy("APP");
                roleRepository.save(role);
            }
        };
    }

}
