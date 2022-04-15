import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cooperative.reducer';

export const CooperativeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cooperativeEntity = useAppSelector(state => state.cooperative.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cooperativeDetailsHeading">
          <Translate contentKey="coopcycleApp.cooperative.detail.title">Cooperative</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="coopcycleApp.cooperative.id">Id</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopcycleApp.cooperative.name">Name</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/cooperative" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cooperative/${cooperativeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CooperativeDetail;
