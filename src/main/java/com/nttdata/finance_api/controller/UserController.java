package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.service.UserExcelService;
import com.nttdata.finance_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserExcelService excelService;

    public UserController(UserService userService, UserExcelService excelService) {
        this.userService = userService;
        this.excelService = excelService;
    }


    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }


    @GetMapping
    public List<User> list() {
        return userService.findAll();
    }


    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }


    @PostMapping(
            value = "/upload",
            consumes = "multipart/form-data"
    )
    public String uploadUsers(@RequestParam("file") MultipartFile file) {
        excelService.importUsers(file);
        return "Usuários importados com sucesso";
    }

}
