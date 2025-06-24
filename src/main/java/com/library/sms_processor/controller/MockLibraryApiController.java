package com.library.sms_processor.controller;

import com.library.sms_processor.model.OverdueBookInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mock")
public class MockLibraryApiController {

    @GetMapping("/overdueBooks")
    public List<OverdueBookInfo> getMockData () {
        return List.of(
                new OverdueBookInfo("16478777783", LocalDate.now().minusDays(4)),
                new OverdueBookInfo("16478497711", LocalDate.now().minusDays(1))

        );
    }

}
