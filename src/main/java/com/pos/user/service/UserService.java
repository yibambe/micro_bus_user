package com.pos.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pos.user.api.dto.UserCreateRequest;
import com.pos.user.api.dto.UserResponse;
import com.pos.user.api.dto.UserUpdateRequest;
import com.pos.user.domain.User;
import com.pos.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getName());
    }

    public Page<UserResponse> list(String q, Pageable pageable) {
        Page<User> page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNameContainingIgnoreCase(q.trim(), pageable);

        return page.map(this::toResponse);
    }

    public UserResponse get(String id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
        return toResponse(user);
    }

    public UserResponse create(UserCreateRequest req) {
        User user = User.builder()
                .name(req.name().trim())
                .build();

        User saved = repo.save(user);
        return toResponse(saved);
    }

    public UserResponse update(String id, UserUpdateRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));

        user.setName(req.name().trim());
        User saved = repo.save(user);
        return toResponse(saved);
    }

    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
        }
        repo.deleteById(id);
    }
}
