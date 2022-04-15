import { ICart } from 'app/shared/model/cart.model';

export interface IShop {
  id?: number;
  addressS?: string;
  menu?: string | null;
  carts?: ICart[] | null;
}

export const defaultValue: Readonly<IShop> = {};
