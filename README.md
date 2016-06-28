# ticket-booking-service
---
Implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a performance venue.

This application is developed using Spring Boot, Spring JDBC, Spring RESTful web services

1.	Find the number of seats available within the venue, optionally by seating level
	Note: available seats are seats that are neither held nor reserved.
	* Total seats available in all venues:
	
		```
		GET - http://localhost:8080/ticket-booking-service/v1/venue/seats
		```
	* Number of seats available at a particular level:
		
		```
		GET - http://localhost:8080/ticket-booking-service/v1/venue/seats/{levelId}
		```
2.	Find and hold the best available seats on behalf of a customer, potentially limited to specific levels
	Note: each ticket hold should expire within a set number of seconds.
	
	```
	POST - http://localhost:8080/ticket-booking-service/v1/venue/seats/hold
	```
	
	RequestBody:
	```
	{
	  "numSeats": "6250",
	  "customerEmail": "vamshi.krishna588@gmail.com",
	  "minLevel": "1",
	  "maxLevel": "4"
	}
	```
	
	ResponseEntity:
	```
	{
	  "id": 1,
	  "customerEmail": "vamshi.krishna588@gmail.com",
	  "seatHoldVenueDetailList": [
		{
		  "level": 1,
		  "numberOfSeatHolds": 1250
		},
		{
		  "level": 2,
		  "numberOfSeatHolds": 2000
		},
		{
		  "level": 3,
		  "numberOfSeatHolds": 1500
		},
		{
		  "level": 4,
		  "numberOfSeatHolds": 1500
		}
	  ]
	}
	```
3.	Reserve and commit a specific group of held seats for a customer

	```
	POST - http://localhost:8080/ticket-booking-service/v1/venue/seats/reserve
	```
	
	RequestBody:
	```
	{
	  "seatHoldId": "1",
	  "customerEmail": "vamshi.krishna588@gmail.com"
	}
	```
	
	ResponseEntity:
	```
	{
	  "seatHoldId": 1,
	  "customerEmail": "vamshi.krishna588@gmail.com",
	  "confirmationCode": "0879edae-2dd3-4c59-9df3-604edc5a4623"
	}
	```