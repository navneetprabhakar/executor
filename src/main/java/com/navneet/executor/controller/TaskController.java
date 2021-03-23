package com.navneet.executor.controller;

import com.navneet.executor.models.ServiceRequest;
import com.navneet.executor.models.TaskStatus;
import com.navneet.executor.models.UploadRequest;
import com.navneet.executor.models.UploadResponse;
import com.navneet.executor.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author navneetprabhakar
 */
@RestController
@RequestMapping("v1/task")
public class TaskController {
    @Autowired
    private UploadService uploadService;

    /**
     * This API processes csv in single thread (Synchronous)
     * @param request
     * @return
     */
    @PostMapping("upload/single")
    public UploadResponse uploadCsvSingleThreaded(@RequestBody ServiceRequest<UploadRequest> request){
        return uploadService.uploadCsvSingleThreaded(request.getData());
    }

    /**
     * This API processes csv in multiple parallel threads (Synchronous)
     * @param request
     * @return
     */
    @PostMapping("upload/multi")
    public UploadResponse uploadCsvMultiThreaded(@RequestBody ServiceRequest<UploadRequest> request){
        return uploadService.uploadCsvMultiThreaded(request.getData());
    }

    /**
     * This API processes csv in multiple parallel threads (Asynchronous)
     * @param request
     * @return
     */
    @PostMapping("upload/async")
    public UploadResponse uploadCsvAsynchronous(@RequestBody ServiceRequest<UploadRequest> request){
        return uploadService.uploadCsvAsynchronous(request.getData());
    }

    /**
     * This poll API fetches the task status based on task id
     * @param taskId
     * @return
     */
    @GetMapping("status")
    public TaskStatus checkUploadTaskStatus(@RequestParam(name = "task_id") String taskId){
        return uploadService.checkUploadTaskStatus(taskId);
    }
}
