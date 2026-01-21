package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // Criar usuário
    public User create(User user) {
        return repository.save(user);
    }

    // Listar todos os usuários
    public List<User> findAll() {
        return repository.findAll();
    }

    // Buscar usuário por ID
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id)
                );
    }

    // Deletar usuário
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
