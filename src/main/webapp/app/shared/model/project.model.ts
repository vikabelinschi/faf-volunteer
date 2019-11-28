import { Moment } from 'moment';
import { IStatus } from 'app/shared/model/status.model';
import { ICategory } from 'app/shared/model/category.model';
import { IUser } from 'app/shared/model/user.model';
import { IOng } from 'app/shared/model/ong.model';

export interface IProject {
  id?: number;
  name?: string;
  description?: string;
  registrationDate?: Moment;
  status?: IStatus;
  category?: ICategory;
  users?: IUser[];
  ong?: IOng;
}

export const defaultValue: Readonly<IProject> = {};
