import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOngRequest, defaultValue } from 'app/shared/model/ong-request.model';

export const ACTION_TYPES = {
  FETCH_ONGREQUEST_LIST: 'ongRequest/FETCH_ONGREQUEST_LIST',
  FETCH_ONGREQUEST: 'ongRequest/FETCH_ONGREQUEST',
  CREATE_ONGREQUEST: 'ongRequest/CREATE_ONGREQUEST',
  UPDATE_ONGREQUEST: 'ongRequest/UPDATE_ONGREQUEST',
  DELETE_ONGREQUEST: 'ongRequest/DELETE_ONGREQUEST',
  RESET: 'ongRequest/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOngRequest>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OngRequestState = Readonly<typeof initialState>;

// Reducer

export default (state: OngRequestState = initialState, action): OngRequestState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ONGREQUEST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ONGREQUEST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ONGREQUEST):
    case REQUEST(ACTION_TYPES.UPDATE_ONGREQUEST):
    case REQUEST(ACTION_TYPES.DELETE_ONGREQUEST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ONGREQUEST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ONGREQUEST):
    case FAILURE(ACTION_TYPES.CREATE_ONGREQUEST):
    case FAILURE(ACTION_TYPES.UPDATE_ONGREQUEST):
    case FAILURE(ACTION_TYPES.DELETE_ONGREQUEST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ONGREQUEST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ONGREQUEST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ONGREQUEST):
    case SUCCESS(ACTION_TYPES.UPDATE_ONGREQUEST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ONGREQUEST):
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

const apiUrl = 'api/ong-requests';

// Actions

export const getEntities: ICrudGetAllAction<IOngRequest> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ONGREQUEST_LIST,
    payload: axios.get<IOngRequest>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOngRequest> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ONGREQUEST,
    payload: axios.get<IOngRequest>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOngRequest> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ONGREQUEST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOngRequest> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ONGREQUEST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOngRequest> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ONGREQUEST,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
