import { IProject } from 'app/shared/model/project.model';

export interface IOng {
  id?: number;
  name?: string;
  description?: string;
  idno?: string;
  email?: string;
  address?: string;
  phone?: string;
  projects?: IProject[];
}

export const defaultValue: Readonly<IOng> = {};
