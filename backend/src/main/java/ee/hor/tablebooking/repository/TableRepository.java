package ee.hor.tablebooking.repository;

import ee.hor.tablebooking.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TableRepository extends JpaRepository<TableEntity, UUID> {
}
