package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.Transaction;
import com.nttdata.finance_api.repository.TransactionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class TransactionReportService {

    private final TransactionRepository repository;

    public TransactionReportService(TransactionRepository repository) {
        this.repository = repository;
    }


    public byte[] generateExcelReport(Long userId) {

        List<Transaction> transactions =
                repository.findByUser_Id(userId);

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Transações");


            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Data");
            header.createCell(1).setCellValue("Categoria");
            header.createCell(2).setCellValue("Tipo");
            header.createCell(3).setCellValue("Valor");

            int rowIdx = 1;
            for (Transaction t : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(t.getDate().toString());
                row.createCell(1).setCellValue(t.getCategory().name());
                row.createCell(2).setCellValue(t.getType().name());
                row.createCell(3).setCellValue(t.getAmount().doubleValue());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }
}