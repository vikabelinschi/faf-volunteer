import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Status from './status';
import Category from './category';
import Project from './project';
import Ong from './ong';
import VolunteerRequest from './volunteer-request';
import OngRequest from './ong-request';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}status`} component={Status} />
      <ErrorBoundaryRoute path={`${match.url}category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}project`} component={Project} />
      <ErrorBoundaryRoute path={`${match.url}ong`} component={Ong} />
      <ErrorBoundaryRoute path={`${match.url}volunteer-request`} component={VolunteerRequest} />
      <ErrorBoundaryRoute path={`${match.url}ong-request`} component={OngRequest} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
