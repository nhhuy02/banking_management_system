package com.ojt.klb.external;

//import com.ojt.klb.configuration.FeignConfiguration;
import com.ojt.klb.model.dto.external.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-user-service")
public interface UserService {
    @GetMapping("/api/v1/customer/{userId}")
    ResponseEntity<UserDto> readUserById(@PathVariable Long userId);
}