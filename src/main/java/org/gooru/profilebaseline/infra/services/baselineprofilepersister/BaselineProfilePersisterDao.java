package org.gooru.profilebaseline.infra.services.baselineprofilepersister;

import org.gooru.profilebaseline.infra.services.baselineprofilepersister.LearnerProfileBaselinedModel.LearnerProfileBaselinedModelBean;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ashish.
 */

interface BaselineProfilePersisterDao {

  @GetGeneratedKeys
  @SqlUpdate("insert into learner_profile_baselined (tx_subject_code, class_id, course_id, user_id, gut_codes, lp_data) values (:subjectCode, :classId, :courseId, :userId, :gutCodes, :lpData)")
  Long persistLearnerProfileBaselined(@BindBean LearnerProfileBaselinedModelBean model);

}
