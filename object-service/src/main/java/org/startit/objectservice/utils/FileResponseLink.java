package org.startit.objectservice.utils;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.startit.objectservice.controller.ObjectController;
import org.startit.objectservice.transfer.FileResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FileResponseLink implements LinkUtils<FileResponse> {
    public FileResponse addOperationWithLink(FileResponse response) {
        Link[] links = new Link[]{
                linkTo(methodOn(ObjectController.class).downloadFile(response.getFilename()))
                        .withRel("file")
                        .withType("GET")
                        .withDeprecation("Download File")
        };
        return response.add(links);
    }

    public FileResponse getOperationWithLink(FileResponse response) {
        Link[] links = new Link[]{
                linkTo(methodOn(ObjectController.class).fileUpload(null, null))
                        .withRel("file")
                        .withType("POST")
                        .withDeprecation("Add File"),
                linkTo(methodOn(ObjectController.class).downloadFile(response.getFilename()))
                        .withRel("file")
                        .withType("GET")
                        .withDeprecation("Download File")
        };
        return response.add(links);
    }
}