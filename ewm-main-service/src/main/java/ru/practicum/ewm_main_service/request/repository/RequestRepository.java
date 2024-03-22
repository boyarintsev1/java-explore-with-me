package ru.practicum.ewm_main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_main_service.request.entity.ParticipationRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query(value = "SELECT count(event) from public.P_Requests where event=?1 and status=?2", nativeQuery = true)
    Long countAllByEventAndStatus(Long eventId, String toString);

    @Query("SELECT new ru.practicum.ewm_main_service.request.entity.ParticipationRequest (r.id , r.created, r.event, " +
            "r.requester, r.status) " +
            "from ParticipationRequest as r " +
            "WHERE r.event.initiator.id <> ?1 AND r.requester.id = ?1 " +
            "ORDER BY r.id")
    List<ParticipationRequest> findRequestsByUserId(Long userId);

    @Query("SELECT new ru.practicum.ewm_main_service.request.entity.ParticipationRequest (r.id , r.created, r.event, " +
            "r.requester, r.status) " +
            "from ParticipationRequest as r " +
            "WHERE r.event.id = ?2 AND r.event.initiator.id = ?1 " +
            "ORDER BY r.id")
    List<ParticipationRequest> findRequestsToEventOfCurrentUser(Long userId, Long eventId);
}

