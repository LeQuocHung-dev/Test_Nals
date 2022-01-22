package com.test.nals.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.nals.config.TestConfiguration;
import com.test.nals.domain.WorkRequest;
import com.test.nals.entity.Status;
import com.test.nals.entity.Work;
import com.test.nals.repository.impl.WorkRepositoryImpl;
import com.test.nals.service.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, WorkController.class, WorkService.class, WorkRepositoryImpl.class})
@Slf4j
public class WorkControllerTest {

    @Autowired
    WorkController workController;

    @Test
    public void createNewWork() throws Exception {
        //GIVEN
        WorkRequest work = getWorkRequest();
        //WHEN
        ResponseEntity status = workController.addWork(work);
        //THEN
        assertEquals(201, status.getStatusCodeValue());
    }

    @Test
    public void updateWork() throws Exception {
        //GIVEN
        String idWork = "work12";
        WorkRequest work = getWorkRequest();
        workController.addWork(work);
        //WHEN
        ResponseEntity responseEntityActual = workController.updateWork(idWork, work);
        //THEN
        assertEquals(200, responseEntityActual.getStatusCodeValue());
    }

    @Test
    public void removeWork() throws Exception {
        //GIVEN
        String id = "work12";
        WorkRequest work = getWorkRequest();
        workController.addWork(work);
        //WHEN
        ResponseEntity responseEntityActual = workController.removeWork(id);
        //THEN
        assertEquals(200, responseEntityActual.getStatusCodeValue());

    }

    @MockBean
    WorkService workServiceMock;
    
    @InjectMocks
    WorkController workControllerMock;
    
    @Test
    public void getWorks() throws Exception {
        //GIVEN
        List<Work> works = new ArrayList<>();
        prepareWorkList(works);
        Page<Work> workPage = new PageImpl<Work>(works);
        ReflectionTestUtils.setField(workControllerMock, "workService", workServiceMock);
        Mockito.when(workServiceMock.getListWork(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(workPage);
        //WHEN
        Page<Work> workListActual = workControllerMock.getWorks(0,5, "DESC", "id");
        //THEN
        assertTrue(workListActual.getTotalElements() > 0);
    }

    /*
    * Preparing a list of work
    *
    */
    private List<Work> prepareWorkList(List<Work> works) throws ParseException {

        //Create a new work
        Work work;
        for (int i = 0; i < 5; i++) {
            work = new Work();
            work.setId("work" + i);
            work.setWorkName("workName" + i);
            work.setStartingDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/1/2020"));
            work.setEndingDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/2/2020"));
            work.setStatus(Status.PLANNING);
            works.add(work);
        }
        return works;
    }
    
    private WorkRequest getWorkRequest() {
        WorkRequest request = null;
        try {
            request = new WorkRequest();
            request.setId("work12");
            request.setWorkName("Lam Toan");
            request.setStartingDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/1/2020"));
            request.setEndingDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/02/2020"));
            request.setStatus(Status.PLANNING);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        return request;
    }

}