package org.gooru.profilebaseline.bootstrap.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.profilebaseline.infra.constants.Constants;
import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.services.ProfileBaselineQueueRecordProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */
public class ProfileBaselineProcessingVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ProfileBaselineProcessingVerticle.class);

  private static final String SUCCESS = "SUCCESS";
  private static final String FAIL = "FAIL";

  @Override
  public void start(Future<Void> startFuture) {

    EventBus eb = vertx.eventBus();
    eb.localConsumer(Constants.EventBus.MBEP_LP_BASELINE_QUEUE_PROCESSOR, this::processMessage)
        .completionHandler(result -> {
          if (result.succeeded()) {
            LOGGER.info("LP Baseline processor point ready to listen");
            startFuture.complete();
          } else {
            LOGGER.error(
                "Error registering the LP Baseline processing handler. Halting the machinery");
            startFuture.fail(result.cause());
            Runtime.getRuntime().halt(1);
          }
        });
  }

  @Override
  public void stop(Future<Void> stopFuture) {
  }

  private void processMessage(Message<String> message) {
    vertx.executeBlocking(future -> {
      try {
        ProfileBaselineQueueModel model = ProfileBaselineQueueModel.fromJson(message.body());
        ProfileBaselineQueueRecordProcessingService.build().doProfileBaseline(model);
        sendMessageToPostProcessor(model);
        future.complete();
      } catch (Exception e) {
        LOGGER.warn("Not able to do LP Baseline for the model. '{}'", message.body(), e);
        future.fail(e);
      }
    }, asyncResult -> {
      if (asyncResult.succeeded()) {
        message.reply(SUCCESS);
      } else {
        LOGGER.warn("LP Baseline not done for model: '{}'", message.body(), asyncResult.cause());
        message.reply(FAIL);
      }
    });
  }

  private void sendMessageToPostProcessor(ProfileBaselineQueueModel model) {
    JsonObject request = new JsonObject(model.toJson());

    vertx.eventBus()
        .send(Constants.EventBus.MBEP_LP_BASELINE_POST_PROCESSOR, request, new DeliveryOptions()
            .addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_POSTPROCESS_RESCOPE_R0));
  }

}
