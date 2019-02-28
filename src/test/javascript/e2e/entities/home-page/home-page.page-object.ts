import { element, by, ElementFinder } from 'protractor';

export class HomePageComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-home-page div table .btn-danger'));
    title = element.all(by.css('jhi-home-page div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class HomePageUpdatePage {
    pageTitle = element(by.id('jhi-home-page-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    youngMaleUrlInput = element(by.id('field_youngMaleUrl'));
    youngFemaleUrlInput = element(by.id('field_youngFemaleUrl'));
    adultMaleUrlInput = element(by.id('field_adultMaleUrl'));
    adultFemaleUrlInput = element(by.id('field_adultFemaleUrl'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setYoungMaleUrlInput(youngMaleUrl) {
        await this.youngMaleUrlInput.sendKeys(youngMaleUrl);
    }

    async getYoungMaleUrlInput() {
        return this.youngMaleUrlInput.getAttribute('value');
    }

    async setYoungFemaleUrlInput(youngFemaleUrl) {
        await this.youngFemaleUrlInput.sendKeys(youngFemaleUrl);
    }

    async getYoungFemaleUrlInput() {
        return this.youngFemaleUrlInput.getAttribute('value');
    }

    async setAdultMaleUrlInput(adultMaleUrl) {
        await this.adultMaleUrlInput.sendKeys(adultMaleUrl);
    }

    async getAdultMaleUrlInput() {
        return this.adultMaleUrlInput.getAttribute('value');
    }

    async setAdultFemaleUrlInput(adultFemaleUrl) {
        await this.adultFemaleUrlInput.sendKeys(adultFemaleUrl);
    }

    async getAdultFemaleUrlInput() {
        return this.adultFemaleUrlInput.getAttribute('value');
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class HomePageDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-homePage-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-homePage'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
