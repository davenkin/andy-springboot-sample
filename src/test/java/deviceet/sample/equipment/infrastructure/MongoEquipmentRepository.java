package deviceet.sample.equipment.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoEquipmentRepository extends AbstractMongoRepository<Equipment> implements EquipmentRepository {
}
