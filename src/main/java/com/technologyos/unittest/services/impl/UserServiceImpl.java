package com.technologyos.unittest.services.impl;

import com.technologyos.unittest.dtos.UserDto;
import com.technologyos.unittest.entities.UserEntity;
import com.technologyos.unittest.exceptions.UsersServiceException;
import com.technologyos.unittest.repositories.UserRepository;
import com.technologyos.unittest.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
   private final UserRepository usersRepository;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;


   @Override
   public UserDto createUser(UserDto user) {

      if (usersRepository.findByEmail(user.getEmail()) != null){
         throw new UsersServiceException("Record already exists");
      }

      ModelMapper modelMapper = new ModelMapper();
      UserEntity userEntity = modelMapper.map(user, UserEntity.class);

      String publicUserId = UUID.randomUUID().toString();
      userEntity.setUserId(publicUserId);
      userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

      UserEntity storedUserDetails = usersRepository.save(userEntity);

      return modelMapper.map(storedUserDetails, UserDto.class);
   }

   @Override
   public List<UserDto> getUsers(int page, int limit) {
      List<UserDto> returnValue = new ArrayList<>();

      if (page > 0) page -=1;

      Pageable pageableRequest = PageRequest.of(page, limit);

      Page<UserEntity> usersPage = usersRepository.findAll(pageableRequest);
      List<UserEntity> users = usersPage.getContent();

      Type listType = new TypeToken<List<UserDto>>() {}.getType();
      returnValue = new ModelMapper().map(users, listType);

      return returnValue;
   }

   @Override
   public UserDto getUser(String email) {
      UserEntity userEntity = usersRepository.findByEmail(email);

      if (userEntity == null)
         throw new UsernameNotFoundException(email);

      UserDto returnValue = new UserDto();
      BeanUtils.copyProperties(userEntity, returnValue);

      return returnValue;
   }

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      UserEntity userEntity = usersRepository.findByEmail(email);

      if (userEntity == null)
         throw new UsernameNotFoundException(email);

      return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
   }
}
