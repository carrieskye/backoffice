import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISlide } from 'app/shared/model/slide.model';

@Component({
    selector: 'jhi-slide-detail',
    templateUrl: './slide-detail.component.html'
})
export class SlideDetailComponent implements OnInit {
    slide: ISlide;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ slide }) => {
            this.slide = slide;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
