package com.example.rabbit.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SampleController {
    @Autowired
    private RabbitTemplate template;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Empty mapping";
    }

    @GetMapping("/queue1")
    public String queue1() {
        log.info("Emit to queue 1");
        template.convertAndSend("queue1","Message to queue");
        return "Emit to queue 1";
    }

    @RequestMapping("/queue2")
    @ResponseBody
    String queue2() {
        log.info("Emit to queue 2");
        for(int i = 0;i<10;i++) {
            template.convertAndSend("query-example-2", "Message " + i);
        }
        return "Emit to queue 2";
    }

    @RequestMapping("/queue3")
    @ResponseBody
    String queue3() {
        log.info("Emit to exchange-example-3");
        template.setExchange("exchange-example-3");
        template.convertAndSend("Fanout message");
        return "Emit to exchange-example-3";
    }

    @RequestMapping("/queue4")
    @ResponseBody
    String queue4() {
        template.setExchange("direct-exchange-example-4");
        template.convertAndSend("error", "Error");
        template.convertAndSend("info", "Info");
        template.convertAndSend("warning", "Warning");
        return "Emit to direct-exchange-example-4";
    }

    @RequestMapping("/queue5")
    @ResponseBody
    String queue5() {
        template.setExchange("topic-exchange-example-5");
        template.convertAndSend("fast.orange.fox", "fast.orange.fox");
        template.convertAndSend("small.funny.rabbit", "small.funny.rabbit");
        template.convertAndSend("lazy.lion", "lazy.lion");
        return "topic-exchange-example-5";
    }

    @RequestMapping("/queue6")
    @ResponseBody
    String queue6() {
        template.setReplyTimeout(60 * 1000);
        String message = "Emit Send And Receive";
        log.info(message);
        String response = (String) template.convertSendAndReceive("query-example-6", message);
        log.info(String.format("Received on producer '%s'",response));
        return "returned from worker : " + response;
    }
}
