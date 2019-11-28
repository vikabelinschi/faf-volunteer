import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import OngComponentsPage, { OngDeleteDialog } from './ong.page-object';
import OngUpdatePage from './ong-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Ong e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ongComponentsPage: OngComponentsPage;
  let ongUpdatePage: OngUpdatePage;
  let ongDeleteDialog: OngDeleteDialog;

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

  it('should load Ongs', async () => {
    await navBarPage.getEntityPage('ong');
    ongComponentsPage = new OngComponentsPage();
    expect(await ongComponentsPage.getTitle().getText()).to.match(/Ongs/);
  });

  it('should load create Ong page', async () => {
    await ongComponentsPage.clickOnCreateButton();
    ongUpdatePage = new OngUpdatePage();
    expect(await ongUpdatePage.getPageTitle().getAttribute('id')).to.match(/volunteerApp.ong.home.createOrEditLabel/);
    await ongUpdatePage.cancel();
  });

  it('should create and save Ongs', async () => {
    async function createOng() {
      await ongComponentsPage.clickOnCreateButton();
      await ongUpdatePage.setNameInput('name');
      expect(await ongUpdatePage.getNameInput()).to.match(/name/);
      await ongUpdatePage.setDescriptionInput('description');
      expect(await ongUpdatePage.getDescriptionInput()).to.match(/description/);
      await ongUpdatePage.setIdnoInput('idno');
      expect(await ongUpdatePage.getIdnoInput()).to.match(/idno/);
      await ongUpdatePage.setEmailInput('email');
      expect(await ongUpdatePage.getEmailInput()).to.match(/email/);
      await ongUpdatePage.setAddressInput('address');
      expect(await ongUpdatePage.getAddressInput()).to.match(/address/);
      await ongUpdatePage.setPhoneInput('phone');
      expect(await ongUpdatePage.getPhoneInput()).to.match(/phone/);
      await waitUntilDisplayed(ongUpdatePage.getSaveButton());
      await ongUpdatePage.save();
      await waitUntilHidden(ongUpdatePage.getSaveButton());
      expect(await ongUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createOng();
    await ongComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await ongComponentsPage.countDeleteButtons();
    await createOng();

    await ongComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await ongComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Ong', async () => {
    await ongComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await ongComponentsPage.countDeleteButtons();
    await ongComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    ongDeleteDialog = new OngDeleteDialog();
    expect(await ongDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/volunteerApp.ong.delete.question/);
    await ongDeleteDialog.clickOnConfirmButton();

    await ongComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await ongComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
