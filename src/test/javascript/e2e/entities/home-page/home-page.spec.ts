/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { HomePageComponentsPage, HomePageDeleteDialog, HomePageUpdatePage } from './home-page.page-object';

const expect = chai.expect;

describe('HomePage e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let homePageUpdatePage: HomePageUpdatePage;
    let homePageComponentsPage: HomePageComponentsPage;
    let homePageDeleteDialog: HomePageDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load HomePages', async () => {
        await navBarPage.goToEntity('home-page');
        homePageComponentsPage = new HomePageComponentsPage();
        await browser.wait(ec.visibilityOf(homePageComponentsPage.title), 5000);
        expect(await homePageComponentsPage.getTitle()).to.eq('backofficeApp.homePage.home.title');
    });

    it('should load create HomePage page', async () => {
        await homePageComponentsPage.clickOnCreateButton();
        homePageUpdatePage = new HomePageUpdatePage();
        expect(await homePageUpdatePage.getPageTitle()).to.eq('backofficeApp.homePage.home.createOrEditLabel');
        await homePageUpdatePage.cancel();
    });

    it('should create and save HomePages', async () => {
        const nbButtonsBeforeCreate = await homePageComponentsPage.countDeleteButtons();

        await homePageComponentsPage.clickOnCreateButton();
        await promise.all([
            homePageUpdatePage.setNameInput('name'),
            homePageUpdatePage.setYoungMaleUrlInput('youngMaleUrl'),
            homePageUpdatePage.setYoungFemaleUrlInput('youngFemaleUrl'),
            homePageUpdatePage.setAdultMaleUrlInput('adultMaleUrl'),
            homePageUpdatePage.setAdultFemaleUrlInput('adultFemaleUrl')
        ]);
        expect(await homePageUpdatePage.getNameInput()).to.eq('name');
        expect(await homePageUpdatePage.getYoungMaleUrlInput()).to.eq('youngMaleUrl');
        expect(await homePageUpdatePage.getYoungFemaleUrlInput()).to.eq('youngFemaleUrl');
        expect(await homePageUpdatePage.getAdultMaleUrlInput()).to.eq('adultMaleUrl');
        expect(await homePageUpdatePage.getAdultFemaleUrlInput()).to.eq('adultFemaleUrl');
        await homePageUpdatePage.save();
        expect(await homePageUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await homePageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last HomePage', async () => {
        const nbButtonsBeforeDelete = await homePageComponentsPage.countDeleteButtons();
        await homePageComponentsPage.clickOnLastDeleteButton();

        homePageDeleteDialog = new HomePageDeleteDialog();
        expect(await homePageDeleteDialog.getDialogTitle()).to.eq('backofficeApp.homePage.delete.question');
        await homePageDeleteDialog.clickOnConfirmButton();

        expect(await homePageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
