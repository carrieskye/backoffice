import { Component, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IDevice } from 'app/shared/model/device.model';
import { filter, map } from 'rxjs/operators';
import { DeviceService } from 'app/entities/device';
import { AgeDistributionService } from 'app/graphs/age-distribution/age-distribution.service';

@Component({
    selector: 'jhi-age-distribution',
    templateUrl: './age-distribution.component.html',
    styleUrls: ['../graphs.scss']
})
export class AgeDistributionComponent implements OnInit {
    devicesLoading = false;
    graphLoading = false;

    interval = 15;

    devices: IDevice[] = [];
    selectedDevice: IDevice;

    chartTypes = [{ type: 'bar', name: 'Bar Chart' }, { type: 'line', name: 'Line chart' }];
    selectedChartType = this.chartTypes[0];

    activitiesLabels: string[] = [];
    activitiesData: any[] = [{ data: [], label: 0 }, { data: [], label: 25 }, { data: [], label: 50 }, { data: [], label: 75 }];
    activitiesChartType = this.selectedChartType.type;
    activitiesLegend = true;
    activitiesOptions: any = { scaleShowVerticalLines: false, responsive: true };

    constructor(
        protected ageDistributionService: AgeDistributionService,
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
                    const allDevices = { id: -1, name: 'All', postalCode: 0, homepage: null };
                    this.devices.push(allDevices);
                    res.forEach(device => this.devices.push(device));

                    if (this.devices.length > 0) {
                        this.selectStore(this.devices[0]);
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
        this.graphLoading = true;
        this.activitiesLabels = [];
        const numberOfStores = this.selectedDevice.id === -1 ? this.devices.length - 1 : 1;
        this.ageDistributionService.query(this.selectedDevice.id, this.interval).subscribe(
            result => {
                const keys = Object.keys(result).sort();
                const labels = Object.keys(result[keys[0]]);

                let data = [];
                labels.forEach(() => data.push([]));

                keys.forEach(key => {
                    this.activitiesLabels.push(key);
                    Object.keys(result[key]).forEach((category, index) => {
                        data[index].push(Math.round(result[key][category] / numberOfStores));
                    });
                });

                this.activitiesData = [];
                labels.forEach((label, index) => {
                    this.activitiesData[index] = { data: data[index], label: label };
                });

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
        this.selectedDevice = device;
        this.updateTable();
    }

    selectChartType(chartType) {
        this.selectedChartType = chartType;
        this.activitiesChartType = this.selectedChartType.type;
        this.updateTable();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
