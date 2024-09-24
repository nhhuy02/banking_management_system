package com.ojt.klb.banking_notification_service.dto.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Setter
@Getter
@NoArgsConstructor
public class ListResponse<T>{
    private Page<T> page;


    public ListResponse(Page<T> page) {
        this.page = page;
    }



}
