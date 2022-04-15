import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICooperative } from 'app/shared/model/cooperative.model';
import { getEntities as getCooperatives } from 'app/entities/cooperative/cooperative.reducer';
import { IDriver } from 'app/shared/model/driver.model';
import { getEntity, updateEntity, createEntity, reset } from './driver.reducer';

export const DriverUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cooperatives = useAppSelector(state => state.cooperative.entities);
  const driverEntity = useAppSelector(state => state.driver.entity);
  const loading = useAppSelector(state => state.driver.loading);
  const updating = useAppSelector(state => state.driver.updating);
  const updateSuccess = useAppSelector(state => state.driver.updateSuccess);
  const handleClose = () => {
    props.history.push('/driver');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCooperatives({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...driverEntity,
      ...values,
      cooperative: cooperatives.find(it => it.id.toString() === values.cooperative.toString()),
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
          ...driverEntity,
          cooperative: driverEntity?.cooperative?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.driver.home.createOrEditLabel" data-cy="DriverCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.driver.home.createOrEditLabel">Create or edit a Driver</Translate>
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
                  id="driver-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.driver.firstnameD')}
                id="driver-firstnameD"
                name="firstnameD"
                data-cy="firstnameD"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                  pattern: { value: /^[A-Z][a-z]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Z][a-z]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.driver.lastnameD')}
                id="driver-lastnameD"
                name="lastnameD"
                data-cy="lastnameD"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                  pattern: { value: /^[A-Z][a-z]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Z][a-z]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.driver.phoneD')}
                id="driver-phoneD"
                name="phoneD"
                data-cy="phoneD"
                type="text"
              />
              <ValidatedField
                id="driver-cooperative"
                name="cooperative"
                data-cy="cooperative"
                label={translate('coopcycleApp.driver.cooperative')}
                type="select"
              >
                <option value="" key="0" />
                {cooperatives
                  ? cooperatives.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/driver" replace color="info">
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

export default DriverUpdate;
