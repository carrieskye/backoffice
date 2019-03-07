/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PersonelComponentsPage, PersonelDeleteDialog, PersonelUpdatePage } from './personel.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Personel e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let personelUpdatePage: PersonelUpdatePage;
    let personelComponentsPage: PersonelComponentsPage;
    let personelDeleteDialog: PersonelDeleteDialog;
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

    it('should load Personels', async () => {
        await navBarPage.goToEntity('personel');
        personelComponentsPage = new PersonelComponentsPage();
        await browser.wait(ec.visibilityOf(personelComponentsPage.title), 5000);
        expect(await personelComponentsPage.getTitle()).to.eq('backofficeApp.personel.home.title');
    });

    it('should load create Personel page', async () => {
        await personelComponentsPage.clickOnCreateButton();
        personelUpdatePage = new PersonelUpdatePage();
        expect(await personelUpdatePage.getPageTitle()).to.eq('backofficeApp.personel.home.createOrEditLabel');
        await personelUpdatePage.cancel();
    });

    it('should create and save Personels', async () => {
        const nbButtonsBeforeCreate = await personelComponentsPage.countDeleteButtons();

        await personelComponentsPage.clickOnCreateButton();
        await promise.all([personelUpdatePage.setNameInput('name'), personelUpdatePage.setPhotoInput(absolutePath)]);
        expect(await personelUpdatePage.getNameInput()).to.eq('name');
        const selectedIsIgnored = personelUpdatePage.getIsIgnoredInput();
        if (await selectedIsIgnored.isSelected()) {
            await personelUpdatePage.getIsIgnoredInput().click();
            expect(await personelUpdatePage.getIsIgnoredInput().isSelected()).to.be.false;
        } else {
            await personelUpdatePage.getIsIgnoredInput().click();
            expect(await personelUpdatePage.getIsIgnoredInput().isSelected()).to.be.true;
        }
        expect(await personelUpdatePage.getPhotoInput()).to.endsWith(fileNameToUpload);
        await personelUpdatePage.save();
        expect(await personelUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await personelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Personel', async () => {
        const nbButtonsBeforeDelete = await personelComponentsPage.countDeleteButtons();
        await personelComponentsPage.clickOnLastDeleteButton();

        personelDeleteDialog = new PersonelDeleteDialog();
        expect(await personelDeleteDialog.getDialogTitle()).to.eq('backofficeApp.personel.delete.question');
        await personelDeleteDialog.clickOnConfirmButton();

        expect(await personelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
