import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClient } from 'app/shared/model/client.model';
import { getEntity, updateEntity, createEntity, reset } from './client.reducer';

export const ClientUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const clientEntity = useAppSelector(state => state.client.entity);
  const loading = useAppSelector(state => state.client.loading);
  const updating = useAppSelector(state => state.client.updating);
  const updateSuccess = useAppSelector(state => state.client.updateSuccess);
  const handleClose = () => {
    props.history.push('/client');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...clientEntity,
      ...values,
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
          ...clientEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.client.home.createOrEditLabel" data-cy="ClientCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.client.home.createOrEditLabel">Create or edit a Client</Translate>
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
                  id="client-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.client.idC')}
                id="client-idC"
                name="idC"
                data-cy="idC"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.client.firstnameC')}
                id="client-firstnameC"
                name="firstnameC"
                data-cy="firstnameC"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                  pattern: { value: /^[A-Z][a-z]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Z][a-z]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.client.lastnameC')}
                id="client-lastnameC"
                name="lastnameC"
                data-cy="lastnameC"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                  pattern: { value: /^[A-Z][a-z]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Z][a-z]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.client.emailC')}
                id="client-emailC"
                name="emailC"
                data-cy="emailC"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                  pattern: {
                    value: /^([a-zA-Z0-9_\-\\.]+)@([a-zA-Z0-9_\-\\.]+)\\.([a-zA-Z]{2,5})$/,
                    message: translate('entity.validation.pattern', {
                      pattern: '^([a-zA-Z0-9_\\-\\\\.]+)@([a-zA-Z0-9_\\-\\\\.]+)\\\\.([a-zA-Z]{2,5})$',
                    }),
                  },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.client.phoneC')}
                id="client-phoneC"
                name="phoneC"
                data-cy="phoneC"
                type="text"
              />
              <ValidatedField
                label={translate('coopcycleApp.client.addressC')}
                id="client-addressC"
                name="addressC"
                data-cy="addressC"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/client" replace color="info">
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

export default ClientUpdate;
