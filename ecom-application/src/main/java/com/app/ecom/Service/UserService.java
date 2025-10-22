package com.app.ecom.Service;

import com.app.ecom.Dto.AddressDTO;
import com.app.ecom.Dto.userRequest;
import com.app.ecom.Dto.userResponse;
import com.app.ecom.Repository.UserRepository;
import com.app.ecom.model.user.Address;
import com.app.ecom.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

//    private List<User> userList = new ArrayList<>();
//    private Long nextId = 1L;

//    commeneted after using required agrs constructor annotation
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public List<userResponse> fetchAllUser() {
        return userRepository.findAll()
                .stream().map(this::maptoUserResponse)
                .collect(Collectors.toList());

    }

    public userRequest addUser(userRequest userReq) {
//        user.setId(nextId++);
        User user = new User();
        updateUserFromRequest(user, userReq);
        userRepository.save(user);
        return userReq;
//        return userList;

    }



    public Optional<userResponse> getUserById(Long id) {
        return userRepository.findById(id).map(this::maptoUserResponse);
    }

//        User us= new User();
//        for(User user: userList){
//            if (user.getId().equals(id)){
//                us.setId(user.getId());
//                us.setFirstName(user.getFirstName());
//                us.setLastName(user.getLastName());
//            }
//        }
//        return us;
// return userList.stream()
//         .filter(user -> user.getId().equals(id))
//         .findFirst(); will return optional  so change method return type too nplus object type

    public Boolean updateUserById(Long id, userRequest Updated) {
//        User us = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("NO element by id" + id));
//        us.setFirstName(Updated.getFirstName());
//        us.setLastName(Updated.getLastName());
//        us.setEmail(Updated.getEmail());
//        us.setPhone(Updated.getPhone());
//        User saved = userRepository.save(us);
//        return Optional.of(saved);

        return userRepository.findById(id)
                .map(existing -> {updateUserFromRequest(existing,Updated);
                userRepository.save(existing);
                return true;

        })
                .orElse(false);
    }
    private void updateUserFromRequest(User user, userRequest userReq) {
        user.setFirstName(userReq.getFirstName());
        user.setLastName(userReq.getLastName());
        user.setEmail(userReq.getEmail());
        user.setPhone(userReq.getPhone());
        if(userReq.getAddress()!=null){
            Address address= new Address();
            address.setStreet(userReq.getAddress().getStreet());
            address.setState(userReq.getAddress().getState());
            address.setCountry(userReq.getAddress().getCountry());
            address.setCity(userReq.getAddress().getCity());
            address.setZipcode(userReq.getAddress().getZipcode());
            user.setAddress(address);

        }

    }

    private userResponse maptoUserResponse(User user){
    userResponse userResponse= new userResponse();
    userResponse.setId(String.valueOf(user.getId()));
    userResponse.setFirstName(user.getFirstName());
    userResponse.setLastName(user.getLastName());
    userResponse.setEmail(user.getEmail());
    userResponse.setPhone(user.getPhone());
    userResponse.setUserRole(user.getUserRole());

    if(user.getAddress()!=null){
        AddressDTO addressDTO= new AddressDTO();
        addressDTO.setStreet(user.getAddress().getStreet());
        addressDTO.setCity(user.getAddress().getCity());
        addressDTO.setCountry(user.getAddress().getCountry());
        addressDTO.setState(user.getAddress().getState());
        addressDTO.setZipcode(user.getAddress().getZipcode());
        userResponse.setAddress(addressDTO);
    }
    return userResponse;

    }
}
//return userList.stream()
//        .filter(user -> user.getId().equals(id))
//            .findFirst()
//            .map(existingUser -> {
//        existingUser.setLastName(Updated.getLastName());
//        existingUser.setFirstName(Updated.getFirstName());
//        return true;
//
//    }).orElse(false);
//}

