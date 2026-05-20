package com.navneet.executor.controller;

import com.navneet.executor.models.ServiceRequest;
import com.navneet.executor.models.TaskStatus;
import com.navneet.executor.models.UploadRequest;
import com.navneet.executor.models.UploadResponse;
import com.navneet.executor.service.UploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaskController
 * Tests cover happy path and error cases for all public endpoints
 */
@DisplayName("TaskController Tests")
class TaskControllerTest {

    @Mock
    private UploadService uploadService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== uploadCsvSingleThreaded Tests ====================

    @Test
    @DisplayName("uploadCsvSingleThreaded - Happy Path: Should return UploadResponse with valid data")
    void testUploadCsvSingleThreadedSuccess() {
        // Arrange
        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setPath("/path/to/file.csv");

        ServiceRequest<UploadRequest> serviceRequest = new ServiceRequest<>();
        serviceRequest.setData(uploadRequest);

        UploadResponse expectedResponse = UploadResponse.builder()
                .count(100)
                .taskId("task-123")
                .timeTaken(5000L)
                .build();

        when(uploadService.uploadCsvSingleThreaded(any(UploadRequest.class)))
                .thenReturn(expectedResponse);

        // Act
        UploadResponse actualResponse = taskController.uploadCsvSingleThreaded(serviceRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(100, actualResponse.getCount());
        assertEquals("task-123", actualResponse.getTaskId());
        assertEquals(5000L, actualResponse.getTimeTaken());
        verify(uploadService, times(1)).uploadCsvSingleThreaded(any(UploadRequest.class));
    }

    @Test
    @DisplayName("uploadCsvSingleThreaded - Error Case: Should handle null response from service")
    void testUploadCsvSingleThreadedNullResponse() {
        // Arrange
        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setPath("/invalid/path.csv");

        ServiceRequest<UploadRequest> serviceRequest = new ServiceRequest<>();
        serviceRequest.setData(uploadRequest);

        when(uploadService.uploadCsvSingleThreaded(any(UploadRequest.class)))
                .thenReturn(null);

        // Act
        UploadResponse actualResponse = taskController.uploadCsvSingleThreaded(serviceRequest);

        // Assert
        assertNull(actualResponse);
        verify(uploadService, times(1)).uploadCsvSingleThreaded(any(UploadRequest.class));
    }

    // ==================== uploadCsvMultiThreaded Tests ====================

    @Test
    @DisplayName("uploadCsvMultiThreaded - Happy Path: Should return UploadResponse with valid data")
    void testUploadCsvMultiThreadedSuccess() {
        // Arrange
        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setPath("/path/to/file.csv");

        ServiceRequest<UploadRequest> serviceRequest = new ServiceRequest<>();
        serviceRequest.setData(uploadRequest);

        UploadResponse expectedResponse = UploadResponse.builder()
                .count(500)
                .taskId("task-456")
                .timeTaken(2000L)
                .build();

        when(uploadService.uploadCsvMultiThreaded(any(UploadRequest.class)))
                .thenReturn(expectedResponse);

        // Act
        UploadResponse actualResponse = taskController.uploadCsvMultiThreaded(serviceRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCount());
        assertEquals("task-456", actualResponse.getTaskId());
        assertEquals(2000L, actualResponse.getTimeTaken());
        verify(uploadService, times(1)).uploadCsvMultiThreaded(any(UploadRequest.class));
    }

    @Test
    @DisplayName("uploadCsvMultiThreaded - Error Case: Should handle service returning null")
    void testUploadCsvMultiThreadedNullResponse() {
        // Arrange
        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setPath("/invalid/path.csv");

        ServiceRequest<UploadRequest> serviceRequest = new ServiceRequest<>();
        serviceRequest.setData(uploadRequest);

        when(uploadService.uploadCsvMultiThreaded(any(UploadRequest.class)))
                .thenReturn(null);

        // Act
        UploadResponse actualResponse = taskController.uploadCsvMultiThreaded(serviceRequest);

        // Assert
        assertNull(actualResponse);
        verify(uploadService, times(1)).uploadCsvMultiThreaded(any(UploadRequest.class));
    }

    // ==================== uploadCsvAsynchronous Tests ====================

    @Test
    @DisplayName("uploadCsvAsynchronous - Happy Path: Should return UploadResponse with valid data")
    void testUploadCsvAsynchronousSuccess() {
        // Arrange
        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setPath("/path/to/file.csv");

        ServiceRequest<UploadRequest> serviceRequest = new ServiceRequest<>();
        serviceRequest.setData(uploadRequest);

        UploadResponse expectedResponse = UploadResponse.builder()
                .count(1000)
                .taskId("task-789")
                .timeTaken(1500L)
                .build();

        when(uploadService.uploadCsvAsynchronous(any(UploadRequest.class)))
                .thenReturn(expectedResponse);

        // Act
        UploadResponse actualResponse = taskController.uploadCsvAsynchronous(serviceRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(1000, actualResponse.getCount());
        assertEquals("task-789", actualResponse.getTaskId());
        assertEquals(1500L, actualResponse.getTimeTaken());
        verify(uploadService, times(1)).uploadCsvAsynchronous(any(UploadRequest.class));
    }

    @Test
    @DisplayName("uploadCsvAsynchronous - Error Case: Should handle service returning null")
    void testUploadCsvAsynchronousNullResponse() {
        // Arrange
        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setPath("/invalid/path.csv");

        ServiceRequest<UploadRequest> serviceRequest = new ServiceRequest<>();
        serviceRequest.setData(uploadRequest);

        when(uploadService.uploadCsvAsynchronous(any(UploadRequest.class)))
                .thenReturn(null);

        // Act
        UploadResponse actualResponse = taskController.uploadCsvAsynchronous(serviceRequest);

        // Assert
        assertNull(actualResponse);
        verify(uploadService, times(1)).uploadCsvAsynchronous(any(UploadRequest.class));
    }

    // ==================== checkUploadTaskStatus Tests ====================

    @Test
    @DisplayName("checkUploadTaskStatus - Happy Path: Should return TaskStatus with valid data")
    void testCheckUploadTaskStatusSuccess() {
        // Arrange
        String taskId = "task-123";
        TaskStatus expectedStatus = TaskStatus.builder()
                .taskId(taskId)
                .status("COMPLETED")
                .processed(100)
                .total(100)
                .build();

        when(uploadService.checkUploadTaskStatus(taskId))
                .thenReturn(expectedStatus);

        // Act
        TaskStatus actualStatus = taskController.checkUploadTaskStatus(taskId);

        // Assert
        assertNotNull(actualStatus);
        assertEquals(taskId, actualStatus.getTaskId());
        assertEquals("COMPLETED", actualStatus.getStatus());
        assertEquals(100, actualStatus.getProcessed());
        assertEquals(100, actualStatus.getTotal());
        verify(uploadService, times(1)).checkUploadTaskStatus(taskId);
    }

    @Test
    @DisplayName("checkUploadTaskStatus - Happy Path: Should return TaskStatus with PROCESSING status")
    void testCheckUploadTaskStatusProcessing() {
        // Arrange
        String taskId = "task-456";
        TaskStatus expectedStatus = TaskStatus.builder()
                .taskId(taskId)
                .status("PROCESSING")
                .processed(50)
                .total(100)
                .build();

        when(uploadService.checkUploadTaskStatus(taskId))
                .thenReturn(expectedStatus);

        // Act
        TaskStatus actualStatus = taskController.checkUploadTaskStatus(taskId);

        // Assert
        assertNotNull(actualStatus);
        assertEquals(taskId, actualStatus.getTaskId());
        assertEquals("PROCESSING", actualStatus.getStatus());
        assertEquals(50, actualStatus.getProcessed());
        assertEquals(100, actualStatus.getTotal());
        verify(uploadService, times(1)).checkUploadTaskStatus(taskId);
    }

    @Test
    @DisplayName("checkUploadTaskStatus - Error Case: Should handle invalid task ID")
    void testCheckUploadTaskStatusInvalidTaskId() {
        // Arrange
        String invalidTaskId = "invalid-task-id";

        when(uploadService.checkUploadTaskStatus(invalidTaskId))
                .thenReturn(null);

        // Act
        TaskStatus actualStatus = taskController.checkUploadTaskStatus(invalidTaskId);

        // Assert
        assertNull(actualStatus);
        verify(uploadService, times(1)).checkUploadTaskStatus(invalidTaskId);
    }

    @Test
    @DisplayName("checkUploadTaskStatus - Error Case: Should handle empty task ID")
    void testCheckUploadTaskStatusEmptyTaskId() {
        // Arrange
        String emptyTaskId = "";

        when(uploadService.checkUploadTaskStatus(emptyTaskId))
                .thenReturn(null);

        // Act
        TaskStatus actualStatus = taskController.checkUploadTaskStatus(emptyTaskId);

        // Assert
        assertNull(actualStatus);
        verify(uploadService, times(1)).checkUploadTaskStatus(emptyTaskId);
    }
}
