package com.navneet.executor.data.repo;

import com.navneet.executor.data.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author navneetprabhakar
 */
@Repository
public interface TasksRepo extends JpaRepository<Tasks, Long> {

    Tasks findByTaskId(String taskId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tasks t SET t.processedCount = t.processedCount +1 where t.taskId=:task_id")
    void updateProcessedCount(@Param("task_id") String taskId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tasks t SET t.status = :status where t.taskId=:task_id")
    void updateTaskStatus(@Param("status") String status, @Param("task_id") String taskId);

}
