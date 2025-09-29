package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.AdminReportDto;

import java.io.ByteArrayInputStream;

public interface AdminReportService {

    AdminReportDto generateUserReport();

    ByteArrayInputStream generateUserReportPdf();
}
