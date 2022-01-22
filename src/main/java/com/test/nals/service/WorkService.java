package com.test.nals.service;

import com.test.nals.domain.WorkRequest;
import com.test.nals.entity.Work;
import com.test.nals.exception.DaoException;
import com.test.nals.util.ErrorCode;
import com.test.nals.repository.WorkRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@Getter
@Setter
public class WorkService {

    @Autowired
    WorkRepository workRepository;

    public WorkService() {
    }

    /*
    * Method to create a new Work
    */
    public int addWork(WorkRequest workRequest) {
        log.info("Create a new work {} with status {}", workRequest.getWorkName(), workRequest.getStatus());

        //Throw exception if a work is exist with id
        if (workRepository.isExistWork(workRequest.getId())) {
            throw new DaoException(ErrorCode.WORK_EXISTED.getMessage());
        }
        int result = workRepository.addWork(workRequest);
        log.info("The work with name {} has just created", workRequest.getWorkName());
        return result;
    }

    /*
    * Method to change a exist work
    */
    public int updateWork(String idWork, WorkRequest workRequest) {
        log.info("Update a Work with Id ()", idWork);

        //if a work is not exist, throw a new exception
        if (!workRepository.isExistWork(workRequest.getId())) {
            throw new DaoException(ErrorCode.WORK_NOT_EXISTED.getMessage());
        }
        //Update a work
        int result = workRepository.updateWork(workRequest);
        log.info("The Work with Id {} is updated", idWork);
        return result;
    }

    /*
    * Method to remove a work with Id
    */
    public int removeWork(String idWork) {
        log.info("Delete work with Id {}", idWork);

        //if a work is not exist, throw a new exception
        if (!workRepository.isExistWork(idWork)) {
            throw new DaoException(ErrorCode.WORK_NOT_EXISTED.getMessage());
        }

        //Remove a work with Id
        int result = workRepository.deleteWork(idWork);

        log.info("Work with Id {} is deleted", idWork);
        return result;
    }

    /*
    * Method to get list of work per page
    * @Param int page, int size, String sortType, String sortField
    * @Return works
    */
    public Page<Work> getListWork(int page, int size, String sortType, String sortField) {

        //sort object
        Sort sortable = null;
        if ("ASC".equalsIgnoreCase(sortType)) {
            sortable = Sort.by(sortField).ascending();
        }
        if ("DESC".equalsIgnoreCase(sortType)) {
            sortable = Sort.by(sortField).descending();
        }
        //pageable object
        Pageable pageable = PageRequest.of(page, size, sortable);

        return workRepository.getWorks(pageable);
    }

    /*
    * Method to validate times
    */
    private boolean isValidTime(LocalDateTime startingDate, LocalDateTime endingDate) {
        //If starttingDate after endingDate: return false
        if ((startingDate != null && endingDate != null) && startingDate.isAfter(endingDate)) {
            return false;
        }
        return true;
    }

}
