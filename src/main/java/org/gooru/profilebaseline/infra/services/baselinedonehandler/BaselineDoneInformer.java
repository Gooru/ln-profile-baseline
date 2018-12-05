package org.gooru.profilebaseline.infra.services.baselinedonehandler;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

public interface BaselineDoneInformer {

  void inform(ProfileBaselineProcessingContext context);

  static BaselineDoneInformer build(DBI dbi4core) {
    return new BaselineDoneInformerImpl(dbi4core);
  }

  static BaselineDoneInformer build() {
    return new BaselineDoneInformerImpl(DBICreator.getDbiForDefaultDS());
  }

}
