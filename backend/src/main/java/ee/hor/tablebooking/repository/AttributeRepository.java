package ee.hor.tablebooking.repository;

import ee.hor.tablebooking.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttributeRepository extends JpaRepository<AttributeEntity, UUID> {
}