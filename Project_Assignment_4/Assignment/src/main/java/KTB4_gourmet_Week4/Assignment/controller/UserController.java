/*
package KTB4_gourmet_Week4.Assignment.controller;

import KTB4_gourmet_Week4.Assignment.dto.UserRequestDto;
import KTB4_gourmet_Week4.Assignment.dto.UserResponseDto;
import KTB4_gourmet_Week4.Assignment.entity.User;
import KTB4_gourmet_Week4.Assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;


    @PostMapping
    @Transactional
    public UserResponseDto createUser(@RequestBody UserRequestDto request) {
        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getNickname()
        );
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponseDto(user);
    }

    @PutMapping("/{userId}")
    @Transactional
    public UserResponseDto updateNickname(
            @PathVariable Long userId,
            @RequestBody UserRequestDto request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.changeNickname(request.getNickname());
        return new UserResponseDto(user);
    }

    @DeleteMapping("/{userId}")
    @Transactional
    public void deleteUser(@PathVariable Long userId) {
        userRepository.deleteById(userId);
    }

}
*/

package KTB4_gourmet_Week4.Assignment.controller;

import KTB4_gourmet_Week4.Assignment.dto.LoginRequestDto;
import KTB4_gourmet_Week4.Assignment.dto.UserRequestDto;
import KTB4_gourmet_Week4.Assignment.dto.UserResponseDto;
import KTB4_gourmet_Week4.Assignment.entity.User;
import KTB4_gourmet_Week4.Assignment.store.MemoryStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto signup(@RequestBody UserRequestDto request) {
        Long id = MemoryStore.userId++;

        User user = new User(
                id,
                request.getEmail(),
                request.getPassword(),
                request.getNickname()
        );

        MemoryStore.users.put(id, user);

        return new UserResponseDto(user);
    }

    @PostMapping("/login")
    public UserResponseDto login(@RequestBody LoginRequestDto request) {
        User user = MemoryStore.users.values().stream()
                .filter(findUser -> findUser.getEmail().equals(request.getEmail()))
                .filter(findUser -> findUser.getPassword().equals(request.getPassword()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("email or password is invalid"));

        return new UserResponseDto(user);
    }

    @GetMapping
    public List<UserResponseDto> getUsers() {
        return MemoryStore.users.values().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId) {
        User user = MemoryStore.users.get(userId);

        if (user == null) {
            throw new IllegalArgumentException("user not found");
        }

        return new UserResponseDto(user);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(
            @PathVariable Long userId,
            @RequestBody UserRequestDto request
    ) {
        User user = MemoryStore.users.get(userId);

        if (user == null) {
            throw new IllegalArgumentException("user not found");
        }

        user.update(request.getNickname());

        return new UserResponseDto(user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        MemoryStore.users.remove(userId);
    }
}
