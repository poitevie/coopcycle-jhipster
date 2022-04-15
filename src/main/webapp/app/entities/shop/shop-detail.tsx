import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './shop.reducer';

export const ShopDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const shopEntity = useAppSelector(state => state.shop.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shopDetailsHeading">
          <Translate contentKey="coopcycleApp.shop.detail.title">Shop</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shopEntity.id}</dd>
          <dt>
            <span id="addressS">
              <Translate contentKey="coopcycleApp.shop.addressS">Address S</Translate>
            </span>
          </dt>
          <dd>{shopEntity.addressS}</dd>
          <dt>
            <span id="menu">
              <Translate contentKey="coopcycleApp.shop.menu">Menu</Translate>
            </span>
          </dt>
          <dd>{shopEntity.menu}</dd>
        </dl>
        <Button tag={Link} to="/shop" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shop/${shopEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShopDetail;
