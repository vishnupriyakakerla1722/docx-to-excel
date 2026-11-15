package com.app;

import jakarta.persistence.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class ConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConverterApplication.class, args);
    }

    @Autowired
    private RecordRepo repo;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convert(@RequestParam("file") MultipartFile file) throws Exception {
        XWPFDocument doc = new XWPFDocument(file.getInputStream());
        Workbook workbook = new XSSFWorkbook();
        List<XWPFTable> tables = doc.getTables();

        for (int i = 0; i < tables.size(); i++) {
            Sheet sheet = workbook.createSheet("Table_" + (i + 1));
            XWPFTable table = tables.get(i);
            for (int r = 0; r < table.getRows().size(); r++) {
                Row excelRow = sheet.createRow(r);
                XWPFTableRow docRow = table.getRows().get(r);
                for (int c = 0; c < docRow.getTableCells().size(); c++) {
                    excelRow.createCell(c).setCellValue(docRow.getTableCells().get(c).getText());
                }
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        doc.close();

        ConversionRecord rec = new ConversionRecord();
        rec.setFileName(file.getOriginalFilename());
        rec.setTablesCount(tables.size());
        rec.setStatus("SUCCESS");
        repo.save(rec);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(out.toByteArray());
    }

    @GetMapping("/history")
    public List<ConversionRecord> history() {
        return repo.findAll();
    }
}

@Entity
class ConversionRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private int tablesCount;
    private String status;

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public int getTablesCount() { return tablesCount; }
    public void setTablesCount(int tablesCount) { this.tablesCount = tablesCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

interface RecordRepo extends JpaRepository<ConversionRecord, Long> {}
