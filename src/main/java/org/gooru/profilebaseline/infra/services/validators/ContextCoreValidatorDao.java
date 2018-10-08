package org.gooru.profilebaseline.infra.services.validators;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */
interface ContextCoreValidatorDao {

  @SqlQuery("select exists (select 1 from class c inner join class_member cm on c.id = cm.class_id "
      + " and c.id = :classId and c.is_deleted = false and cm.user_id = :userId inner join "
      + " course crs on crs.id = c.course_id and crs.id = :courseId and crs.is_deleted = false)")
  boolean validateClassCourseUserCombo(@Bind("classId") UUID classId,
      @Bind("courseId") UUID courseId, @Bind("userId") UUID userId);

  @SqlQuery("select exists(select 1 from course where id = :courseId and is_deleted = false)")
  boolean validateCourseExists(@Bind("courseId") UUID courseId);

}