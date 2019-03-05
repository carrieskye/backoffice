import { Component, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { ActivityService } from 'app/graphs/stores/activity/activity.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { DeviceService } from 'app/entities/device';
import { IDevice } from 'app/shared/model/device.model';
import { filter, map } from 'rxjs/operators';

@Component({
    selector: 'jhi-stores-activity',
    templateUrl: './activity.component.html',
    styles: []
})
export class ActivityComponent implements OnInit {
    interval = 15;
    devices: IDevice[];
    selectedDevice: IDevice;

    activitiesLabels: string[] = [];
    activitiesData: any[] = [{ data: [], label: 'Male' }, { data: [], label: 'Female' }];
    activitiesChartType = 'bar';
    activitiesLegend = true;
    activitiesOptions: any = { scaleShowVerticalLines: false, responsive: true };

    constructor(
        protected activityService: ActivityService,
        protected deviceService: DeviceService,
        protected jhiAlertService: JhiAlertService
    ) {}

    loadAll() {
        this.deviceService
            .query()
            .pipe(
                filter((res: HttpResponse<IDevice[]>) => res.ok),
                map((res: HttpResponse<IDevice[]>) => res.body)
            )
            .subscribe(
                (res: IDevice[]) => {
                    this.devices = res;
                    if (res.length > 0) {
                        this.selectStore(res[0]);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    updateTable() {
        this.activitiesLabels = [];
        this.activityService.query(this.selectedDevice.id, this.interval).subscribe(
            result => {
                const keys = Object.keys(result).sort();

                const maleData = [];
                const femaleData = [];

                keys.forEach(key => {
                    this.activitiesLabels.push(key);

                    maleData.push(result[key]['m']);
                    femaleData.push(result[key]['f']);
                });

                this.activitiesData = [{ data: maleData, label: 'Male' }, { data: femaleData, label: 'Female' }];
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
    }

    selectStore(device: IDevice) {
        this.selectedDevice = device;
        this.updateTable();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
