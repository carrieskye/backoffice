import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';
import { IHomePage } from 'app/shared/model/home-page.model';
import { HomePageService } from 'app/entities/home-page';
import { ISlide } from 'app/shared/model/slide.model';
import { SlideService } from 'app/entities/slide';

@Component({
    selector: 'jhi-device-update',
    templateUrl: './device-update.component.html'
})
export class DeviceUpdateComponent implements OnInit {
    device: IDevice;
    isSaving: boolean;

    homepages: IHomePage[];

    slides: ISlide[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected deviceService: DeviceService,
        protected homePageService: HomePageService,
        protected slideService: SlideService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ device }) => {
            this.device = device;
        });
        this.homePageService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IHomePage[]>) => mayBeOk.ok),
                map((response: HttpResponse<IHomePage[]>) => response.body)
            )
            .subscribe((res: IHomePage[]) => (this.homepages = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.slideService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISlide[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISlide[]>) => response.body)
            )
            .subscribe((res: ISlide[]) => (this.slides = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.device.id !== undefined) {
            this.subscribeToSaveResponse(this.deviceService.update(this.device));
        } else {
            this.subscribeToSaveResponse(this.deviceService.create(this.device));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>) {
        result.subscribe((res: HttpResponse<IDevice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackHomePageById(index: number, item: IHomePage) {
        return item.id;
    }

    trackSlideById(index: number, item: ISlide) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
