package deviceet.business.device;

import deviceet.IntegrationTest;
import deviceet.TestRandomExternalEventGenerator;
import deviceet.business.device.command.ConfigureDeviceNameCommand;
import deviceet.business.device.command.DeviceCommandService;
import deviceet.business.device.domain.Device;
import deviceet.business.device.domain.DeviceReference;
import deviceet.business.device.domain.DeviceRepository;
import deviceet.business.device.domain.event.DeviceCreatedEvent;
import deviceet.business.device.domain.event.DeviceNameConfiguredEvent;
import deviceet.business.device.query.DeviceQueryService;
import deviceet.business.device.query.ListDeviceQuery;
import deviceet.business.device.query.QListedDevice;
import deviceet.common.notification.NotificationService;
import deviceet.common.security.Principal;
import deviceet.external.ExternalDeviceCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.stream.IntStream;

import static deviceet.common.event.DomainEventType.DEVICE_CREATED_EVENT;
import static deviceet.common.event.DomainEventType.DEVICE_NAME_CONFIGURED_EVENT;
import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DeviceIntegrationTest extends IntegrationTest {
    @Autowired
    private DeviceCommandService deviceCommandService;

    @Autowired
    private DeviceQueryService deviceQueryService;

    @Autowired
    private DeviceRepository deviceRepository;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    public void should_create_device_upon_external_device_created_event() {
        ExternalDeviceCreatedEvent externalDeviceCreatedEvent = TestRandomExternalEventGenerator.createExternalDeviceCreatedEvent();
        eventConsumer.consumeExternalEvent(externalDeviceCreatedEvent);

        Device device = deviceRepository.byId(externalDeviceCreatedEvent.getDeviceId(), externalDeviceCreatedEvent.getOrgId());
        assertEquals(externalDeviceCreatedEvent.getDeviceName(), device.getReportedName());
        assertEquals(externalDeviceCreatedEvent.getDeviceName(), device.getConfiguredName());
        assertEquals(externalDeviceCreatedEvent.getOsType(), device.getOsType());
        assertEquals(externalDeviceCreatedEvent.getCpuArchitecture(), device.getCpuArchitecture());

        //Verify DeviceCreatedEvent raised
        DeviceCreatedEvent internalDeviceCreatedEvent = latestEventFor(device.getId(), DEVICE_CREATED_EVENT, DeviceCreatedEvent.class);
        assertEquals(device.getId(), internalDeviceCreatedEvent.getDeviceId());
    }

    @Test
    public void should_send_email_upon_device_created_event() {
        ExternalDeviceCreatedEvent externalDeviceCreatedEvent = TestRandomExternalEventGenerator.createExternalDeviceCreatedEvent();
        eventConsumer.consumeExternalEvent(externalDeviceCreatedEvent);

        DeviceCreatedEvent internalDeviceCreatedEvent = latestEventFor(externalDeviceCreatedEvent.getDeviceId(), DEVICE_CREATED_EVENT, DeviceCreatedEvent.class);
        eventConsumer.consumeDomainEvent(internalDeviceCreatedEvent);
        verify(notificationService, times(1)).notifyOnDeviceCreated(internalDeviceCreatedEvent.getDeviceId());
    }

    @Test
    public void should_configure_device_name() {
        ExternalDeviceCreatedEvent externalDeviceCreatedEvent = TestRandomExternalEventGenerator.createExternalDeviceCreatedEvent();
        eventConsumer.consumeExternalEvent(externalDeviceCreatedEvent);
        Principal principal = createPrincipal(externalDeviceCreatedEvent.getOrgId());
        ConfigureDeviceNameCommand configureDeviceNameCommand = ConfigureDeviceNameCommand.builder().name("newName").build();
        deviceCommandService.configureDeviceName(externalDeviceCreatedEvent.getDeviceId(), configureDeviceNameCommand, principal);
        Device device = deviceRepository.byId(externalDeviceCreatedEvent.getDeviceId(), externalDeviceCreatedEvent.getOrgId());
        assertEquals(configureDeviceNameCommand.name(), device.getConfiguredName());
        assertNotEquals(configureDeviceNameCommand.name(), device.getReportedName());

        DeviceNameConfiguredEvent internalDeviceCreatedEvent = latestEventFor(device.getId(), DEVICE_NAME_CONFIGURED_EVENT, DeviceNameConfiguredEvent.class);
        assertEquals(configureDeviceNameCommand.name(), internalDeviceCreatedEvent.getNewName());
    }

    @Test
    public void should_cache_org_devices() {
        ExternalDeviceCreatedEvent externalDeviceCreatedEvent = TestRandomExternalEventGenerator.createExternalDeviceCreatedEvent();
        eventConsumer.consumeExternalEvent(externalDeviceCreatedEvent);
        String key = "Cache:ORG_DEVICES::" + externalDeviceCreatedEvent.getOrgId();

        assertFalse(stringRedisTemplate.hasKey(key));
        List<DeviceReference> cachedDeviceReferences = deviceRepository.cachedDeviceReferences(externalDeviceCreatedEvent.getOrgId());
        assertFalse(cachedDeviceReferences.isEmpty());
        assertTrue(stringRedisTemplate.hasKey(key));

        Device device = deviceRepository.byId(externalDeviceCreatedEvent.getDeviceId(), externalDeviceCreatedEvent.getOrgId());
        deviceRepository.save(device);
        assertFalse(stringRedisTemplate.hasKey(key));
    }

    @Test
    public void should_list_devices() {
        String orgId = "ORG" + newSnowflakeId();
        IntStream.range(0, 14).forEach(i -> eventConsumer.consumeExternalEvent(TestRandomExternalEventGenerator.createExternalDeviceCreatedEvent(orgId)));
        Page<QListedDevice> devices = deviceQueryService.listDevices(new ListDeviceQuery(null, null), PageRequest.of(0, 10), createPrincipal(orgId));
        assertEquals(14, devices.getTotalElements());
        assertEquals(10, devices.getContent().size());
    }

}