package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.PaymentDto;
import com.smartbiz.smartbiz_api.entity.Payment;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.exception.BadRequestException;
import com.smartbiz.smartbiz_api.exception.FileStorageException;
import com.smartbiz.smartbiz_api.exception.NotFoundException;
import com.smartbiz.smartbiz_api.repo.PaymentRepo;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final UserRepo userRepo;

    @Value("${file.upload.dir:uploads/payments}")
    private String uploadDir;

    @Override
    public PaymentDto uploadPayment(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("No file uploaded");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        try {
            Path storageRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(storageRoot);

            String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
            String ext = "";
            int dot = originalFilename.lastIndexOf('.');
            if (dot >= 0) {
                ext = originalFilename.substring(dot);
            }
            String storedFilename = UUID.randomUUID() + ext;
            Path target = storageRoot.resolve(storedFilename);

            // Save the file
            file.transferTo(target);

            Payment payment = Payment.builder()
                    .user(user)
                    .originalFilename(originalFilename)
                    .storedFilename(storedFilename)
                    .storagePath(target.toString())
                    .contentType(file.getContentType())
                    .sizeBytes(file.getSize())
                    .uploadedAt(LocalDateTime.now())
                    .build();

            payment = paymentRepo.save(payment);

            return PaymentDto.builder()
                    .id(payment.getId())
                    .userId(user.getId())
                    .originalFilename(payment.getOriginalFilename())
                    .storedFilename(payment.getStoredFilename())
                    .storagePath(payment.getStoragePath())
                    .contentType(payment.getContentType())
                    .sizeBytes(payment.getSizeBytes())
                    .uploadedAt(payment.getUploadedAt())
                    .build();

        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Payment getPaymentById(Long paymentId) {
        return paymentRepo.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found"));
    }


    @Override
    public byte[] downloadPaymentFile(Long paymentId) throws Exception {
        Payment payment = getPaymentById(paymentId);
        Path filePath = Path.of(payment.getStoragePath());
        if (!Files.exists(filePath)) {
            throw new NotFoundException("File not found on server");
        }
        return Files.readAllBytes(filePath);
    }

    @Override
    public Payment getLatestPaymentByUserId(Long userId) {
        return paymentRepo.findTopByUser_IdOrderByUploadedAtDesc(userId)
                .orElseThrow(() -> new NotFoundException("No payment slip found for this user"));
    }



}

