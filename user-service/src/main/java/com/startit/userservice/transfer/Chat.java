package com.startit.userservice.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private Long id;
    private Long itemId;
    private Long customerId;
}
