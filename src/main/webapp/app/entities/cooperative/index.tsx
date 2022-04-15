import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cooperative from './cooperative';
import CooperativeDetail from './cooperative-detail';
import CooperativeUpdate from './cooperative-update';
import CooperativeDeleteDialog from './cooperative-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CooperativeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CooperativeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CooperativeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cooperative} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CooperativeDeleteDialog} />
  </>
);

export default Routes;
