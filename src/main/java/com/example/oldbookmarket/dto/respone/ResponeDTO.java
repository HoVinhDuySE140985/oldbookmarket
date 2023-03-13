package com.example.oldbookmarket.dto.respone;

import com.example.oldbookmarket.enumcode.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponeDTO {
    private SuccessCode successCode;
    private Object data;
}
