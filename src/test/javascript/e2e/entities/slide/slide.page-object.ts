import { element, by, ElementFinder } from 'protractor';

export class SlideComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-slide div table .btn-danger'));
    title = element.all(by.css('jhi-slide div h2#page-heading span')).first();

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

export class SlideUpdatePage {
    pageTitle = element(by.id('jhi-slide-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    imageInput = element(by.id('file_image'));
    ageCategorySelect = element(by.id('field_ageCategory'));
    genderSelect = element(by.id('field_gender'));
    deviceSelect = element(by.id('field_device'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setImageInput(image) {
        await this.imageInput.sendKeys(image);
    }

    async getImageInput() {
        return this.imageInput.getAttribute('value');
    }

    async setAgeCategorySelect(ageCategory) {
        await this.ageCategorySelect.sendKeys(ageCategory);
    }

    async getAgeCategorySelect() {
        return this.ageCategorySelect.element(by.css('option:checked')).getText();
    }

    async ageCategorySelectLastOption() {
        await this.ageCategorySelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setGenderSelect(gender) {
        await this.genderSelect.sendKeys(gender);
    }

    async getGenderSelect() {
        return this.genderSelect.element(by.css('option:checked')).getText();
    }

    async genderSelectLastOption() {
        await this.genderSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async deviceSelectLastOption() {
        await this.deviceSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async deviceSelectOption(option) {
        await this.deviceSelect.sendKeys(option);
    }

    getDeviceSelect(): ElementFinder {
        return this.deviceSelect;
    }

    async getDeviceSelectedOption() {
        return this.deviceSelect.element(by.css('option:checked')).getText();
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

export class SlideDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-slide-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-slide'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
