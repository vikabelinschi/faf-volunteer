import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './ong-request.reducer';
import { IOngRequest } from 'app/shared/model/ong-request.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOngRequestDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OngRequestDetail extends React.Component<IOngRequestDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ongRequestEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="volunteerApp.ongRequest.detail.title">OngRequest</Translate> [<b>{ongRequestEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="volunteerApp.ongRequest.name">Name</Translate>
              </span>
            </dt>
            <dd>{ongRequestEntity.name}</dd>
            <dt>
              <span id="idno">
                <Translate contentKey="volunteerApp.ongRequest.idno">Idno</Translate>
              </span>
            </dt>
            <dd>{ongRequestEntity.idno}</dd>
            <dt>
              <span id="registrationDate">
                <Translate contentKey="volunteerApp.ongRequest.registrationDate">Registration Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={ongRequestEntity.registrationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="volunteerApp.ongRequest.user">User</Translate>
            </dt>
            <dd>{ongRequestEntity.user ? ongRequestEntity.user.login : ''}</dd>
          </dl>
          <Button tag={Link} to="/ong-request" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/ong-request/${ongRequestEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ ongRequest }: IRootState) => ({
  ongRequestEntity: ongRequest.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OngRequestDetail);
