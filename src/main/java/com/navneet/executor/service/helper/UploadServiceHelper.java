package com.navneet.executor.service.helper;

import com.navneet.executor.constants.StatusEnum;
import com.navneet.executor.data.models.Customer;
import com.navneet.executor.data.models.Tasks;
import com.navneet.executor.data.repo.TasksRepo;
import com.navneet.executor.models.CustomerInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author navneetprabhakar
 */
@Service
@Log4j2
public class UploadServiceHelper {

    @Autowired
    private TasksRepo tasksRepo;

    /**
     * This method creates a task entry for file process
     * @param totalCount
     * @return
     */
    public String createTasks(Integer totalCount){
        try{
            String taskId=UUID.randomUUID().toString();
            tasksRepo.save(Tasks.builder()
                    .taskId(taskId)
                    .totalCount(totalCount)
                    .processedCount(0)
                    .status(StatusEnum.INITIATED.name())
                    .build());
            return taskId;
        }catch(Exception e){
            log.error("An error occurred in saving Task entity:",e);
        }
        return null;
    }

    /**
     * This method converts the request POJO to Entity POJO
     * @param request
     * @return
     */
    public Customer requestToEntity(CustomerInfo request){
        return Customer.builder()
                .customerId(request.getId())
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .build();
    }
}
