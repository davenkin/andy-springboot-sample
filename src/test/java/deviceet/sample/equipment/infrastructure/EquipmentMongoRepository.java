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
public class EquipmentMongoRepository extends AbstractMongoRepository<Equipment> implements EquipmentRepository {
    private final CachedEquipmentMongoRepository cachedEquipmentMongoRepository;

    @Override
    public List<EquipmentSummary> cachedEquipmentSummaries(String orgId) {
        return cachedEquipmentMongoRepository.cachedEquipmentSummaries(orgId);
    }

    @Override
    public void evictCachedEquipmentSummaries(String orgId) {
        cachedEquipmentMongoRepository.evictCachedEquipmentSummaries(orgId);
    }
}
