package com.smartbiz.smartbiz_api.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.smartbiz.smartbiz_api.dto.AdminReportDto;
import com.smartbiz.smartbiz_api.entity.PlanType;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {

    private final UserRepo userRepository;

    @Override
    public AdminReportDto generateUserReport() {
        long total = userRepository.count();
        long normal = userRepository.countByPlan(PlanType.NORMAL);
        long pro = userRepository.countByPlan(PlanType.PRO);

        return new AdminReportDto(total, normal, pro);
    }

    @Override
    public ByteArrayInputStream generateUserReportPdf() {
        AdminReportDto report = generateUserReport();

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("User Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            // Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            PdfPCell header1 = new PdfPCell(new Phrase("Metric"));
            PdfPCell header2 = new PdfPCell(new Phrase("Value"));
            table.addCell(header1);
            table.addCell(header2);

            table.addCell("Total Users");
            table.addCell(String.valueOf(report.getTotalUsers()));

            table.addCell("Normal Users");
            table.addCell(String.valueOf(report.getNormalUsers()));

            table.addCell("Pro Users");
            table.addCell(String.valueOf(report.getProUsers()));

            document.add(table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
