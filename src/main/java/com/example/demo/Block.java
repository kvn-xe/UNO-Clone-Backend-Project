package com.example.demo;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/block")
public class Block {
  HashMap<String, String> table = new HashMap<>();

  @GetMapping("/{id}")
  public String getMethodName(@PathVariable String id) {
    table.put("1", "First Block");
    String res = table.get(id);

    if (res == null || res.equals("")) {
      return "There are no Blocks";
    }
    return res;
  }
  
}
