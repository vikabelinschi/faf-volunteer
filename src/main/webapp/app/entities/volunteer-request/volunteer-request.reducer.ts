import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVolunteerRequest, defaultValue } from 'app/shared/model/volunteer-request.model';

export const ACTION_TYPES = {
  FETCH_VOLUNTEERREQUEST_LIST: 'volunteerRequest/FETCH_VOLUNTEERREQUEST_LIST',
  FETCH_VOLUNTEERREQUEST: 'volunteerRequest/FETCH_VOLUNTEERREQUEST',
  CREATE_VOLUNTEERREQUEST: 'volunteerRequest/CREATE_VOLUNTEERREQUEST',
  UPDATE_VOLUNTEERREQUEST: 'volunteerRequest/UPDATE_VOLUNTEERREQUEST',
  DELETE_VOLUNTEERREQUEST: 'volunteerRequest/DELETE_VOLUNTEERREQUEST',
  RESET: 'volunteerRequest/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVolunteerRequest>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type VolunteerRequestState = Readonly<typeof initialState>;

// Reducer

export default (state: VolunteerRequestState = initialState, action): VolunteerRequestState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_VOLUNTEERREQUEST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VOLUNTEERREQUEST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VOLUNTEERREQUEST):
    case REQUEST(ACTION_TYPES.UPDATE_VOLUNTEERREQUEST):
    case REQUEST(ACTION_TYPES.DELETE_VOLUNTEERREQUEST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_VOLUNTEERREQUEST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VOLUNTEERREQUEST):
    case FAILURE(ACTION_TYPES.CREATE_VOLUNTEERREQUEST):
    case FAILURE(ACTION_TYPES.UPDATE_VOLUNTEERREQUEST):
    case FAILURE(ACTION_TYPES.DELETE_VOLUNTEERREQUEST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_VOLUNTEERREQUEST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_VOLUNTEERREQUEST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VOLUNTEERREQUEST):
    case SUCCESS(ACTION_TYPES.UPDATE_VOLUNTEERREQUEST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VOLUNTEERREQUEST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/volunteer-requests';

// Actions

export const getEntities: ICrudGetAllAction<IVolunteerRequest> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_VOLUNTEERREQUEST_LIST,
    payload: axios.get<IVolunteerRequest>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IVolunteerRequest> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VOLUNTEERREQUEST,
    payload: axios.get<IVolunteerRequest>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IVolunteerRequest> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VOLUNTEERREQUEST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IVolunteerRequest> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VOLUNTEERREQUEST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVolunteerRequest> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VOLUNTEERREQUEST,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
