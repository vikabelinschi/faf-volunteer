import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './project.reducer';
import { IProject } from 'app/shared/model/project.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProjectDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProjectDetail extends React.Component<IProjectDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { projectEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="volunteerApp.project.detail.title">Project</Translate> [<b>{projectEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="volunteerApp.project.name">Name</Translate>
              </span>
            </dt>
            <dd>{projectEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="volunteerApp.project.description">Description</Translate>
              </span>
            </dt>
            <dd>{projectEntity.description}</dd>
            <dt>
              <span id="registrationDate">
                <Translate contentKey="volunteerApp.project.registrationDate">Registration Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={projectEntity.registrationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="volunteerApp.project.status">Status</Translate>
            </dt>
            <dd>{projectEntity.status ? projectEntity.status.name : ''}</dd>
            <dt>
              <Translate contentKey="volunteerApp.project.category">Category</Translate>
            </dt>
            <dd>{projectEntity.category ? projectEntity.category.name : ''}</dd>
            <dt>
              <Translate contentKey="volunteerApp.project.user">User</Translate>
            </dt>
            <dd>
              {projectEntity.users
                ? projectEntity.users.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.login}</a>
                      {i === projectEntity.users.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}{' '}
            </dd>
            <dt>
              <Translate contentKey="volunteerApp.project.ong">Ong</Translate>
            </dt>
            <dd>{projectEntity.ong ? projectEntity.ong.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/project" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/project/${projectEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ project }: IRootState) => ({
  projectEntity: project.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProjectDetail);
