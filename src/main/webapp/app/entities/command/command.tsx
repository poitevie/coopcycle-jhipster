import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICommand } from 'app/shared/model/command.model';
import { getEntities } from './command.reducer';

export const Command = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const commandList = useAppSelector(state => state.command.entities);
  const loading = useAppSelector(state => state.command.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="command-heading" data-cy="CommandHeading">
        <Translate contentKey="coopcycleApp.command.home.title">Commands</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.command.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/command/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.command.home.createLabel">Create new Command</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {commandList && commandList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.command.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.command.addressC">Address C</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.command.dateC">Date C</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.command.client">Client</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.command.driver">Driver</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {commandList.map((command, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/command/${command.id}`} color="link" size="sm">
                      {command.id}
                    </Button>
                  </td>
                  <td>{command.addressC}</td>
                  <td>{command.dateC}</td>
                  <td>{command.client ? <Link to={`/client/${command.client.id}`}>{command.client.id}</Link> : ''}</td>
                  <td>{command.driver ? <Link to={`/driver/${command.driver.id}`}>{command.driver.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/command/${command.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/command/${command.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/command/${command.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="coopcycleApp.command.home.notFound">No Commands found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Command;
