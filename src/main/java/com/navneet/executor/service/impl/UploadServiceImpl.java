package com.navneet.executor.service.impl;

import com.navneet.executor.constants.StatusEnum;
import com.navneet.executor.data.models.Tasks;
import com.navneet.executor.data.repo.CustomerRepo;
import com.navneet.executor.data.repo.TasksRepo;
import com.navneet.executor.models.CustomerInfo;
import com.navneet.executor.models.TaskStatus;
import com.navneet.executor.models.UploadRequest;
import com.navneet.executor.models.UploadResponse;
import com.navneet.executor.service.UploadService;
import com.navneet.executor.service.helper.UploadServiceHelper;
import com.navneet.executor.utils.FileUtils;
import com.univocity.parsers.common.IterableResult;
import com.univocity.parsers.common.ParsingContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UploadServiceHelper uploadServiceHelper;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private TasksRepo tasksRepo;
    /**
     * This method uploads CSV and process the request in multithreaded executor
     *
     * @param request
     * @return
     */
    @Override
    public UploadResponse uploadCsvMultiThreaded(UploadRequest request) {
        try{
            if(null!=request && StringUtils.hasLength(request.getPath())){
                Long start= new Date().getTime();
                Integer numberOfLines=fileUtils.numberOfLines(request.getPath())-1; // Reduce by 1 to ignore header line
                IterableResult<CustomerInfo, ParsingContext> iterator= fileUtils.readDataFromCsv(request.getPath(), CustomerInfo.class);
                String taskId=uploadServiceHelper.createTasks(numberOfLines);
                if(StringUtils.hasLength(taskId)){
                    tasksRepo.updateTaskStatus(StatusEnum.PROCESSING.name(),taskId);
                    // Max number of thread is a function of the resource consumption per thread and total number of cores
                    // I am using the value same as the number of available cores
                    int cores = Runtime.getRuntime().availableProcessors();
                    ExecutorService executor = Executors.newFixedThreadPool(cores);
                    for(CustomerInfo element:iterator){
                        // Runnable Lambda thread
                        executor.submit(()->{
                            customerRepo.save(uploadServiceHelper.requestToEntity(element));
                            tasksRepo.updateProcessedCount(taskId);
                        });
                    }

                    executor.shutdown();
                    while (!executor.isTerminated()) {
                        // Since execution has not completed sleep thread for 1 sec and check again
                        Thread.sleep(1000);
                    }
                    Long timeTaken=new Date().getTime()-start;
                    log.info("File processing completed in :{} ms", timeTaken);
                    tasksRepo.updateTaskStatus(StatusEnum.COMPLETED.name(),taskId);
                    return UploadResponse.builder().count(numberOfLines).taskId(taskId).timeTaken(timeTaken).build();
                }else{
                    log.info("Unable to create task.");
                }
            }else{
                log.info("Invalid upload request. File path is mandatory.");
            }
        }catch (Exception e){
            log.error("An error occurred while reading csv from file path:",e);
        }
        return null;
    }

    /**
     * This method uploads CSV and process the request in single thread
     *
     * @param request
     * @return
     */
    @Override
    public UploadResponse uploadCsvSingleThreaded(UploadRequest request) {
        try{
            if(null!=request && StringUtils.hasLength(request.getPath())){
                Long start= new Date().getTime();
                Integer numberOfLines=fileUtils.numberOfLines(request.getPath())-1; // Reduce by 1 to ignore header line
                IterableResult<CustomerInfo, ParsingContext> iterator= fileUtils.readDataFromCsv(request.getPath(), CustomerInfo.class);
                String taskId=uploadServiceHelper.createTasks(numberOfLines);
                if(StringUtils.hasLength(taskId)){
                    tasksRepo.updateTaskStatus(StatusEnum.PROCESSING.name(),taskId);
                    for(CustomerInfo element:iterator){
                        customerRepo.save(uploadServiceHelper.requestToEntity(element));
                        tasksRepo.updateProcessedCount(taskId);
                    }
                    Long timeTaken=new Date().getTime()-start;
                    log.info("File processing completed in :{} ms", timeTaken);
                    tasksRepo.updateTaskStatus(StatusEnum.COMPLETED.name(),taskId);
                    return UploadResponse.builder().count(numberOfLines).taskId(taskId).timeTaken(timeTaken).build();
                }else{
                    log.info("Unable to create task.");
                }
            }else{
                log.info("Invalid upload request. File path is mandatory.");
            }
        }catch (Exception e){
            log.error("An error occurred while reading csv from file path:",e);
        }
        return null;
    }

    /**
     * This method uploads CSV and processes the request in asynchronous manner with multithread
     *
     * @param request
     * @return
     */
    @Override
    public UploadResponse uploadCsvAsynchronous(UploadRequest request) {
        try{
            if(null!=request && StringUtils.hasLength(request.getPath())){
                Long start= new Date().getTime();
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(()->uploadCsvMultiThreaded(request));
                Long timeTaken=new Date().getTime()-start;
                log.info("Asynchronous process initiated in :{} ms", timeTaken);
                return UploadResponse.builder().timeTaken(timeTaken).build();
            }else{
                log.info("Invalid upload request. File path is mandatory.");
            }
        }catch(Exception e){
            log.error("An error occurred in uploading csv in asynchronous manner:",e);
        }
        return null;
    }

    /**
     * This method checks status of task id
     *
     * @param taskId
     * @return
     */
    @Override
    public TaskStatus checkUploadTaskStatus(String taskId) {
        try{
            if(StringUtils.hasLength(taskId)){
                Tasks task=tasksRepo.findByTaskId(taskId);
                if(null!=task){
                    log.info("Task status for task id:{} , status:{}", taskId, task.getStatus());
                    return TaskStatus.builder().taskId(taskId).total(task.getTotalCount()).processed(task.getProcessedCount()).status(task.getStatus()).build();
                }else{
                    log.info("Invalid task id");
                }
            }else{
                log.info("Task Id cannot be empty or null.");
            }
        }catch(Exception e){
            log.error("An error occurred in fetching the upload task status:",e);
        }
        return null;
    }
}
