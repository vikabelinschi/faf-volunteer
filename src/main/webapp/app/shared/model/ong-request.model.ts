import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';

export interface IOngRequest {
  id?: number;
  name?: string;
  idno?: string;
  registrationDate?: Moment;
  user?: IUser;
}

export const defaultValue: Readonly<IOngRequest> = {};
