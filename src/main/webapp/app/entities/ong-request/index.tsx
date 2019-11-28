import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OngRequest from './ong-request';
import OngRequestDetail from './ong-request-detail';
import OngRequestUpdate from './ong-request-update';
import OngRequestDeleteDialog from './ong-request-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OngRequestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OngRequestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OngRequestDetail} />
      <ErrorBoundaryRoute path={match.url} component={OngRequest} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OngRequestDeleteDialog} />
  </>
);

export default Routes;
