package com.driver.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;

@Repository
public class UserRepository{
//	name and hotel
	HashMap<String,Hotel> hotelsDb=new HashMap<>();
	
//	adharCardNo and user
	HashMap<Integer,User> usersDb=new HashMap<>();
	
//	uuid and booking
	HashMap<String,Booking> bookingsDb=new HashMap<>();
	
	
	public String addHotel(Hotel hotel){
		if(hotel==null || hotel.getHotelName()==null || hotelsDb.containsKey(hotel)) return "FAILURE";
		hotelsDb.put(hotel.getHotelName(), hotel);
		return "SUCCESS";
	}
	
	 public Integer addUser( User user){
		 usersDb.put(user.getaadharCardNo(), user);
		 return user.getaadharCardNo();
		 
	 }
	 
	 public String getHotelWithMostFacilities() {
		 int max=-1;
		 for(String name:hotelsDb.keySet()) {
			 if(hotelsDb.get(name).getFacilities().size()>max) {
				 max=hotelsDb.get(name).getFacilities().size();
			 }
		 }
		 if(max<1) return "";
		 List<String> l=new ArrayList<>();
		 
		 for(String name:hotelsDb.keySet()) {
			 if(hotelsDb.get(name).getFacilities().size()==max) {
				 l.add(name);
			 }
		 }
		 
		 Collections.sort(l);
		 
		 return l.get(0);
		  
	 }
	 
	 public int bookARoom(Booking booking){
		 String hotelName=booking.getHotelName();
		 
		 int roomsBooked=booking.getNoOfRooms();
		 
		 if(roomsBooked<hotelsDb.get(hotelName).getAvailableRooms()) return -1;
		 
		 int leftRooms=hotelsDb.get(hotelName).getAvailableRooms()-roomsBooked;
		 
		 hotelsDb.get(hotelName).setAvailableRooms(leftRooms);
		 
		 int totalamount=hotelsDb.get(hotelName).getPricePerNight()*roomsBooked;
		 
		 booking.setAmountToBePaid(totalamount);
		 
		String bookingId= UUID.randomUUID().toString();
		
		booking.setBookingId(bookingId);
		
		bookingsDb.put(bookingId, booking);
		
		return totalamount;
		 
	 }
	 
	 public int getBookings(Integer aadharCard) {
		 int totalBookings=0;
		 for(String uuid:bookingsDb.keySet()) {
			 if(bookingsDb.get(uuid).getBookingAadharCard()==aadharCard) totalBookings++;
		 }
		 return totalBookings;
	 }
	 
	 public Hotel updateFacilities(List<Facility> newFacilities,String hotelName) {
		  Set<Facility> newUniqueFacilities=new HashSet<>();
		 for(Facility f:newFacilities) {
			 if(!hotelsDb.get(hotelName).getFacilities().contains(f)) newUniqueFacilities.add(f); 
		 }
		 for(Facility f:newUniqueFacilities) {
			 hotelsDb.get(hotelName).getFacilities().add(f);
		 }
		 return hotelsDb.get(hotelName);
	 }

}