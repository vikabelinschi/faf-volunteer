import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './volunteer-request.reducer';
import { IVolunteerRequest } from 'app/shared/model/volunteer-request.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVolunteerRequestDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class VolunteerRequestDetail extends React.Component<IVolunteerRequestDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { volunteerRequestEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="volunteerApp.volunteerRequest.detail.title">VolunteerRequest</Translate> [
            <b>{volunteerRequestEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="registrationDate">
                <Translate contentKey="volunteerApp.volunteerRequest.registrationDate">Registration Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={volunteerRequestEntity.registrationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="volunteerApp.volunteerRequest.user">User</Translate>
            </dt>
            <dd>{volunteerRequestEntity.user ? volunteerRequestEntity.user.login : ''}</dd>
            <dt>
              <Translate contentKey="volunteerApp.volunteerRequest.project">Project</Translate>
            </dt>
            <dd>{volunteerRequestEntity.project ? volunteerRequestEntity.project.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/volunteer-request" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/volunteer-request/${volunteerRequestEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ volunteerRequest }: IRootState) => ({
  volunteerRequestEntity: volunteerRequest.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VolunteerRequestDetail);
