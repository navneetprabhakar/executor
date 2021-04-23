package com.navneet.executor.service;

import com.navneet.executor.models.TaskStatus;
import com.navneet.executor.models.UploadRequest;
import com.navneet.executor.models.UploadResponse;

/**
 * @author navneetprabhakar
 */
public interface UploadService {

    /**
     * This method uploads CSV and processes the request in multithreaded executor
     * @param request
     * @return
     */
    UploadResponse uploadCsvMultiThreaded(UploadRequest request);

    /**
     * This method uploads CSV and processes the request in single thread
     * @param request
     * @return
     */
    UploadResponse uploadCsvSingleThreaded(UploadRequest request);

    /**
     * This method uploads CSV and processes the request in asynchronous manner
     * @param request
     * @return
     */
    UploadResponse uploadCsvAsynchronous(UploadRequest request);

    /**
     * This method checks status of task id
     * @param taskId
     * @return
     */
    TaskStatus checkUploadTaskStatus(String taskId);

    /**
     * This method uploads CSV and processes the request with ThreadPoolTaskExecutor
     * @param request
     * @return
     */
    UploadResponse uploadCsvMultiThreadedWithThreadPoolExecutor(UploadRequest request);


}
