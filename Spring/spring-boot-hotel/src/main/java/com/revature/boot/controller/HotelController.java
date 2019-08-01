package com.revature.boot.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.boot.beans.Hotel;
import com.revature.boot.service.HotelService;

/*
 * @RestController: combines @Controller with @ResponseBody
 * 		- @ResponseBody - says we don't need the ViewResolver,
 * 		this is a REST API bro
 *
 * @RequestMapping: we can annotate the controller with this.
 * 		- allows us to specify the URI path that this controller
 * 		handles.
 * 		- we can also specify additional attributes:
 * 	consumes - MediaType of request body
 * 	produces - MediaType of response body
 * 
 */

@RestController
@RequestMapping(value = "/api/v1/rooms/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class HotelController {

	private static final Logger log = Logger.getLogger(HotelController.class);
	
	@Autowired
	private HotelService service;
	
	/*
	 * @RequestBody says to read the Http request body, parse it, and marshall it from
	 * JSON into a Java Object
	 */
	
	@PostMapping("/room")
	public ResponseEntity<Hotel> save(@RequestBody Hotel room) {
		return new ResponseEntity<Hotel>(service.save(room), HttpStatus.CREATED);
		// this is going to return a 201 if the record is successfully created
	}
	
	/*
	 * @PathVariable says to search the uri path for this variable's value
	 * 
	 * Example:
	 * 	@GetMapping("/path/{varName}")
	 * 	public Object findByPropertyName(@PathVariable Object varName)
	 * 
	 * You can have multiple path variables:
	 * Example: "/room/{id}/beds/{beds}"
	 */
	@GetMapping("/room/{id}")
	public Hotel findById(@PathVariable int id) {
		return service.findById(id);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Hotel>> findAll(){
		return new ResponseEntity<List<Hotel>>(service.findAll(), HttpStatus.OK);
	}
	
	@DeleteMapping("/room/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		service.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/price")
	public ResponseEntity<List<Hotel>> findByPriceRange(@RequestParam("lo") double low,
			@RequestParam("hi") double high){
		HttpHeaders headers = new HttpHeaders();
		headers.add("custom-header", "pickles");
		return ResponseEntity.ok()
				.header("my-header","this is a header")
				.headers(headers)
				.body(service.findByPriceRange(low, high));
		//return new ResponseEntity<List<Hotel>>(service.findByPriceRange(low,high),
		//		headers, HttpStatus.OK);
	}
}
