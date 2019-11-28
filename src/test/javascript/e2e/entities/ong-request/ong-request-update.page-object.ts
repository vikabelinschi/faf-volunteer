import { element, by, ElementFinder } from 'protractor';

export default class OngRequestUpdatePage {
  pageTitle: ElementFinder = element(by.id('volunteerApp.ongRequest.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#ong-request-name'));
  idnoInput: ElementFinder = element(by.css('input#ong-request-idno'));
  registrationDateInput: ElementFinder = element(by.css('input#ong-request-registrationDate'));
  userSelect: ElementFinder = element(by.css('select#ong-request-user'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setIdnoInput(idno) {
    await this.idnoInput.sendKeys(idno);
  }

  async getIdnoInput() {
    return this.idnoInput.getAttribute('value');
  }

  async setRegistrationDateInput(registrationDate) {
    await this.registrationDateInput.sendKeys(registrationDate);
  }

  async getRegistrationDateInput() {
    return this.registrationDateInput.getAttribute('value');
  }

  async userSelectLastOption() {
    await this.userSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userSelectOption(option) {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect() {
    return this.userSelect;
  }

  async getUserSelectedOption() {
    return this.userSelect.element(by.css('option:checked')).getText();
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
