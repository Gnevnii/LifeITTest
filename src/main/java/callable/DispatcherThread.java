package callable;

import bean.PhoneCall;
import service.TaxiDriverService;

import java.util.concurrent.Callable;

public class DispatcherThread implements Callable<Integer> {

    private final PhoneCall phoneCall;

    public DispatcherThread(final PhoneCall phoneCall) {
        this.phoneCall = phoneCall;
    }

    private Integer proccessPhoneCall() {
        int taxiId = phoneCall.getMessage().getTaxiId();
        TaxiDriverService.get().submitMessageToTaxiDriverById(taxiId, phoneCall.getMessage());
        return taxiId;
    }

    @Override
    public Integer call() {
        return proccessPhoneCall();
    }
}
