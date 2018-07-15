package callable;

import message.IMessage;
import utils.LifeITUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;

import static java.lang.Thread.sleep;
import static utils.LifeITUtils.log;

public class TaxiThread implements Runnable {

    private final IMessage message;
    private static final int SLEEP_DURATION = 3000;

    public TaxiThread(final IMessage message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            log(Level.INFO, LocalDateTime.now() + " Driver " + Thread.currentThread().getName() + " STARTED drives client " + message.getDispatched());
            saveMessage();
            sleep(SLEEP_DURATION);
            log(Level.INFO, LocalDateTime.now() + " Driver " + Thread.currentThread().getName() + " FINISHED drives client " + message.getDispatched());
        } catch (InterruptedException | IOException e) {
            log(Level.SEVERE, "Error in taxi driver thread");
            throw new RuntimeException("Error in taxi driver thread", e);
        }
    }

    private void saveMessage() throws IOException {
        LifeITUtils.saveMessage(Thread.currentThread().getName(), message);
    }
}
