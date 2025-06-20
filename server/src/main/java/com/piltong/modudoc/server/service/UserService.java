package com.piltong.modudoc.server.service;

import com.piltong.modudoc.server.model.User;
import com.piltong.modudoc.server.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * [UserService]
 * 유저 도메인에 대한 비즈니스 로직을 처리하는 클래스.
 * - 유저 생성, 수정, 조회, 삭제 기능을 제공한다.
 * - 입력 검증, 예외 처리, 로깅을 포함한다.
 * - 영속 계층(UserRepository)에 직접 접근하지 않고 이 서비스를 통해 조작한다.
 */
public class UserService {

    private final UserRepository userRepo;
    private static final Logger log = LogManager.getLogger(UserService.class);

    // 생성자: Repository 넣기
    public UserService(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    /**
     * 유저를 생성하거나 수정
     * @param user 유저 객체
     * @return 저장된 유저 객체 (id가 자동 채워질 수 있음)
     */
    public User save(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            String msg = "Invalid user data";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        return userRepo.save(user);
    }

    /**
     * 유저 조회
     * @param id 유저 ID
     * @return Optional<User>
     */
    public Optional<User> findById(String id) {
        if (id == null || id.isBlank()) {
            String msg = "User ID must not be null or blank.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        return userRepo.findById(id);
    }

    /**
     * 모든 유저 조회
     * @return 유저 리스트
     */
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * 유저 삭제
     * @param id 유저 ID
     */
    public void delete(String id) {
        if (id == null || id.isBlank()) {
            String msg = "User ID must not be null or blank.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (!userRepo.delete(id)) {
            String msg = "Failed to delete user with ID: " + id;
            log.error(msg);
            throw new RuntimeException(msg);
        }

        log.info("User deleted: {}", id);
    }

    /**
     * 유저 존재 여부 확인
     * @param id 유저 ID
     * @return 존재 여부
     */
    public boolean exists(String id) {
        return userRepo.findById(id).isPresent();
    }
}
