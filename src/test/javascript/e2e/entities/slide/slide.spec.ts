/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SlideComponentsPage, SlideDeleteDialog, SlideUpdatePage } from './slide.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Slide e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let slideUpdatePage: SlideUpdatePage;
    let slideComponentsPage: SlideComponentsPage;
    let slideDeleteDialog: SlideDeleteDialog;
    const fileNameToUpload = 'logo-jhipster.png';
    const fileToUpload = '../../../../../main/webapp/content/images/' + fileNameToUpload;
    const absolutePath = path.resolve(__dirname, fileToUpload);

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Slides', async () => {
        await navBarPage.goToEntity('slide');
        slideComponentsPage = new SlideComponentsPage();
        await browser.wait(ec.visibilityOf(slideComponentsPage.title), 5000);
        expect(await slideComponentsPage.getTitle()).to.eq('backofficeApp.slide.home.title');
    });

    it('should load create Slide page', async () => {
        await slideComponentsPage.clickOnCreateButton();
        slideUpdatePage = new SlideUpdatePage();
        expect(await slideUpdatePage.getPageTitle()).to.eq('backofficeApp.slide.home.createOrEditLabel');
        await slideUpdatePage.cancel();
    });

    it('should create and save Slides', async () => {
        const nbButtonsBeforeCreate = await slideComponentsPage.countDeleteButtons();

        await slideComponentsPage.clickOnCreateButton();
        await promise.all([
            slideUpdatePage.setImageInput(absolutePath),
            slideUpdatePage.ageCategorySelectLastOption(),
            slideUpdatePage.genderSelectLastOption()
            // slideUpdatePage.deviceSelectLastOption(),
        ]);
        expect(await slideUpdatePage.getImageInput()).to.endsWith(fileNameToUpload);
        await slideUpdatePage.save();
        expect(await slideUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await slideComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Slide', async () => {
        const nbButtonsBeforeDelete = await slideComponentsPage.countDeleteButtons();
        await slideComponentsPage.clickOnLastDeleteButton();

        slideDeleteDialog = new SlideDeleteDialog();
        expect(await slideDeleteDialog.getDialogTitle()).to.eq('backofficeApp.slide.delete.question');
        await slideDeleteDialog.clickOnConfirmButton();

        expect(await slideComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
