import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { LoginModalService, AccountService, Account } from 'app/core';
import { DeviceService } from 'app/entities/device';
import { MetricsService } from 'app/home/metrics.service';
import { IDevice } from 'app/shared/model/device.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    metrics: any;
    devices: IDevice[];

    constructor(
        private accountService: AccountService,
        private deviceService: DeviceService,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private metricsService: MetricsService
    ) {}

    getMetrics() {
        this.metricsService.all().subscribe(data => (this.metrics = data));
    }

    getDevices() {
        this.deviceService
            .query()
            .pipe(
                filter((res: HttpResponse<IDevice[]>) => res.ok),
                map((res: HttpResponse<IDevice[]>) => res.body)
            )
            .subscribe(
                (res: IDevice[]) => {
                    this.devices = res;
                },
                (res: HttpErrorResponse) => console.error('error fetching devices', res.message)
            );
    }

    ngOnInit() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();

        setInterval(() => this.getDevices(), 2000);
        this.getDevices();
        setInterval(() => this.getMetrics(), 2000);
        this.getMetrics();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    ratioToFemalePercentage(ratio) {
        return Math.round(ratio * 10000) / 100;
    }

    ratioToMalePercentage(ratio) {
        return Math.round((1 - ratio) * 10000) / 100;
    }
}
