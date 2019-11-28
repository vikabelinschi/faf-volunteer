import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOng, defaultValue } from 'app/shared/model/ong.model';

export const ACTION_TYPES = {
  FETCH_ONG_LIST: 'ong/FETCH_ONG_LIST',
  FETCH_ONG: 'ong/FETCH_ONG',
  CREATE_ONG: 'ong/CREATE_ONG',
  UPDATE_ONG: 'ong/UPDATE_ONG',
  DELETE_ONG: 'ong/DELETE_ONG',
  RESET: 'ong/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOng>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OngState = Readonly<typeof initialState>;

// Reducer

export default (state: OngState = initialState, action): OngState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ONG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ONG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ONG):
    case REQUEST(ACTION_TYPES.UPDATE_ONG):
    case REQUEST(ACTION_TYPES.DELETE_ONG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ONG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ONG):
    case FAILURE(ACTION_TYPES.CREATE_ONG):
    case FAILURE(ACTION_TYPES.UPDATE_ONG):
    case FAILURE(ACTION_TYPES.DELETE_ONG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ONG_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ONG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ONG):
    case SUCCESS(ACTION_TYPES.UPDATE_ONG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ONG):
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

const apiUrl = 'api/ongs';

// Actions

export const getEntities: ICrudGetAllAction<IOng> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ONG_LIST,
    payload: axios.get<IOng>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOng> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ONG,
    payload: axios.get<IOng>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOng> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ONG,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOng> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ONG,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOng> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ONG,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
