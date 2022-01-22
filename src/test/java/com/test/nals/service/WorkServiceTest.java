package com.test.nals.service;

import com.test.nals.config.TestConfiguration;
import com.test.nals.domain.WorkRequest;
import com.test.nals.entity.Status;
import com.test.nals.entity.Work;
import com.test.nals.repository.WorkRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class WorkServiceTest {

    @Autowired
    private WorkService workService;

    @MockBean
    private WorkRepository workRepository;

    @Test
    public void createNewWork() throws Exception {
        //GIVEN
        //create a work request argument
        WorkRequest request = new WorkRequest();
        request.setId("work12");
        request.setWorkName("Lam Toan Van");
        request.setStartingDate(null);
        request.setEndingDate(null);
        request.setStatus(Status.PLANNING);
        //convert to work object
        when(workRepository.addWork(request)).thenReturn(1);
        //WHEN
        int result = workService.addWork(request);
        //THEN
        assertEquals(1, result);
    }

    @Test
    public void updateWork() throws Exception {
        //GIVEN
        //create a work request argument
        WorkRequest request = new WorkRequest();
        request.setId("work12");
        request.setWorkName("Lam Toan Van");
        request.setStartingDate(null);
        request.setEndingDate(null);
        request.setStatus(Status.PLANNING);

        when(workRepository.isExistWork(request.getId())).thenReturn(true);
        when(workRepository.updateWork(request)).thenReturn(1);
        //WHEN
        int result = workService.updateWork(request.getId(), request);
        //THEN
        assertEquals(1, result);
    }

    @Test
    public void removeWork() throws Exception {
        //GIVEN
        String workId = "work12";
        //mock value when call deleteWork of workRepository
        when(workRepository.isExistWork(workId)).thenReturn(true);
        when(workRepository.deleteWork(workId)).thenReturn(1);
        //WHEN
        int result = workService.removeWork(workId);
        //THEN
        assertEquals(1, result);
    }

    @Test
    public void getListWork() throws Exception {
        //GIVEN
        List<Work> works = new ArrayList<>();
        prepareWorkList(works);
        Page<Work> workPage = new PageImpl<Work>(works);

        //Create pageable object
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());
        when(workRepository.getWorks(pageable)).thenReturn(workPage);
        //WHEN
        Page<Work> page = workService.getListWork(0, 5, "ASC", "id");
        //THEN
        assertTrue(page.getTotalPages()>0);
    }

    /*
    * Preparing a list of work
    *
    */
    private List<Work> prepareWorkList(List<Work> works) {

        //Create a new work
        Work work;
        for (int i = 0; i < 5; i++) {
            work = new Work();
            work.setId("work" + i);
            work.setWorkName("workName" + i);
            work.setStartingDate(null);
            work.setEndingDate(null);
            work.setStatus(Status.PLANNING);
            works.add(work);
        }
        return works;
    }

}