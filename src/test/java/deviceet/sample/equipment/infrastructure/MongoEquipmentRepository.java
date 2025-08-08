package deviceet.sample.equipment.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.EquipmentSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MongoEquipmentRepository extends AbstractMongoRepository<Equipment> implements EquipmentRepository {
    private final CachedMongoEquipmentRepository cachedMongoEquipmentRepository;

    @Override
    public List<EquipmentSummary> cachedEquipmentSummaries(String orgId) {
        return cachedMongoEquipmentRepository.cachedEquipmentSummaries(orgId);
    }

    @Override
    public void evictCachedEquipmentSummaries(String orgId) {
        cachedMongoEquipmentRepository.evictCachedEquipmentSummaries(orgId);
    }
}
