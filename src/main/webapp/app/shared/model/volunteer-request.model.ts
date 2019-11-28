import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { IProject } from 'app/shared/model/project.model';

export interface IVolunteerRequest {
  id?: number;
  registrationDate?: Moment;
  user?: IUser;
  project?: IProject;
}

export const defaultValue: Readonly<IVolunteerRequest> = {};
