import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VolunteerRequest from './volunteer-request';
import VolunteerRequestDetail from './volunteer-request-detail';
import VolunteerRequestUpdate from './volunteer-request-update';
import VolunteerRequestDeleteDialog from './volunteer-request-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VolunteerRequestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VolunteerRequestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VolunteerRequestDetail} />
      <ErrorBoundaryRoute path={match.url} component={VolunteerRequest} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={VolunteerRequestDeleteDialog} />
  </>
);

export default Routes;
