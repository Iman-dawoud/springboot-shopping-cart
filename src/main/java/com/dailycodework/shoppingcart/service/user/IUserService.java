package com.dailycodework.shoppingcart.service.user;

import com.dailycodework.shoppingcart.dto.UserDto;
import com.dailycodework.shoppingcart.model.User;
import com.dailycodework.shoppingcart.request.CreateUserRequest;
import com.dailycodework.shoppingcart.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}