package com.library.sms_processor.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchResult {

    private List<String> result;

}
