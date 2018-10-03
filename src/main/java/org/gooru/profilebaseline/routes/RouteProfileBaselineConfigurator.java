package org.gooru.profilebaseline.routes;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.gooru.profilebaseline.infra.constants.Constants;
import org.gooru.profilebaseline.infra.constants.HttpConstants;
import org.gooru.profilebaseline.routes.utils.DeliveryOptionsBuilder;
import org.gooru.profilebaseline.routes.utils.RouteRequestUtility;
import org.gooru.profilebaseline.routes.utils.RouteResponseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish
 */
public class RouteProfileBaselineConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RouteProfileBaselineConfigurator.class);
  private EventBus eb = null;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    eb = vertx.eventBus();
    mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1_000;
    router.post(Constants.Route.API_LPBASELINE_CALCULATE).handler(this::doBaselineLP);
  }

  private void doBaselineLP(RoutingContext routingContext) {
    DeliveryOptions options = DeliveryOptionsBuilder
        .buildWithoutApiVersion(routingContext, mbusTimeout, Constants.Message.MSG_OP_LP_BASELINE);
    eb.send(Constants.EventBus.MBEP_LP_BASELINE,
        RouteRequestUtility.getBodyForMessage(routingContext),
        options);
    RouteResponseUtility
        .responseHandlerStatusOnlyNoBodyOrHeaders(routingContext, HttpConstants.HttpStatus.SUCCESS);
  }
}
