package ee.hor.tablebooking.repository;

import ee.hor.tablebooking.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TableRepository extends JpaRepository<TableEntity, UUID> {
    List<TableEntity> findAllByRestaurantId(UUID id);

}
