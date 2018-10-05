package org.gooru.profilebaseline.infra.services.queueoperators;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish
 */
class ProfileBaselineQueueRecordDispatcherServiceImpl implements
    ProfileBaselineQueueRecordDispatcherService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ProfileBaselineQueueRecordDispatcherService.class);
  private final DBI dbi;

  ProfileBaselineQueueRecordDispatcherServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public ProfileBaselineQueueModel getNextRecordToDispatch() {
    ProfileBaselineQueueOperatorDao dao = dbi.onDemand(ProfileBaselineQueueOperatorDao.class);
    ProfileBaselineQueueModel model = dao.getNextDispatchableModel();
    if (model == null) {
      model = ProfileBaselineQueueModel.createNonPersistedEmptyModel();
    } else {
      dao.setQueuedRecordStatusAsDispatched(model.getId());
    }
    return model;
  }
}
