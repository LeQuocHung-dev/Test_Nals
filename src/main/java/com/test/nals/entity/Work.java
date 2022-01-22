package com.test.nals.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "work")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Work {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "work_Name")
    String workName;

    @Column(name = "starting_Date")
    Date startingDate;

    @Column(name = "ending_Date")
    Date endingDate;

    @Column(name = "status")
    Status status;
}
