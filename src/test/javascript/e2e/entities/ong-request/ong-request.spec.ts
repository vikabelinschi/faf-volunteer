import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import OngRequestComponentsPage, { OngRequestDeleteDialog } from './ong-request.page-object';
import OngRequestUpdatePage from './ong-request-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('OngRequest e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ongRequestComponentsPage: OngRequestComponentsPage;
  let ongRequestUpdatePage: OngRequestUpdatePage;
  let ongRequestDeleteDialog: OngRequestDeleteDialog;

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

  it('should load OngRequests', async () => {
    await navBarPage.getEntityPage('ong-request');
    ongRequestComponentsPage = new OngRequestComponentsPage();
    expect(await ongRequestComponentsPage.getTitle().getText()).to.match(/Ong Requests/);
  });

  it('should load create OngRequest page', async () => {
    await ongRequestComponentsPage.clickOnCreateButton();
    ongRequestUpdatePage = new OngRequestUpdatePage();
    expect(await ongRequestUpdatePage.getPageTitle().getAttribute('id')).to.match(/volunteerApp.ongRequest.home.createOrEditLabel/);
    await ongRequestUpdatePage.cancel();
  });

  it('should create and save OngRequests', async () => {
    async function createOngRequest() {
      await ongRequestComponentsPage.clickOnCreateButton();
      await ongRequestUpdatePage.setNameInput('name');
      expect(await ongRequestUpdatePage.getNameInput()).to.match(/name/);
      await ongRequestUpdatePage.setIdnoInput('idno');
      expect(await ongRequestUpdatePage.getIdnoInput()).to.match(/idno/);
      await ongRequestUpdatePage.setRegistrationDateInput('01-01-2001');
      expect(await ongRequestUpdatePage.getRegistrationDateInput()).to.eq('2001-01-01');
      await ongRequestUpdatePage.userSelectLastOption();
      await waitUntilDisplayed(ongRequestUpdatePage.getSaveButton());
      await ongRequestUpdatePage.save();
      await waitUntilHidden(ongRequestUpdatePage.getSaveButton());
      expect(await ongRequestUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createOngRequest();
    await ongRequestComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await ongRequestComponentsPage.countDeleteButtons();
    await createOngRequest();

    await ongRequestComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await ongRequestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last OngRequest', async () => {
    await ongRequestComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await ongRequestComponentsPage.countDeleteButtons();
    await ongRequestComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    ongRequestDeleteDialog = new OngRequestDeleteDialog();
    expect(await ongRequestDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/volunteerApp.ongRequest.delete.question/);
    await ongRequestDeleteDialog.clickOnConfirmButton();

    await ongRequestComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await ongRequestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
