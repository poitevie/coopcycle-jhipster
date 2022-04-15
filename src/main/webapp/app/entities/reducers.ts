import client from 'app/entities/client/client.reducer';
import cooperative from 'app/entities/cooperative/cooperative.reducer';
import driver from 'app/entities/driver/driver.reducer';
import shop from 'app/entities/shop/shop.reducer';
import cart from 'app/entities/cart/cart.reducer';
import command from 'app/entities/command/command.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  client,
  cooperative,
  driver,
  shop,
  cart,
  command,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
