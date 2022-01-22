package com.test.nals.repository;

import com.test.nals.domain.WorkRequest;
import com.test.nals.entity.Work;
import org.springframework.data.domain.Page;

public interface WorkRepository {

    int count();

    boolean isExistWork(String idWork);

    int addWork(WorkRequest work);

    int updateWork(WorkRequest work);

    int deleteWork(String workId);

    Page<Work> getWorks(org.springframework.data.domain.Pageable pageable);

}
