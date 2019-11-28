import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './ong.reducer';
import { IOng } from 'app/shared/model/ong.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOngDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OngDetail extends React.Component<IOngDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ongEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="volunteerApp.ong.detail.title">Ong</Translate> [<b>{ongEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="volunteerApp.ong.name">Name</Translate>
              </span>
            </dt>
            <dd>{ongEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="volunteerApp.ong.description">Description</Translate>
              </span>
            </dt>
            <dd>{ongEntity.description}</dd>
            <dt>
              <span id="idno">
                <Translate contentKey="volunteerApp.ong.idno">Idno</Translate>
              </span>
            </dt>
            <dd>{ongEntity.idno}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="volunteerApp.ong.email">Email</Translate>
              </span>
            </dt>
            <dd>{ongEntity.email}</dd>
            <dt>
              <span id="address">
                <Translate contentKey="volunteerApp.ong.address">Address</Translate>
              </span>
            </dt>
            <dd>{ongEntity.address}</dd>
            <dt>
              <span id="phone">
                <Translate contentKey="volunteerApp.ong.phone">Phone</Translate>
              </span>
            </dt>
            <dd>{ongEntity.phone}</dd>
          </dl>
          <Button tag={Link} to="/ong" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/ong/${ongEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ ong }: IRootState) => ({
  ongEntity: ong.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OngDetail);
