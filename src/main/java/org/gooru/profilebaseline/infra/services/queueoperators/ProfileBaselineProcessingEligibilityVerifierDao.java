package org.gooru.profilebaseline.infra.services.queueoperators;

import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface ProfileBaselineProcessingEligibilityVerifierDao {

  @SqlQuery(
      "select exists (select 1 from baseline_learner_profile_master where user_id = :userId and "
          + " course_id = :courseId  and class_id is null)")
  boolean profileBaselineDoneForUserInIL(@BindBean ProfileBaselineQueueModel model);

  @SqlQuery(
      "select exists (select 1 from baseline_learner_profile_master where user_id = :userId and "
          + "course_id = :courseId  and class_id = :classId)")
  boolean profileBaselineDoneForUserInClass(@BindBean ProfileBaselineQueueModel model);

  @SqlQuery("select exists (select 1 from profile_baseline_queue where id = :id and status = 1)")
  boolean isQueuedRecordStillDispatched(@Bind("id") Long id);
}
