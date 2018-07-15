import bean.PhoneCall;
import utils.LifeITUtils;
import com.google.common.collect.Maps;
import message.Message;
import service.DispatcherService;
import service.TaxiDriverService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class Test {

    private static final HashMap<String, PhoneCall> CLIENT_CALLS = Maps.newHashMap();
    private static final int TOTAL_CALLS = 200;
    private final int clients;
    private final int taxiDrivers;
    private final MessageCreationType messageCreationType;

    public Test(final int clients, final int taxiDrivers, final MessageCreationType type) {
        this.clients = clients;
        this.taxiDrivers = taxiDrivers;
        this.messageCreationType = type;

        initServices();
        initPhoneCalls();
        clearPreviousResults();
    }

    private void initServices() {
        TaxiDriverService.configureService(taxiDrivers);
        DispatcherService.configureService(clients);
    }

    private void initPhoneCalls() {
        CLIENT_CALLS.clear();
        switch (messageCreationType) {
            case FROM_XML:
                CLIENT_CALLS.putAll(getClientCallsFromGeneratedFiles());
                break;
            case GENERATE:
                CLIENT_CALLS.putAll(createClientCallsObjects());
                break;
            default:
                //do nothing
        }
    }

    private Map<String, PhoneCall> getClientCallsFromGeneratedFiles() {
        try {
            return LifeITUtils.generateInitialCallXml(TOTAL_CALLS);
        } catch (IOException e) {
            throw new RuntimeException("Error generating xml files", e);
        }
    }

    private Map<String, PhoneCall> createClientCallsObjects() {
        Map<String, PhoneCall> resultMap = Maps.newHashMap();

        Random r = new Random();
        for (int i = 0; i < TOTAL_CALLS; i++) {
            int randomTaxiDriverId = r.nextInt(11);
            final String clientId = "callerId-" + i;
            PhoneCall call = new PhoneCall(clientId, new Message(randomTaxiDriverId));

            resultMap.put(clientId, call);
        }

        return resultMap;
    }

    private void clearPreviousResults() {
        try {
            LifeITUtils.clearFolders();
        } catch (IOException e) {
            LifeITUtils.log(Level.SEVERE, "Error cleaning output directory");
            throw new RuntimeException("Error cleaning output directory", e);
        }
    }

    public void start() {
        for (Map.Entry<String, PhoneCall> phoneCallEntry : CLIENT_CALLS.entrySet()) {
            DispatcherService.get().processSinglePhoneCall(phoneCallEntry.getValue());
            System.out.println(String.format(LocalDateTime.now() + " Клиент %s назначен таксисту %s", phoneCallEntry.getKey(), phoneCallEntry.getValue().getMessage().getTaxiId()));
        }
    }
}
