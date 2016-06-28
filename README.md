# ticket-booking-service
---
Implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

This application is developed using Spring Boot, Spring JDBC, Spring RESTful web services

### Assumptions
---
1. Users are provided seats based on the availability.
2. No seat numbers.
3. Hold time for the seats is 60 seconds. If the user doesn't reserve the seats before 60 seconds, then the holds are removed and user has to send a request again to hold the seats.
4. User can hold and reserve the seats at multiple levels by providing the minLevel and maxLevel.


### RESTful Web Services
---

1.	Find the number of seats available within the venue, optionally by seating level
	Note: available seats are seats that are neither held nor reserved.
	* Total seats available in all venues:
	
		```
		GET - http://localhost:8080/ticket-booking-service/v1/venue/seats
		```
		
		![numSeatsAvailable](https://github.com/vamshins/ticket-booking-service/blob/master/img/numSeatsAvailable.JPG)
	* Number of seats available at a particular level:
		
		```
		GET - http://localhost:8080/ticket-booking-service/v1/venue/seats/{levelId}
		```
		
		![numSeatsAvailableAtLevel](https://github.com/vamshins/ticket-booking-service/blob/master/img/numSeatsAvailableAtLevel.JPG)
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
	
	![holdSeats](https://github.com/vamshins/ticket-booking-service/blob/master/img/holdSeats.JPG)
	
	This request will expire after 60 seconds. User has to reserve the seats using the web service in the following request.
	
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
	
	![reserveSeats](https://github.com/vamshins/ticket-booking-service/blob/master/img/reserveSeats.JPG)
	
	

