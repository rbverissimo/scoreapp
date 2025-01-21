package br.com.serasa.scoreapp.security.services;

import br.com.serasa.scoreapp.security.domain.Role;
import br.com.serasa.scoreapp.security.domain.User;
import br.com.serasa.scoreapp.security.dto.RegistroDto;
import br.com.serasa.scoreapp.security.repositories.RoleRepository;
import br.com.serasa.scoreapp.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public User save(RegistroDto registroDto){
        return null;
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public Optional<Role> getRoleByNome(String name){
        return roleRepository.findByName(name);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
