import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import VolunteerRequestComponentsPage, { VolunteerRequestDeleteDialog } from './volunteer-request.page-object';
import VolunteerRequestUpdatePage from './volunteer-request-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('VolunteerRequest e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let volunteerRequestComponentsPage: VolunteerRequestComponentsPage;
  let volunteerRequestUpdatePage: VolunteerRequestUpdatePage;
  let volunteerRequestDeleteDialog: VolunteerRequestDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load VolunteerRequests', async () => {
    await navBarPage.getEntityPage('volunteer-request');
    volunteerRequestComponentsPage = new VolunteerRequestComponentsPage();
    expect(await volunteerRequestComponentsPage.getTitle().getText()).to.match(/Volunteer Requests/);
  });

  it('should load create VolunteerRequest page', async () => {
    await volunteerRequestComponentsPage.clickOnCreateButton();
    volunteerRequestUpdatePage = new VolunteerRequestUpdatePage();
    expect(await volunteerRequestUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /volunteerApp.volunteerRequest.home.createOrEditLabel/
    );
    await volunteerRequestUpdatePage.cancel();
  });

  it('should create and save VolunteerRequests', async () => {
    async function createVolunteerRequest() {
      await volunteerRequestComponentsPage.clickOnCreateButton();
      await volunteerRequestUpdatePage.setRegistrationDateInput('01-01-2001');
      expect(await volunteerRequestUpdatePage.getRegistrationDateInput()).to.eq('2001-01-01');
      await volunteerRequestUpdatePage.userSelectLastOption();
      await volunteerRequestUpdatePage.projectSelectLastOption();
      await waitUntilDisplayed(volunteerRequestUpdatePage.getSaveButton());
      await volunteerRequestUpdatePage.save();
      await waitUntilHidden(volunteerRequestUpdatePage.getSaveButton());
      expect(await volunteerRequestUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createVolunteerRequest();
    await volunteerRequestComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await volunteerRequestComponentsPage.countDeleteButtons();
    await createVolunteerRequest();

    await volunteerRequestComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await volunteerRequestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last VolunteerRequest', async () => {
    await volunteerRequestComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await volunteerRequestComponentsPage.countDeleteButtons();
    await volunteerRequestComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    volunteerRequestDeleteDialog = new VolunteerRequestDeleteDialog();
    expect(await volunteerRequestDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /volunteerApp.volunteerRequest.delete.question/
    );
    await volunteerRequestDeleteDialog.clickOnConfirmButton();

    await volunteerRequestComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await volunteerRequestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
