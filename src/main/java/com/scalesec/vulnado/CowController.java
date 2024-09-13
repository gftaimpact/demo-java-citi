package com.scalesec.vulnado;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.*;


@RestController
@EnableAutoConfiguration
public class CowController {
    @RequestMapping(value = "/cowsay")
    String cowsay(@RequestParam(defaultValue = "I love Linux!") String input) {
        return Cowsay.run(input);
    }
}
