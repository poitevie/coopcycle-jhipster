import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDriver } from 'app/shared/model/driver.model';
import { getEntities } from './driver.reducer';

export const Driver = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const driverList = useAppSelector(state => state.driver.entities);
  const loading = useAppSelector(state => state.driver.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="driver-heading" data-cy="DriverHeading">
        <Translate contentKey="coopcycleApp.driver.home.title">Drivers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.driver.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/driver/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.driver.home.createLabel">Create new Driver</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {driverList && driverList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.driver.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.driver.firstnameD">Firstname D</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.driver.lastnameD">Lastname D</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.driver.phoneD">Phone D</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.driver.cooperative">Cooperative</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {driverList.map((driver, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/driver/${driver.id}`} color="link" size="sm">
                      {driver.id}
                    </Button>
                  </td>
                  <td>{driver.firstnameD}</td>
                  <td>{driver.lastnameD}</td>
                  <td>{driver.phoneD}</td>
                  <td>{driver.cooperative ? <Link to={`/cooperative/${driver.cooperative.id}`}>{driver.cooperative.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/driver/${driver.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/driver/${driver.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/driver/${driver.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="coopcycleApp.driver.home.notFound">No Drivers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Driver;
