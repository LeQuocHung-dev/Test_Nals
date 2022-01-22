package com.test.nals.domain;

import com.test.nals.entity.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkRequest {

    String id;

    @NotNull(message = "WorkName must not empty or null")
    String workName;

    Date startingDate;

    Date endingDate;

    Status status;
}
