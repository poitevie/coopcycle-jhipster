import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { ICart } from 'app/shared/model/cart.model';
import { getEntities as getCarts } from 'app/entities/cart/cart.reducer';
import { IDriver } from 'app/shared/model/driver.model';
import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { ICommand } from 'app/shared/model/command.model';
import { getEntity, updateEntity, createEntity, reset } from './command.reducer';

export const CommandUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const clients = useAppSelector(state => state.client.entities);
  const carts = useAppSelector(state => state.cart.entities);
  const drivers = useAppSelector(state => state.driver.entities);
  const commandEntity = useAppSelector(state => state.command.entity);
  const loading = useAppSelector(state => state.command.loading);
  const updating = useAppSelector(state => state.command.updating);
  const updateSuccess = useAppSelector(state => state.command.updateSuccess);
  const handleClose = () => {
    props.history.push('/command');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getClients({}));
    dispatch(getCarts({}));
    dispatch(getDrivers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...commandEntity,
      ...values,
      client: clients.find(it => it.id.toString() === values.client.toString()),
      driver: drivers.find(it => it.id.toString() === values.driver.toString()),
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
          ...commandEntity,
          client: commandEntity?.client?.id,
          driver: commandEntity?.driver?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.command.home.createOrEditLabel" data-cy="CommandCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.command.home.createOrEditLabel">Create or edit a Command</Translate>
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
                  id="command-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.command.addressC')}
                id="command-addressC"
                name="addressC"
                data-cy="addressC"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.command.dateC')}
                id="command-dateC"
                name="dateC"
                data-cy="dateC"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="command-client"
                name="client"
                data-cy="client"
                label={translate('coopcycleApp.command.client')}
                type="select"
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="command-driver"
                name="driver"
                data-cy="driver"
                label={translate('coopcycleApp.command.driver')}
                type="select"
              >
                <option value="" key="0" />
                {drivers
                  ? drivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/command" replace color="info">
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

export default CommandUpdate;
