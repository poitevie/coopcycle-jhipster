import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/client">
        <Translate contentKey="global.menu.entities.client" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cooperative">
        <Translate contentKey="global.menu.entities.cooperative" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/driver">
        <Translate contentKey="global.menu.entities.driver" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/shop">
        <Translate contentKey="global.menu.entities.shop" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cart">
        <Translate contentKey="global.menu.entities.cart" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/command">
        <Translate contentKey="global.menu.entities.command" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
