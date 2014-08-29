package ai.autonumber.gcm;


import ai.autonumber.db.UserDao;
import ai.autonumber.gcm.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CommunicationHelper {

  private static Sender sender = new Sender("AIzaSyDt6HY91byxWK99vhhriyXxEXfKFyH3kcU");
  private static final int MULTICAST_SIZE = 1000;


  private static final Executor threadPool = Executors.newFixedThreadPool(5);
  protected final Logger logger = Logger.getLogger(getClass().getName());

  private static CommunicationHelper instance = null;

  public static CommunicationHelper getInstance() {
    if(instance == null)
      instance = new CommunicationHelper();
    return instance;
  }

  private CommunicationHelper() {
  }

  public void sendMessageTo(String deviceId, Message message) {
    // send a single message using plain post
    try {
      sender.send(message, deviceId, 5);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendMessageTo(List<String> devices, Message message) {
    // send a multicast message using JSON
    // must split in chunks of 1000 devices (GCM limit)
    int total = devices.size();
    List<String> partialDevices = new ArrayList<>(total);
    int counter = 0;
    for (String device : devices) {
      counter++;
      partialDevices.add(device);
      int partialSize = partialDevices.size();
      if (partialSize == MULTICAST_SIZE || counter == total) {
        asyncSend(partialDevices, message);
        partialDevices.clear();
      }
    }
  }


  private void asyncSend(List<String> partialDevices, final Message message) {
    // make a copy
    final List<String> devices = new ArrayList<>(partialDevices);
    threadPool.execute(new Runnable() {

      public void run() {
        MulticastResult multicastResult;
        try {
          multicastResult = sender.send(message, devices, 5);
        } catch (IOException e) {
          logger.log(Level.SEVERE, "Error posting messages", e);
          return;
        }
        List<Result> results = multicastResult.getResults();
        // analyze the results
        for (int i = 0; i < devices.size(); i++) {
          String regId = devices.get(i);
          Result result = results.get(i);
          String messageId = result.getMessageId();
          if (messageId != null) {
            logger.fine("Succesfully sent message to device: " + regId +
                "; messageId = " + messageId);
            String canonicalRegId = result.getCanonicalRegistrationId();
            if (canonicalRegId != null) {
              // same device has more than on registration id: update it
              logger.info("canonicalRegId " + canonicalRegId);
              UserDao.updateRegistration(regId, canonicalRegId);
            }
          } else {
            String error = result.getErrorCodeName();
            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
              // application has been removed from device - unregister it
              logger.info("Unregistered device: " + regId);
              UserDao.unregister(regId);
            } else {
              logger.severe("Error sending message to " + regId + ": " + error);
            }
          }
        }
      }
    });
  }
}
