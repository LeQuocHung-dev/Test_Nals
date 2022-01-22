package com.test.nals.repository.impl;

import com.test.nals.domain.WorkRequest;
import com.test.nals.entity.Status;
import com.test.nals.entity.Work;
import com.test.nals.exception.DaoException;
import com.test.nals.repository.WorkRepository;
import com.test.nals.util.DaoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class WorkRepositoryImpl implements WorkRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean isExistWork(String idWork) {
        log.info("Check Work with Id {} exist or not", idWork);

        //params
        List<SqlParameterValue> params = new ArrayList<>();
        params.add(new SqlParameterValue(Types.VARCHAR, idWork));

        //Get query statement
        String query = DaoUtils.getSQLStatement("SELECT_WORK_BY_KEY");

        List<String> result = null;
        try {
            result = jdbcTemplate.query(query, params.toArray(), this.WORK);
        } catch (DaoException ex) {
            throw new DaoException(ex.getMessage());
        }

        //Result is not empty => return true.
        return !result.isEmpty() ? true:false;
    }

    @Override
    public int addWork(WorkRequest work) {
        //Get params
        List<SqlParameterValue> params = new ArrayList<>();
        params.add(new SqlParameterValue(Types.VARCHAR, work.getId()));
        params.add(new SqlParameterValue(Types.VARCHAR, work.getWorkName()));
        params.add(new SqlParameterValue(Types.TIMESTAMP, work.getStartingDate()));
        params.add(new SqlParameterValue(Types.TIMESTAMP, work.getEndingDate()));
        params.add(new SqlParameterValue(Types.VARCHAR, work.getStatus()));

        //Get a query statement
        String query = DaoUtils.getSQLStatement("SAVE_WORK");

        try {
            return jdbcTemplate.update(query, params.toArray());
        } catch (DaoException ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    @Override
    public int updateWork(WorkRequest work) {
        //Get param list
        List<SqlParameterValue> params = new ArrayList<>();
        params.add(new SqlParameterValue(Types.VARCHAR, work.getId()));
        params.add(new SqlParameterValue(Types.VARCHAR, work.getWorkName()));
        params.add(new SqlParameterValue(Types.TIMESTAMP, work.getStartingDate()));
        params.add(new SqlParameterValue(Types.TIMESTAMP, work.getEndingDate()));
        params.add(new SqlParameterValue(Types.VARCHAR, work.getStatus()));
        params.add(new SqlParameterValue(Types.VARCHAR, work.getId()));

        //Get query statement
        String query = DaoUtils.getSQLStatement("UPDATE_WORK");
        try {
            return jdbcTemplate.update(query, params.toArray());
        } catch (DaoException ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    @Override
    public int deleteWork(String workId) {
        //Get params
        List<SqlParameterValue> params = new ArrayList<>();
        params.add(0, new SqlParameterValue(Types.VARCHAR, workId));

        //Get a query statement
        String query = DaoUtils.getSQLStatement("DELETE_WORK");
        try {
            return jdbcTemplate.update(query, params.toArray());
        } catch (DaoException ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    @Override
    public Page<Work> getWorks(org.springframework.data.domain.Pageable pageable) {

        log.info("Get a work list in page {} ", pageable.getPageNumber());

        //Get a sort field name
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : Sort.Order.by("id");
        List<Work> works = new ArrayList<>();
        try {
            works = jdbcTemplate.query("SELECT * FROM work ORDER BY " + order.getProperty() + " "
                            + order.getDirection().name() + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset(),
                    (rs, rowNum) -> mapWorkResult(rs));
        } catch (DaoException ex) {
            throw new DaoException(ex.getMessage());
        }
        return new PageImpl<Work>(works, pageable, count());
    }

    /*
    * Method to count number of records
    * @Return int
    */
    @Override
    public int count() {

        //Get a query statement
        String query = DaoUtils.getSQLStatement("COUNT_WORK");
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(query, Integer.class);
        } catch (DaoException ex) {
            throw new DaoException(ex.getMessage());
        }
        return count;
    }

    private final RowMapper<String> WORK = new RowMapper<String>() {
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("ID");
        }
    };

    /*
    * Convert ResultSet to work object
    */
    public Work mapWorkResult(ResultSet rs) throws SQLException {

        Work work = null; 
        try {
            work = new Work();
            work.setId(rs.getString("id"));
            work.setWorkName(rs.getString("work_Name"));
            work.setStartingDate(new SimpleDateFormat("dd/MM/yyyy").parse(rs.getString("starting_Date")));
            work.setEndingDate(new SimpleDateFormat("dd/MM/yyyy").parse(rs.getString("ending_Date")));
            work.setStatus(getStatus(rs.getString("status")));
        } catch (ParseException e) {
            log.error(e.getMessage());            
        }
        return work;
    }
    
    private Status getStatus(String message) {
        Status status = null;
        switch (message)
        {
            case "PLANNING":
                status = Status.PLANNING;
                break;
            case "DOING":
                status = Status.DOING;
                break;
            case "COMPLETE":
                status = Status.COMPLETE;
                break;    
        }
        return status;
    }


    /*
    * Convert to work object
    *
    */
    public Work convertToWorkObject(WorkRequest workRequest) {
        Work work = new Work();
        work.setId(workRequest.getId());
        work.setWorkName(workRequest.getWorkName());
        work.setStartingDate(workRequest.getStartingDate());
        work.setEndingDate(workRequest.getEndingDate());
        work.setStatus(workRequest.getStatus());
        return work;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
