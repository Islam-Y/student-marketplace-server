package org.startit.objectservice.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL, valueFilter = RepresentationModel.class)
public class FileResponse extends RepresentationModel<FileResponse> implements Serializable {
    String filename;
    String contentType;
    Long fileSize;
    Date createdTime;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    transient InputStreamResource stream;
}
