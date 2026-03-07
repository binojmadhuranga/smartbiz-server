package com.smartbiz.smartbiz_api.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.smartbiz.smartbiz_api.dto.DashboardDto;
import com.smartbiz.smartbiz_api.exception.InternalServerException;
import com.smartbiz.smartbiz_api.service.DashboardService;
import com.smartbiz.smartbiz_api.service.ReportService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final DashboardService dashboardService;

    @Override
    public byte[] generateBusinessReport(Long userId) {

        DashboardDto dashboard = dashboardService.getDashboard(userId, "weekly");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("SmartBiz Business Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Generated Date: " + LocalDate.now()));
            document.add(new Paragraph("------------------------------------------------------"));
            document.add(Chunk.NEWLINE);

            // Content
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Weekly Sales Total: $" + dashboard.getTotalSales(), contentFont));
            document.add(new Paragraph("Customer Count: " + dashboard.getCustomerCount(), contentFont));
            document.add(new Paragraph("Supplier Count: " + dashboard.getSupplierCount(), contentFont));
            document.add(new Paragraph("Employee Count: " + dashboard.getEmployeeCount(), contentFont));

            document.close();
        } catch (Exception e) {
            throw new InternalServerException("Error generating PDF report");
        }
        return baos.toByteArray();
    }


}
