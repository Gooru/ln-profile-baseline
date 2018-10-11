package org.gooru.profilebaseline.infra.services.learnerprofile;

import java.util.List;
import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.services.algebra.competency.Competency;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyAlgebraDefaultBuilder;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyLine;
import org.gooru.profilebaseline.infra.services.algebra.competency.CompetencyMap;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class LearnerProfileProviderImpl implements LearnerProfileProvider {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private LearnerProfileProviderDao dsDao;

  LearnerProfileProviderImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public CompetencyLine findLearnerProfileForUser(ProfileBaselineProcessingContext context) {
    List<Competency> competenciesForLP = fetchDsDao()
        .fetchProficiencyForUserInSpecifiedSubjectAndDomains(context.getUserId().toString(),
            context.getSubject());
    if (competenciesForLP != null && !competenciesForLP.isEmpty()) {
      return CompetencyMap.build(competenciesForLP).getCeilingLine();
    }
    return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
  }

  private LearnerProfileProviderDao fetchDsDao() {
    if (dsDao == null) {
      dsDao = dbi4core.onDemand(LearnerProfileProviderDao.class);
    }
    return dsDao;
  }

}
