package com.tvdgapp.models.shipment;

import lombok.Getter;


public enum ShipmentStatus {

	PENDING("Pending"),
	SHIPPED("Shipped"),
	DELIVERED("Delivered"),
	CANCELLED("Cancelled"),
	BLOCKED("Blocked"),
	COMPLETED("Completed"),
	DRAFT("Draft"),
	IN_TRANSIT("In Transit"),
	BOOKED("Booked") ;
    @Getter
	private String display;


	ShipmentStatus(String display) {
		this.display = display;
		 }

}
