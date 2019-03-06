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
    styleUrls: ['../../graphs.scss']
})
export class ActivityComponent implements OnInit {
    devicesLoading = false;
    graphLoading = false;

    interval = 15;
    devices: IDevice[];
    selectedDevice: IDevice;

    activitiesLabels: string[] = [];
    activitiesData: any[] = [{ data: [], label: 'F' }, { data: [], label: 'M' }];
    activitiesChartType = 'line';
    activitiesLegend = true;
    activitiesOptions: any = { scaleShowVerticalLines: false, responsive: true };

    constructor(
        protected activityService: ActivityService,
        protected deviceService: DeviceService,
        protected jhiAlertService: JhiAlertService
    ) {}

    loadAll() {
        this.devicesLoading = true;
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
                    this.devicesLoading = false;
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                    this.devicesLoading = false;
                }
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

                this.activitiesData = [{ data: femaleData, label: 'F' }, { data: maleData, label: 'M' }];
                this.graphLoading = false;
            },
            (res: HttpErrorResponse) => {
                this.onError(res.message);
                this.graphLoading = false;
            }
        );
    }

    ngOnInit() {
        this.loadAll();
    }

    selectStore(device: IDevice) {
        this.graphLoading = true;
        this.selectedDevice = device;
        this.updateTable();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
