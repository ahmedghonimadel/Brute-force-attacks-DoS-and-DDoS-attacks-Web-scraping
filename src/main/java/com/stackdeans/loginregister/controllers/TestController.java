package com.stackdeans.loginregister.controllers;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {


  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess(HttpServletRequest httpRequest) {
    HttpSession session = httpRequest.getSession(true);
    String appKey =String.valueOf("<Security Key> ");
    Bucket bucket = (Bucket) session.getAttribute("throttler-" +   appKey);
    if (bucket == null) {
      bucket = createNewBucket();
      session.setAttribute("throttler-" + appKey, bucket);
    }
    boolean okToGo = bucket.tryConsume(1);
    if (okToGo) {
      return "Admin Board. your requests is less than 10 bucket algorithm";
    }
    else
      return "You have exceeded the 10 requests in 1 minute limit!" ;
  }

  public Bucket createNewBucket() {
    long capacity = 10;
    Refill refill = Refill.greedy(10, Duration.ofMinutes(1));
    Bandwidth limit = Bandwidth.classic(capacity, refill);
    return Bucket4j.builder().addLimit(limit).build();
  }

}


