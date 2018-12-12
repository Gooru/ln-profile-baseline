package org.gooru.profilebaseline.infra.services.baselinedonehandler;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class BaselineDoneInformerImpl implements BaselineDoneInformer {

  private ProfileBaselineProcessingContext context;
  private final DBI dbi4core;
  private BaselineDoneInformerDao dao;

  BaselineDoneInformerImpl(DBI dbi4core) {
    this.dbi4core = dbi4core;
  }

  @Override
  public void inform(ProfileBaselineProcessingContext context) {
    this.context = context;
    if (context.getClassId() != null) {
      updateStatusForClassMembership();
    }
  }

  private void updateStatusForClassMembership() {
    fetchDao().updateDoneStatusForClassMember(context.getClassId().toString(),
        context.getUserId().toString());
  }

  private BaselineDoneInformerDao fetchDao() {
    if (dao == null) {
      dao = dbi4core.onDemand(BaselineDoneInformerDao.class);
    }
    return dao;
  }
}
