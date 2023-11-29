package ru.rutmiit.services.serv;

import org.modelmapper.ModelMapper;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.rutmiit.dto.dtooo.AddUserDto;
import ru.rutmiit.dto.dtooo.ShowDetailedUserInfoDto;
import ru.rutmiit.models.User;
import ru.rutmiit.repositories.repo.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private ModelMapper modelMapper;
    private UserRepository userRepository;



    public void  register(AddUserDto users) {

                User b = modelMapper.map(users, User.class);
            if (b.getId() == null || findUser(b.getId()).isEmpty()) {
                 modelMapper.map(userRepository.save(b), AddUserDto.class);
            }

    }


    public List<AddUserDto> getAll() {
        return userRepository.findAll().stream().map((s) -> modelMapper.map(s, AddUserDto.class)).collect(Collectors.toList());
    }


    public Optional<AddUserDto> findUser(String id) {
        return Optional.ofNullable(modelMapper.map(userRepository.findById(id), AddUserDto.class));
    }

    public ShowDetailedUserInfoDto userDetails(String userName) {
        return modelMapper.map(userRepository.findByUserName(userName).orElse(null), ShowDetailedUserInfoDto.class);
    }

    public void removeUser(String userName) {
        userRepository.deleteByUserName(userName);
    }


    public AddUserDto update(AddUserDto users) {
        Optional<User> existingUser = userRepository.findByUserName(users.getUserName());
        if (existingUser.isPresent()) {
            return modelMapper.map(userRepository.save(modelMapper.map(users, User.class)), AddUserDto.class);
        } else {
            throw new DataIntegrityViolationException("Exception of update");
        }
    }
}