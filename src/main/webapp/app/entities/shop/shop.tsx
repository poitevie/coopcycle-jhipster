import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IShop } from 'app/shared/model/shop.model';
import { getEntities } from './shop.reducer';

export const Shop = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const shopList = useAppSelector(state => state.shop.entities);
  const loading = useAppSelector(state => state.shop.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="shop-heading" data-cy="ShopHeading">
        <Translate contentKey="coopcycleApp.shop.home.title">Shops</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.shop.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/shop/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.shop.home.createLabel">Create new Shop</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {shopList && shopList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.shop.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.shop.addressS">Address S</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.shop.menu">Menu</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shopList.map((shop, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/shop/${shop.id}`} color="link" size="sm">
                      {shop.id}
                    </Button>
                  </td>
                  <td>{shop.addressS}</td>
                  <td>{shop.menu}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/shop/${shop.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/shop/${shop.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/shop/${shop.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="coopcycleApp.shop.home.notFound">No Shops found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Shop;
