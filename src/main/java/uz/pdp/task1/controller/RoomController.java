package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.entity.Hotel;
import uz.pdp.task1.entity.Room;
import uz.pdp.task1.payload.RoomDto;
import uz.pdp.task1.repository.HotelRepository;
import uz.pdp.task1.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {


    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    RoomRepository roomRepository;

    @PostMapping
    public String addRoom(@RequestBody RoomDto roomDto) {

        Room room = new Room();
        room.setFloor(roomDto.getFloor());
        room.setSize(roomDto.getSize());
        room.setNumber(roomDto.getNumber());
        Optional<Hotel> hotelOptional = hotelRepository.findById(roomDto.getHotelId());
        if (!hotelOptional.isPresent()) return "Hotel not found";
        room.setHotel(hotelOptional.get());
        roomRepository.save(room);
        return "Room added";

    }

   @GetMapping("/byHotelId/{hotelId}")
    public Page<Room> getRoomsByHotelId(@PathVariable Integer hotelId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 2);
        return roomRepository.findAllByHotelId(hotelId, pageable);
    }
    @GetMapping
    public List<Room> getRooms(){
        return roomRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable Integer id){
        try{
            roomRepository.deleteById(id);
            return "Room deleted";
        }catch (Exception e){
            return "Room not deleted. Error: "+e.getMessage();
        }


    }
    @PutMapping("/{id}")
    public String editRoom(@PathVariable Integer id, @RequestBody RoomDto roomDto){
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()){
            Room room = roomOptional.get();
            room.setSize(roomDto.getSize());
            room.setNumber(roomDto.getNumber());
            room.setFloor(roomDto.getFloor());
            Optional<Hotel> hotelOptional = hotelRepository.findById(roomDto.getHotelId());
            if (!hotelOptional.isPresent()) return "Hotel not found!";
            room.setHotel(hotelOptional.get());
            roomRepository.save(room);
            return "Room edited successfully";
        }
        return "Room not found!";
    }

}
