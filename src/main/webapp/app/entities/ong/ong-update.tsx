import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './ong.reducer';
import { IOng } from 'app/shared/model/ong.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOngUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOngUpdateState {
  isNew: boolean;
}

export class OngUpdate extends React.Component<IOngUpdateProps, IOngUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { ongEntity } = this.props;
      const entity = {
        ...ongEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/ong');
  };

  render() {
    const { ongEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="volunteerApp.ong.home.createOrEditLabel">
              <Translate contentKey="volunteerApp.ong.home.createOrEditLabel">Create or edit a Ong</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : ongEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="ong-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="ong-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="ong-name">
                    <Translate contentKey="volunteerApp.ong.name">Name</Translate>
                  </Label>
                  <AvField
                    id="ong-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="ong-description">
                    <Translate contentKey="volunteerApp.ong.description">Description</Translate>
                  </Label>
                  <AvField
                    id="ong-description"
                    type="text"
                    name="description"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 5000, errorMessage: translate('entity.validation.maxlength', { max: 5000 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="idnoLabel" for="ong-idno">
                    <Translate contentKey="volunteerApp.ong.idno">Idno</Translate>
                  </Label>
                  <AvField
                    id="ong-idno"
                    type="text"
                    name="idno"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 13, errorMessage: translate('entity.validation.maxlength', { max: 13 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="ong-email">
                    <Translate contentKey="volunteerApp.ong.email">Email</Translate>
                  </Label>
                  <AvField
                    id="ong-email"
                    type="text"
                    name="email"
                    validate={{
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="ong-address">
                    <Translate contentKey="volunteerApp.ong.address">Address</Translate>
                  </Label>
                  <AvField
                    id="ong-address"
                    type="text"
                    name="address"
                    validate={{
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="phoneLabel" for="ong-phone">
                    <Translate contentKey="volunteerApp.ong.phone">Phone</Translate>
                  </Label>
                  <AvField
                    id="ong-phone"
                    type="text"
                    name="phone"
                    validate={{
                      maxLength: { value: 20, errorMessage: translate('entity.validation.maxlength', { max: 20 }) }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/ong" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  ongEntity: storeState.ong.entity,
  loading: storeState.ong.loading,
  updating: storeState.ong.updating,
  updateSuccess: storeState.ong.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OngUpdate);
