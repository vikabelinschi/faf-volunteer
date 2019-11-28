import { element, by, ElementFinder } from 'protractor';

export default class OngUpdatePage {
  pageTitle: ElementFinder = element(by.id('volunteerApp.ong.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#ong-name'));
  descriptionInput: ElementFinder = element(by.css('input#ong-description'));
  idnoInput: ElementFinder = element(by.css('input#ong-idno'));
  emailInput: ElementFinder = element(by.css('input#ong-email'));
  addressInput: ElementFinder = element(by.css('input#ong-address'));
  phoneInput: ElementFinder = element(by.css('input#ong-phone'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setIdnoInput(idno) {
    await this.idnoInput.sendKeys(idno);
  }

  async getIdnoInput() {
    return this.idnoInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return this.emailInput.getAttribute('value');
  }

  async setAddressInput(address) {
    await this.addressInput.sendKeys(address);
  }

  async getAddressInput() {
    return this.addressInput.getAttribute('value');
  }

  async setPhoneInput(phone) {
    await this.phoneInput.sendKeys(phone);
  }

  async getPhoneInput() {
    return this.phoneInput.getAttribute('value');
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
