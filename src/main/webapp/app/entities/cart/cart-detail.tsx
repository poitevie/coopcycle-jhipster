import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cart.reducer';

export const CartDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cartEntity = useAppSelector(state => state.cart.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cartDetailsHeading">
          <Translate contentKey="coopcycleApp.cart.detail.title">Cart</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cartEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="coopcycleApp.cart.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{cartEntity.amount}</dd>
          <dt>
            <span id="deadline">
              <Translate contentKey="coopcycleApp.cart.deadline">Deadline</Translate>
            </span>
          </dt>
          <dd>{cartEntity.deadline}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.cart.command">Command</Translate>
          </dt>
          <dd>{cartEntity.command ? cartEntity.command.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.cart.client">Client</Translate>
          </dt>
          <dd>{cartEntity.client ? cartEntity.client.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.cart.shop">Shop</Translate>
          </dt>
          <dd>{cartEntity.shop ? cartEntity.shop.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/cart" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cart/${cartEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CartDetail;
