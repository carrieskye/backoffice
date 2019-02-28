import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHomePage } from 'app/shared/model/home-page.model';
import { HomePageService } from './home-page.service';

@Component({
    selector: 'jhi-home-page-delete-dialog',
    templateUrl: './home-page-delete-dialog.component.html'
})
export class HomePageDeleteDialogComponent {
    homePage: IHomePage;

    constructor(protected homePageService: HomePageService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.homePageService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'homePageListModification',
                content: 'Deleted an homePage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-home-page-delete-popup',
    template: ''
})
export class HomePageDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ homePage }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(HomePageDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.homePage = homePage;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/home-page', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/home-page', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
