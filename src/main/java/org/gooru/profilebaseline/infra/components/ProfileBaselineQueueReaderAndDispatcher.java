package org.gooru.profilebaseline.infra.components;


import static org.gooru.profilebaseline.infra.constants.Constants.EventBus.MBEP_LP_BASELINE_QUEUE_PROCESSOR;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.services.queueoperators.ProfileBaselineQueueInitializerService;
import org.gooru.profilebaseline.infra.services.queueoperators.ProfileBaselineQueueRecordDispatcherService;
import org.gooru.profilebaseline.routes.utils.DeliveryOptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the timer based runner class which is responsible to read the Persisted queued requests
 * and send them to Event bus so that they can be processed by listeners. It does wait for reply, so
 * that we do increase the backpressure on TCP bus too much, however what is replied is does not
 * matter as we do schedule another one shot timer to do the similar stuff. For the first run, it
 * re-initializes the status in the DB so that any tasks that were under processing when the
 * application shut down happened would be picked up again.
 *
 * @author ashish.
 */
public final class ProfileBaselineQueueReaderAndDispatcher implements Initializer, Finalizer {

  private static final ProfileBaselineQueueReaderAndDispatcher ourInstance = new ProfileBaselineQueueReaderAndDispatcher();
  private static final int delay = 1_000;
  private static long timerId;
  private static boolean firstTrigger = true;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ProfileBaselineQueueReaderAndDispatcher.class);
  private static final int LP_BASELINE_PROCESS_TIMEOUT = 300;

  public static ProfileBaselineQueueReaderAndDispatcher getInstance() {
    return ourInstance;
  }

  private Vertx vertx;

  private ProfileBaselineQueueReaderAndDispatcher() {
  }

  @Override
  public void finalizeComponent() {
    vertx.cancelTimer(timerId);
  }

  @Override
  public void initializeComponent(Vertx vertx, JsonObject config) {
    this.vertx = vertx;

    timerId = vertx.setTimer(delay, new TimerHandler(vertx));
  }

  static final class TimerHandler implements Handler<Long> {

    private final Vertx vertx;

    TimerHandler(Vertx vertx) {
      this.vertx = vertx;
    }

    @Override
    public void handle(Long event) {
      vertx.<ProfileBaselineQueueModel>executeBlocking(future -> {
        if (firstTrigger) {
          LOGGER.debug("Timer handling for first trigger");
          ProfileBaselineQueueInitializerService.build().initializeQueue();
          firstTrigger = false;
        }
        ProfileBaselineQueueModel model = ProfileBaselineQueueRecordDispatcherService.build()
            .getNextRecordToDispatch();
        future.complete(model);
      }, asyncResult -> {
        if (asyncResult.succeeded()) {
          if (asyncResult.result().isModelPersisted()) {
            vertx.eventBus().send(MBEP_LP_BASELINE_QUEUE_PROCESSOR, asyncResult.result().toJson(),
                DeliveryOptionsBuilder.buildWithoutApiVersion(LP_BASELINE_PROCESS_TIMEOUT),
                eventBusResponse -> {
                  timerId = vertx.setTimer(delay, new TimerHandler(vertx));
                });
          } else {
            timerId = vertx.setTimer(delay, new TimerHandler(vertx));
          }
        } else {
          LOGGER.warn("Processing of record from queue failed. ", asyncResult.cause());
          timerId = vertx.setTimer(delay, new TimerHandler(vertx));
        }
      });

    }
  }

}
