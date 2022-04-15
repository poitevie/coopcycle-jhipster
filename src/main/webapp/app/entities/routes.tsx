import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Client from './client';
import Cooperative from './cooperative';
import Driver from './driver';
import Shop from './shop';
import Cart from './cart';
import Command from './command';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}client`} component={Client} />
        <ErrorBoundaryRoute path={`${match.url}cooperative`} component={Cooperative} />
        <ErrorBoundaryRoute path={`${match.url}driver`} component={Driver} />
        <ErrorBoundaryRoute path={`${match.url}shop`} component={Shop} />
        <ErrorBoundaryRoute path={`${match.url}cart`} component={Cart} />
        <ErrorBoundaryRoute path={`${match.url}command`} component={Command} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
