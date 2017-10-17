package com.rest.service.controller;

import com.rest.service.model.Items;
import com.rest.service.utils.ItemsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/items")

public class ItemsController {

  @Autowired
    private ItemsUtil itemsUtil;

    @RequestMapping(method = RequestMethod.GET)
    public Set<Items> getItems() {
        return itemsUtil.findItems();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> setItems(@RequestBody Items items) throws Exception {
        itemsUtil.add(items);
        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

}