import { ICommand } from 'app/shared/model/command.model';
import { IClient } from 'app/shared/model/client.model';
import { IShop } from 'app/shared/model/shop.model';

export interface ICart {
  id?: number;
  amount?: number;
  deadline?: number;
  command?: ICommand | null;
  client?: IClient | null;
  shop?: IShop | null;
}

export const defaultValue: Readonly<ICart> = {};
