import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import status, {
  StatusState
} from 'app/entities/status/status.reducer';
// prettier-ignore
import category, {
  CategoryState
} from 'app/entities/category/category.reducer';
// prettier-ignore
import project, {
  ProjectState
} from 'app/entities/project/project.reducer';
// prettier-ignore
import ong, {
  OngState
} from 'app/entities/ong/ong.reducer';
// prettier-ignore
import volunteerRequest, {
  VolunteerRequestState
} from 'app/entities/volunteer-request/volunteer-request.reducer';
// prettier-ignore
import ongRequest, {
  OngRequestState
} from 'app/entities/ong-request/ong-request.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly status: StatusState;
  readonly category: CategoryState;
  readonly project: ProjectState;
  readonly ong: OngState;
  readonly volunteerRequest: VolunteerRequestState;
  readonly ongRequest: OngRequestState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  status,
  category,
  project,
  ong,
  volunteerRequest,
  ongRequest,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
