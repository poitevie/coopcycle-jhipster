import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './driver.reducer';

export const DriverDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const driverEntity = useAppSelector(state => state.driver.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="driverDetailsHeading">
          <Translate contentKey="coopcycleApp.driver.detail.title">Driver</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{driverEntity.id}</dd>
          <dt>
            <span id="firstnameD">
              <Translate contentKey="coopcycleApp.driver.firstnameD">Firstname D</Translate>
            </span>
          </dt>
          <dd>{driverEntity.firstnameD}</dd>
          <dt>
            <span id="lastnameD">
              <Translate contentKey="coopcycleApp.driver.lastnameD">Lastname D</Translate>
            </span>
          </dt>
          <dd>{driverEntity.lastnameD}</dd>
          <dt>
            <span id="phoneD">
              <Translate contentKey="coopcycleApp.driver.phoneD">Phone D</Translate>
            </span>
          </dt>
          <dd>{driverEntity.phoneD}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.driver.cooperative">Cooperative</Translate>
          </dt>
          <dd>{driverEntity.cooperative ? driverEntity.cooperative.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/driver" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/driver/${driverEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DriverDetail;
