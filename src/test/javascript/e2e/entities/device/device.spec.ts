/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DeviceComponentsPage, DeviceDeleteDialog, DeviceUpdatePage } from './device.page-object';

const expect = chai.expect;

describe('Device e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let deviceUpdatePage: DeviceUpdatePage;
    let deviceComponentsPage: DeviceComponentsPage;
    let deviceDeleteDialog: DeviceDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Devices', async () => {
        await navBarPage.goToEntity('device');
        deviceComponentsPage = new DeviceComponentsPage();
        await browser.wait(ec.visibilityOf(deviceComponentsPage.title), 5000);
        expect(await deviceComponentsPage.getTitle()).to.eq('backofficeApp.device.home.title');
    });

    it('should load create Device page', async () => {
        await deviceComponentsPage.clickOnCreateButton();
        deviceUpdatePage = new DeviceUpdatePage();
        expect(await deviceUpdatePage.getPageTitle()).to.eq('backofficeApp.device.home.createOrEditLabel');
        await deviceUpdatePage.cancel();
    });

    it('should create and save Devices', async () => {
        const nbButtonsBeforeCreate = await deviceComponentsPage.countDeleteButtons();

        await deviceComponentsPage.clickOnCreateButton();
        await promise.all([
            deviceUpdatePage.setNameInput('name'),
            deviceUpdatePage.setPostalCodeInput('5'),
            deviceUpdatePage.homepageSelectLastOption()
        ]);
        expect(await deviceUpdatePage.getNameInput()).to.eq('name');
        expect(await deviceUpdatePage.getPostalCodeInput()).to.eq('5');
        await deviceUpdatePage.save();
        expect(await deviceUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await deviceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Device', async () => {
        const nbButtonsBeforeDelete = await deviceComponentsPage.countDeleteButtons();
        await deviceComponentsPage.clickOnLastDeleteButton();

        deviceDeleteDialog = new DeviceDeleteDialog();
        expect(await deviceDeleteDialog.getDialogTitle()).to.eq('backofficeApp.device.delete.question');
        await deviceDeleteDialog.clickOnConfirmButton();

        expect(await deviceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
