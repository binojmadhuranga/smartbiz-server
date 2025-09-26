package com.smartbiz.smartbiz_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    // Original file name provided by the client
    @Column(nullable = false)
    private String originalFilename;

    // The stored file name (e.g., UUID + extension)
    @Column(nullable = false)
    private String storedFilename;

    // Absolute or relative storage path where the file is saved
    @Column(nullable = false, length = 1024)
    private String storagePath;

    private String contentType;

    private long sizeBytes;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;
}

