package com.navneet.executor.threads;

import com.navneet.executor.data.repo.CustomerRepo;
import com.navneet.executor.data.repo.TasksRepo;
import com.navneet.executor.models.CustomerInfo;
import com.navneet.executor.service.helper.UploadServiceHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Log4j2
public class ProcessFileUploadTask implements Callable<Boolean> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Autowired
    private UploadServiceHelper uploadServiceHelper;
    @Autowired
    private TasksRepo tasksRepo;
    @Autowired
    private CustomerRepo customerRepo;

    private CustomerInfo element;
    private String taskId;

    @Override
    public Boolean call() {
        try{
            customerRepo.save(uploadServiceHelper.requestToEntity(element));
            tasksRepo.updateProcessedCount(taskId);
            return true;
        }catch (Exception e){
            log.error("An error occurred while processing thread:",e);
        }
        return false;
    }

    public void setElement(CustomerInfo element) {
        this.element = element;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


}
