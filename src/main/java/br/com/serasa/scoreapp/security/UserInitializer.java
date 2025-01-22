package br.com.serasa.scoreapp.security;

import br.com.serasa.scoreapp.security.domain.Role;
import br.com.serasa.scoreapp.security.domain.User;
import br.com.serasa.scoreapp.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.HashSet;

@Component
public class UserInitializer {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    private void init(){
        Role adminRole = userService.getRoleByNome("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role do usuário não encontrado: ADMIN"));
        if(userService.getUserByEmail("admin@serasa.com.br").isPresent()) return;

        User admin = new User();
        admin.setEmail("admin@serasa.com.br");
        admin.setPassword(passwordEncoder.encode("admin@s12"));
        admin.setRoles(new HashSet<>(){{ add(adminRole); }});
        userService.save(admin);

        Role userRole = userService.getRoleByNome("USER")
                .orElseThrow(() -> new RuntimeException("Role do usuário não encontrado: USER"));

        if(userService.getUserByEmail("user@serasa.com.br").isPresent()) return;

        User user = new User();
        user.setEmail("user@serasa.com.br");
        user.setPassword(passwordEncoder.encode("user@s12"));
        user.setRoles(new HashSet<>(){{ add(userRole); }});
        userService.save(user);

    }

}
