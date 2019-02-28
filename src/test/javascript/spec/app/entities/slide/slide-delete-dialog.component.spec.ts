/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BackofficeTestModule } from '../../../test.module';
import { SlideDeleteDialogComponent } from 'app/entities/slide/slide-delete-dialog.component';
import { SlideService } from 'app/entities/slide/slide.service';

describe('Component Tests', () => {
    describe('Slide Management Delete Component', () => {
        let comp: SlideDeleteDialogComponent;
        let fixture: ComponentFixture<SlideDeleteDialogComponent>;
        let service: SlideService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BackofficeTestModule],
                declarations: [SlideDeleteDialogComponent]
            })
                .overrideTemplate(SlideDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SlideDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SlideService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
