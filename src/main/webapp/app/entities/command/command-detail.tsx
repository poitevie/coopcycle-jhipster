import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './command.reducer';

export const CommandDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const commandEntity = useAppSelector(state => state.command.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commandDetailsHeading">
          <Translate contentKey="coopcycleApp.command.detail.title">Command</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commandEntity.id}</dd>
          <dt>
            <span id="addressC">
              <Translate contentKey="coopcycleApp.command.addressC">Address C</Translate>
            </span>
          </dt>
          <dd>{commandEntity.addressC}</dd>
          <dt>
            <span id="dateC">
              <Translate contentKey="coopcycleApp.command.dateC">Date C</Translate>
            </span>
          </dt>
          <dd>{commandEntity.dateC}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.command.client">Client</Translate>
          </dt>
          <dd>{commandEntity.client ? commandEntity.client.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.command.driver">Driver</Translate>
          </dt>
          <dd>{commandEntity.driver ? commandEntity.driver.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/command" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/command/${commandEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommandDetail;
