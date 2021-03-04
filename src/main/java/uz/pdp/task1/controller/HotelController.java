package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.entity.Hotel;
import uz.pdp.task1.repository.HotelRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    HotelRepository hotelRepository;

   @PostMapping
    public String addHotel(@RequestBody Hotel hotel){
        boolean exist = hotelRepository.existsByName(hotel.getName());
        if (exist) return "The hotel already exists in database";
        hotelRepository.save(hotel);
        return "Hotel added";

    }
    @GetMapping
    public List<Hotel> getHotels(){
        return hotelRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteHotel(@PathVariable Integer id){
      try{
          hotelRepository.deleteById(id);
          return "Hotel deleted";
      }catch (Exception e){
          return "Hotel not deleted. Error: "+e.getMessage();
      }
    }

    @PutMapping("/{id}")
    public String editHotel(@PathVariable Integer id, @RequestBody Hotel hotel){
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (optionalHotel.isPresent()){
            Hotel hotel1 = optionalHotel.get();
            hotel1.setName(hotel.getName());
            hotelRepository.save(hotel1);
            return "Hotel edited successfully";
        }
        return "Hotel not found!";
    }
}
