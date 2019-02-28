import { element, by, ElementFinder } from 'protractor';

export class ClassificationComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-classification div table .btn-danger'));
    title = element.all(by.css('jhi-classification div h2#page-heading span')).first();

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

export class ClassificationUpdatePage {
    pageTitle = element(by.id('jhi-classification-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    personIdInput = element(by.id('field_personId'));
    timestampInput = element(by.id('field_timestamp'));
    ageInput = element(by.id('field_age'));
    genderSelect = element(by.id('field_gender'));
    emotionSelect = element(by.id('field_emotion'));
    deviceSelect = element(by.id('field_device'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setPersonIdInput(personId) {
        await this.personIdInput.sendKeys(personId);
    }

    async getPersonIdInput() {
        return this.personIdInput.getAttribute('value');
    }

    async setTimestampInput(timestamp) {
        await this.timestampInput.sendKeys(timestamp);
    }

    async getTimestampInput() {
        return this.timestampInput.getAttribute('value');
    }

    async setAgeInput(age) {
        await this.ageInput.sendKeys(age);
    }

    async getAgeInput() {
        return this.ageInput.getAttribute('value');
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

    async setEmotionSelect(emotion) {
        await this.emotionSelect.sendKeys(emotion);
    }

    async getEmotionSelect() {
        return this.emotionSelect.element(by.css('option:checked')).getText();
    }

    async emotionSelectLastOption() {
        await this.emotionSelect
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

export class ClassificationDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-classification-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-classification'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
