import { element, by, ElementFinder } from 'protractor';

export default class ProjectUpdatePage {
  pageTitle: ElementFinder = element(by.id('volunteerApp.project.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#project-name'));
  descriptionInput: ElementFinder = element(by.css('input#project-description'));
  registrationDateInput: ElementFinder = element(by.css('input#project-registrationDate'));
  statusSelect: ElementFinder = element(by.css('select#project-status'));
  categorySelect: ElementFinder = element(by.css('select#project-category'));
  userSelect: ElementFinder = element(by.css('select#project-user'));
  ongSelect: ElementFinder = element(by.css('select#project-ong'));

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

  async setRegistrationDateInput(registrationDate) {
    await this.registrationDateInput.sendKeys(registrationDate);
  }

  async getRegistrationDateInput() {
    return this.registrationDateInput.getAttribute('value');
  }

  async statusSelectLastOption() {
    await this.statusSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async statusSelectOption(option) {
    await this.statusSelect.sendKeys(option);
  }

  getStatusSelect() {
    return this.statusSelect;
  }

  async getStatusSelectedOption() {
    return this.statusSelect.element(by.css('option:checked')).getText();
  }

  async categorySelectLastOption() {
    await this.categorySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async categorySelectOption(option) {
    await this.categorySelect.sendKeys(option);
  }

  getCategorySelect() {
    return this.categorySelect;
  }

  async getCategorySelectedOption() {
    return this.categorySelect.element(by.css('option:checked')).getText();
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

  async ongSelectLastOption() {
    await this.ongSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ongSelectOption(option) {
    await this.ongSelect.sendKeys(option);
  }

  getOngSelect() {
    return this.ongSelect;
  }

  async getOngSelectedOption() {
    return this.ongSelect.element(by.css('option:checked')).getText();
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
