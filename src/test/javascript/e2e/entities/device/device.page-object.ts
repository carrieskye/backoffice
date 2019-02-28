import { element, by, ElementFinder } from 'protractor';

export class DeviceComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-device div table .btn-danger'));
    title = element.all(by.css('jhi-device div h2#page-heading span')).first();

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

export class DeviceUpdatePage {
    pageTitle = element(by.id('jhi-device-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    postalCodeInput = element(by.id('field_postalCode'));
    homepageSelect = element(by.id('field_homepage'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setPostalCodeInput(postalCode) {
        await this.postalCodeInput.sendKeys(postalCode);
    }

    async getPostalCodeInput() {
        return this.postalCodeInput.getAttribute('value');
    }

    async homepageSelectLastOption() {
        await this.homepageSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async homepageSelectOption(option) {
        await this.homepageSelect.sendKeys(option);
    }

    getHomepageSelect(): ElementFinder {
        return this.homepageSelect;
    }

    async getHomepageSelectedOption() {
        return this.homepageSelect.element(by.css('option:checked')).getText();
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

export class DeviceDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-device-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-device'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
