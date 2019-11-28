import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './status.reducer';
import { IStatus } from 'app/shared/model/status.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStatusProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Status extends React.Component<IStatusProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { statusList, match } = this.props;
    return (
      <div>
        <h2 id="status-heading">
          <Translate contentKey="volunteerApp.status.home.title">Statuses</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="volunteerApp.status.home.createLabel">Create a new Status</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {statusList && statusList.length > 0 ? (
            <Table responsive aria-describedby="status-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="volunteerApp.status.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="volunteerApp.status.description">Description</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {statusList.map((status, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${status.id}`} color="link" size="sm">
                        {status.id}
                      </Button>
                    </td>
                    <td>{status.name}</td>
                    <td>{status.description}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${status.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${status.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${status.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="volunteerApp.status.home.notFound">No Statuses found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ status }: IRootState) => ({
  statusList: status.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Status);
