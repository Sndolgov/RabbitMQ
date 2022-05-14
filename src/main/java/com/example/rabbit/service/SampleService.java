package com.example.rabbit.service;

import java.util.Random;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SampleService {
    private final Random random = new Random();


    @RabbitListener(queues = "queue1")
    public void processQueue1(String message) {
        log.info("Received from queue 1: " + message);
    }

    @RabbitListener(queues = "query-example-2")
    public void worker1(String message) throws InterruptedException {
        log.info("queue 2 worker 1 : " + message);
        Thread.sleep(100 * random.nextInt(20));
    }

    @RabbitListener(queues = "query-example-2")
    public void worker2(String message) throws InterruptedException {
        log.info("queue 2 worker 2 : " + message);
        Thread.sleep(100 * random.nextInt(20));
    }

    @RabbitListener(queues = "query-example-3-1")
    public void worker31(String message) {
        log.info("queue 3 accepted on worker 1 : " + message);
    }

    @RabbitListener(queues = "query-example-3-2")
    public void worker32(String message) {
        log.info("queue 3 accepted on worker 2 : " + message);
    }

    @RabbitListener(queues = "query-example-4-1")
    public void worker41(String message) {
        log.info("queue 4 accepted on worker 1 : " + message);
    }

    @RabbitListener(queues = "query-example-4-2")
    public void worker42(String message) {
        log.info("queue 4 accepted on worker 2 : " + message);
    }

    @RabbitListener(queues = "query-example-5-1")
    public void worker51(String message) {
        log.info("queue 5 accepted on worker 1 : " + message);
    }

    @RabbitListener(queues = "query-example-5-2")
    public void worker52(String message) {
        log.info("queue 5 accepted on worker 2 : " + message);
    }

    @RabbitListener(queues = "query-example-6")
    public String worker6(String message) throws InterruptedException {
        log.info("Received on worker : " + message);
        Thread.sleep(3000);
        return "Reply from worker : " + message;
    }
}
