package com.test.nals.controller;

import com.test.nals.domain.WorkRequest;
import com.test.nals.entity.Work;
import com.test.nals.service.WorkService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/works")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class WorkController {

    @Autowired
    WorkService workService;

    @PostMapping
    public ResponseEntity addWork(@Valid @RequestBody WorkRequest request) {
        workService.addWork(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateWork(@PathVariable(name = "id") String idWork,
                         @RequestBody WorkRequest workRequest) {
        workService.updateWork(idWork, workRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeWork(@PathVariable(name = "id") String idWork) {
        workService.removeWork(idWork);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public Page<Work> getWorks(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                               @RequestParam(name = "size", required = false, defaultValue = "5") int size,
                               @RequestParam(name = "sortType", required = false, defaultValue = "ASC") String sortType,
                               @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField) {
        return workService.getListWork(page, size, sortType, sortField);
    }
}
