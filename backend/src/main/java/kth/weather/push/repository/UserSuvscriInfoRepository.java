package kth.weather.push.repository;

import jakarta.transaction.Transactional;
import kth.weather.push.entity.UserSubscriInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSuvscriInfoRepository extends JpaRepository<UserSubscriInfo, Long> {
    /* 구독자 정보를 배열로 가지고 온다. */
    List<UserSubscriInfo> findByP256dh(String p256dh);

    /* 구독취소시 삭제 처리 한다. */
    @Transactional
    int deleteByP256dh(String p256dh);
}
