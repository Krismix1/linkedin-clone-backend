package org.autopotato.linkedinclonebackend.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("message")
public class MessageController {

    @GetMapping
    public Map<Object, Object> handle() {
        Map<Object, Object> result = new HashMap<>();

        result.put("message", "hola nino");

        return result;
    }
}
