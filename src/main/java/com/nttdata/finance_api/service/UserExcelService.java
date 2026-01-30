package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.Role;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;

@Service
public class UserExcelService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserExcelService(
            UserRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void importUsers(MultipartFile file) {

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(inputStream)
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();


            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();

                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();

                String encodedPassword =
                        passwordEncoder.encode("123456");

                User user = new User(
                        name,
                        email,
                        encodedPassword,
                        Role.USER
                );

                repository.save(user);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar usuários do Excel", e);
        }
    }
}
