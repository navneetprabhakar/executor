package com.navneet.executor.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadResponse {
    private Integer count;
    private String taskId;
    private Long timeTaken;
}
