package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService reportService;

    @GetMapping("/users")
    public ResponseEntity<byte[]> downloadUserReportPdf() {
        ByteArrayInputStream pdfStream = reportService.generateUserReportPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfStream.readAllBytes());
    }

}
