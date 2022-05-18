package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.DataTransferObject;
import org.springframework.hateoas.RepresentationModel;

import javax.xml.crypto.Data;

public interface Hateoas<T extends RepresentationModel<? extends DataTransferObject>> {
    T buildHateoas(T model);
}
