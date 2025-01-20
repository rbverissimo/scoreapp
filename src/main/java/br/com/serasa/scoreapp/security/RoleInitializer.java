package br.com.serasa.scoreapp.security;

import br.com.serasa.scoreapp.security.domain.Role;
import br.com.serasa.scoreapp.security.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RoleInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    private void init(){
        if(!roleRepository.existsByName("ADMIN")){
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if(!roleRepository.existsByName("USER")){
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

    }
}
