import { ICommand } from 'app/shared/model/command.model';
import { ICooperative } from 'app/shared/model/cooperative.model';

export interface IDriver {
  id?: number;
  firstnameD?: string;
  lastnameD?: string;
  phoneD?: string | null;
  commands?: ICommand[] | null;
  cooperative?: ICooperative | null;
}

export const defaultValue: Readonly<IDriver> = {};
