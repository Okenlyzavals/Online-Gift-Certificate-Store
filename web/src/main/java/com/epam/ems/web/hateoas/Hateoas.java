package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.DataTransferObject;
import com.epam.ems.web.hateoas.constant.HateoasConstant;
import com.epam.ems.web.hateoas.exception.HateoasException;
import lombok.extern.java.Log;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public interface Hateoas<T extends RepresentationModel<? extends DataTransferObject>> {
    T buildHateoas(T model);

    default CollectionModel<T> buildPaginationModel(Iterable<T> content,
                                                    Supplier<Object> firstPageSupplier,
                                                    Supplier<Object> nextPageSupplier,
                                                    Supplier<Object> prevPageSupplier){
        CollectionModel<T> model = CollectionModel.of(content);
        Object nextPage;
        Object prevPage;
        Object firstPage;
        try {
            CompletableFuture<Object> firstPageFuture = CompletableFuture.supplyAsync(firstPageSupplier);
            CompletableFuture<Object> nextPageFuture = CompletableFuture.supplyAsync(nextPageSupplier);
            CompletableFuture<Object> previousPageFuture = CompletableFuture.supplyAsync(prevPageSupplier);

            nextPage = nextPageFuture.get();
            prevPage = previousPageFuture.get();
            firstPage = firstPageFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new HateoasException(e);
        }

        model.addIf(nextPage != null, ()->linkTo(nextPage).withRel(HateoasConstant.PAGE_NEXT));
        model.addIf(prevPage != null, ()->linkTo(prevPage).withRel(HateoasConstant.PAGE_PREV));
        model.addIf(firstPage != null, ()->linkTo(firstPage).withRel(HateoasConstant.PAGE_FIRST));
        return model;
    }
}
