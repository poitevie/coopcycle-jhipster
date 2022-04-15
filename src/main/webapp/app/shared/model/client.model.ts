import { ICart } from 'app/shared/model/cart.model';
import { ICommand } from 'app/shared/model/command.model';

export interface IClient {
  id?: number;
  idC?: string;
  firstnameC?: string;
  lastnameC?: string;
  emailC?: string;
  phoneC?: string | null;
  addressC?: string;
  carts?: ICart[] | null;
  commands?: ICommand[] | null;
}

export const defaultValue: Readonly<IClient> = {};
