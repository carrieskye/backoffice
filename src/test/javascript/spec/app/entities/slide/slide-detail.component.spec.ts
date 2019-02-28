/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BackofficeTestModule } from '../../../test.module';
import { SlideDetailComponent } from 'app/entities/slide/slide-detail.component';
import { Slide } from 'app/shared/model/slide.model';

describe('Component Tests', () => {
    describe('Slide Management Detail Component', () => {
        let comp: SlideDetailComponent;
        let fixture: ComponentFixture<SlideDetailComponent>;
        const route = ({ data: of({ slide: new Slide(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BackofficeTestModule],
                declarations: [SlideDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SlideDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SlideDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.slide).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
