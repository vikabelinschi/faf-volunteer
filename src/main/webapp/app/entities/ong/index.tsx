import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ong from './ong';
import OngDetail from './ong-detail';
import OngUpdate from './ong-update';
import OngDeleteDialog from './ong-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OngUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OngUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OngDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ong} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OngDeleteDialog} />
  </>
);

export default Routes;
