package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.exception.BusinessException;
import com.nttdata.finance_api.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;

@Service
public class UserExcelService {

    private final UserRepository repository;

    public UserExcelService(UserRepository repository) {
        this.repository = repository;
    }

    public void importUsers(MultipartFile file) {

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Pula cabeçalho
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();

                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();

                User user = new User(name, email);
                repository.save(user);
            }

        } catch (Exception e) {
            throw new BusinessException("Error importing users from Excel file");
        }
    }
}
