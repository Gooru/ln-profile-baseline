package org.gooru.profilebaseline.infra.services.validators;

import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * The validator to validate the context provided before rescope is calculated.
 *
 * If the validation fails, idea is to abort the processing of the rescope and halt the machinery
 * for this request.
 *
 * @author ashish.
 */

public interface ContextValidator {

  void validate(ProfileBaselineProcessingContext context);

  static ContextValidator build() {
    return new ContextValidatorImpl(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

  static ContextValidator build(DBI dbi4core, DBI dbi4ds) {
    return new ContextValidatorImpl(dbi4core, dbi4ds);
  }
}
