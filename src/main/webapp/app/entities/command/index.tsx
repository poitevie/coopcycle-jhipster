import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Command from './command';
import CommandDetail from './command-detail';
import CommandUpdate from './command-update';
import CommandDeleteDialog from './command-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CommandUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CommandUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CommandDetail} />
      <ErrorBoundaryRoute path={match.url} component={Command} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CommandDeleteDialog} />
  </>
);

export default Routes;
