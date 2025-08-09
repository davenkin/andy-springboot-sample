package deviceet.sample.equipment.infrastructure;

import deviceet.sample.equipment.domain.EquipmentSummary;

import java.util.List;

// Some json deserialization techniques requires a wrapper outside List
public record CachedOrgEquipmentSummaries(List<EquipmentSummary> summaries) {
}
