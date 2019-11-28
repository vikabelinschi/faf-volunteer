import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import StatusComponentsPage, { StatusDeleteDialog } from './status.page-object';
import StatusUpdatePage from './status-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Status e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let statusComponentsPage: StatusComponentsPage;
  let statusUpdatePage: StatusUpdatePage;
  let statusDeleteDialog: StatusDeleteDialog;

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

  it('should load Statuses', async () => {
    await navBarPage.getEntityPage('status');
    statusComponentsPage = new StatusComponentsPage();
    expect(await statusComponentsPage.getTitle().getText()).to.match(/Statuses/);
  });

  it('should load create Status page', async () => {
    await statusComponentsPage.clickOnCreateButton();
    statusUpdatePage = new StatusUpdatePage();
    expect(await statusUpdatePage.getPageTitle().getAttribute('id')).to.match(/volunteerApp.status.home.createOrEditLabel/);
    await statusUpdatePage.cancel();
  });

  it('should create and save Statuses', async () => {
    async function createStatus() {
      await statusComponentsPage.clickOnCreateButton();
      await statusUpdatePage.setNameInput('name');
      expect(await statusUpdatePage.getNameInput()).to.match(/name/);
      await statusUpdatePage.setDescriptionInput('description');
      expect(await statusUpdatePage.getDescriptionInput()).to.match(/description/);
      await waitUntilDisplayed(statusUpdatePage.getSaveButton());
      await statusUpdatePage.save();
      await waitUntilHidden(statusUpdatePage.getSaveButton());
      expect(await statusUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createStatus();
    await statusComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await statusComponentsPage.countDeleteButtons();
    await createStatus();

    await statusComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await statusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Status', async () => {
    await statusComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await statusComponentsPage.countDeleteButtons();
    await statusComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    statusDeleteDialog = new StatusDeleteDialog();
    expect(await statusDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/volunteerApp.status.delete.question/);
    await statusDeleteDialog.clickOnConfirmButton();

    await statusComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await statusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
