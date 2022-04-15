import { IClient } from 'app/shared/model/client.model';
import { ICart } from 'app/shared/model/cart.model';
import { IDriver } from 'app/shared/model/driver.model';

export interface ICommand {
  id?: number;
  addressC?: string;
  dateC?: number;
  client?: IClient | null;
  cart?: ICart | null;
  driver?: IDriver | null;
}

export const defaultValue: Readonly<ICommand> = {};
