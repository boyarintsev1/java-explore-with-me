package ru.practicum.ewm_main_service.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_main_service.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u.id, u.email, u.name FROM public.users u ORDER BY u.id",
            nativeQuery = true)
    Page<User> findAllUsers(Pageable page);

    @Query(value = "SELECT u.id, u.email, u.name FROM public.users u WHERE u.id IN (?1) ORDER BY u.id",
            nativeQuery = true)
    Page<User> findIdsUsers(Integer[] ids, Pageable page);

}

