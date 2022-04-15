import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICommand } from 'app/shared/model/command.model';
import { getEntities as getCommands } from 'app/entities/command/command.reducer';
import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { ICart } from 'app/shared/model/cart.model';
import { getEntity, updateEntity, createEntity, reset } from './cart.reducer';

export const CartUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const commands = useAppSelector(state => state.command.entities);
  const clients = useAppSelector(state => state.client.entities);
  const shops = useAppSelector(state => state.shop.entities);
  const cartEntity = useAppSelector(state => state.cart.entity);
  const loading = useAppSelector(state => state.cart.loading);
  const updating = useAppSelector(state => state.cart.updating);
  const updateSuccess = useAppSelector(state => state.cart.updateSuccess);
  const handleClose = () => {
    props.history.push('/cart');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCommands({}));
    dispatch(getClients({}));
    dispatch(getShops({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cartEntity,
      ...values,
      command: commands.find(it => it.id.toString() === values.command.toString()),
      client: clients.find(it => it.id.toString() === values.client.toString()),
      shop: shops.find(it => it.id.toString() === values.shop.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...cartEntity,
          command: cartEntity?.command?.id,
          client: cartEntity?.client?.id,
          shop: cartEntity?.shop?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.cart.home.createOrEditLabel" data-cy="CartCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.cart.home.createOrEditLabel">Create or edit a Cart</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="cart-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.cart.amount')}
                id="cart-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.cart.deadline')}
                id="cart-deadline"
                name="deadline"
                data-cy="deadline"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="cart-command"
                name="command"
                data-cy="command"
                label={translate('coopcycleApp.cart.command')}
                type="select"
              >
                <option value="" key="0" />
                {commands
                  ? commands.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="cart-client" name="client" data-cy="client" label={translate('coopcycleApp.cart.client')} type="select">
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="cart-shop" name="shop" data-cy="shop" label={translate('coopcycleApp.cart.shop')} type="select">
                <option value="" key="0" />
                {shops
                  ? shops.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cart" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CartUpdate;
