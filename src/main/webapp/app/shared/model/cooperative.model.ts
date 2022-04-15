import { IDriver } from 'app/shared/model/driver.model';

export interface ICooperative {
  id?: string;
  name?: string;
  drivers?: IDriver[] | null;
}

export const defaultValue: Readonly<ICooperative> = {};
