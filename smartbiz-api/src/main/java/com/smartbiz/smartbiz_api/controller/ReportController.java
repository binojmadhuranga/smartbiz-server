package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.service.ReportService;
import com.smartbiz.smartbiz_api.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final JwtUtil jwtUtil ;

    @GetMapping("/business")
    public ResponseEntity<ByteArrayResource> generateBusinessReport(HttpServletRequest request) {
        // ✅ Extract JWT token from header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);

        // ✅ Extract userId from JWT
        Long userId = jwtUtil.extractUserId(token);

        // ✅ Generate PDF for this user
        byte[] pdfBytes = reportService.generateBusinessReport(userId);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=business-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }

    // Lightweight availability check used by frontend (no body returned)
    @RequestMapping(path = "/business", method = RequestMethod.HEAD)
    public ResponseEntity<Void> businessReportAvailable() {
        return ResponseEntity.ok().build();
    }

}
