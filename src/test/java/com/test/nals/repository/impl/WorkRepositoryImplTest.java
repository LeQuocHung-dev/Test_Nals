package com.test.nals.repository.impl;

import com.test.nals.config.TestConfiguration;
import com.test.nals.domain.WorkRequest;
import com.test.nals.entity.Status;
import com.test.nals.entity.Work;
import com.test.nals.repository.WorkRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class WorkRepositoryImplTest {

    @Autowired
    WorkRepository workRepository;

    @Test
    @Transactional
    public void isExist() throws Exception {
        //GIVEN
        String workId = "work12";
        WorkRequest work = getWork();
        workRepository.addWork(work);
        //WHEN
        boolean isExist = workRepository.isExistWork(workId);
        //THEN
        assertTrue(isExist);
    }

    @Test
    @Transactional
    public void save() throws Exception {
        //GIVEN
        WorkRequest work = getWork();
        boolean isExist = workRepository.isExistWork(work.getId());
        //WHEN
        workRepository.addWork(work);
        boolean isExistAfterCreate = workRepository.isExistWork(work.getId());
        //THEN
        assertFalse(isExist);
        assertTrue(isExistAfterCreate);
    }

    @Test
    @Transactional
    public void update() throws Exception {
        //GIVEN
        WorkRequest work = getWork();
        workRepository.addWork(work);
        work.setWorkName("TEST");
        //WHEN
        int row = workRepository.updateWork(work);
        //THEN
        assertEquals(1, row);
    }

    @Test
    public void delete() throws Exception {
        //GIVEN
        WorkRequest work = getWork();
        workRepository.addWork(work);
        //WHEN
        int row = workRepository.deleteWork(work.getId());
        //THEN
        boolean isExist = workRepository.isExistWork(work.getId());
        assertEquals(1, row);
        assertTrue(!isExist);
    }

    @Test
    public void getListWork() throws Exception {
        //GIVEN
        Pageable pageable = PageRequest.of(1,5, Sort.by("id").ascending());
        //WHEN
        Page<Work> works = workRepository.getWorks(pageable);
        //THEN
        assertTrue(works.getTotalPages() > 0);
    }

    private WorkRequest getWork() throws ParseException {
        WorkRequest work = new WorkRequest();
        work.setId("work12");
        work.setWorkName("Lam Toan");
        work.setStartingDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/10/2020"));
        work.setEndingDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/11/2020"));
        work.setStatus(Status.PLANNING);
        return work;
    }

}