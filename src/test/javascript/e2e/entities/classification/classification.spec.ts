/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ClassificationComponentsPage, ClassificationDeleteDialog, ClassificationUpdatePage } from './classification.page-object';

const expect = chai.expect;

describe('Classification e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let classificationUpdatePage: ClassificationUpdatePage;
    let classificationComponentsPage: ClassificationComponentsPage;
    let classificationDeleteDialog: ClassificationDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Classifications', async () => {
        await navBarPage.goToEntity('classification');
        classificationComponentsPage = new ClassificationComponentsPage();
        await browser.wait(ec.visibilityOf(classificationComponentsPage.title), 5000);
        expect(await classificationComponentsPage.getTitle()).to.eq('backofficeApp.classification.home.title');
    });

    it('should load create Classification page', async () => {
        await classificationComponentsPage.clickOnCreateButton();
        classificationUpdatePage = new ClassificationUpdatePage();
        expect(await classificationUpdatePage.getPageTitle()).to.eq('backofficeApp.classification.home.createOrEditLabel');
        await classificationUpdatePage.cancel();
    });

    it('should create and save Classifications', async () => {
        const nbButtonsBeforeCreate = await classificationComponentsPage.countDeleteButtons();

        await classificationComponentsPage.clickOnCreateButton();
        await promise.all([
            classificationUpdatePage.setPersonIdInput('personId'),
            classificationUpdatePage.setTimestampInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            classificationUpdatePage.setAgeInput('5'),
            classificationUpdatePage.genderSelectLastOption(),
            classificationUpdatePage.emotionSelectLastOption(),
            classificationUpdatePage.deviceSelectLastOption()
        ]);
        expect(await classificationUpdatePage.getPersonIdInput()).to.eq('personId');
        expect(await classificationUpdatePage.getTimestampInput()).to.contain('2001-01-01T02:30');
        expect(await classificationUpdatePage.getAgeInput()).to.eq('5');
        await classificationUpdatePage.save();
        expect(await classificationUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await classificationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Classification', async () => {
        const nbButtonsBeforeDelete = await classificationComponentsPage.countDeleteButtons();
        await classificationComponentsPage.clickOnLastDeleteButton();

        classificationDeleteDialog = new ClassificationDeleteDialog();
        expect(await classificationDeleteDialog.getDialogTitle()).to.eq('backofficeApp.classification.delete.question');
        await classificationDeleteDialog.clickOnConfirmButton();

        expect(await classificationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
