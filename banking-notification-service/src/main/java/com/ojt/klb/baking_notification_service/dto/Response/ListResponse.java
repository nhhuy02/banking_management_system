package com.ojt.klb.baking_notification_service.dto.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
public class ListResponse<T>{
    private Page<T> page;


    public ListResponse(Page<T> page) {
        this.page = page;
    }



}
